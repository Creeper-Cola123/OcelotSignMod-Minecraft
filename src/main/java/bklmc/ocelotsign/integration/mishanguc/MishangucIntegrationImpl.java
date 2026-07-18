package bklmc.ocelotsign.integration.mishanguc;

import com.google.common.collect.ImmutableList;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

/**
 * {@link IMishangucIntegration} 的生产实现
 *
 * @see IMishangucIntegration
 * @see MishangAccess
 */
public final class MishangucIntegrationImpl implements IMishangucIntegration {
    private static final Logger LOGGER = LoggerFactory.getLogger(MishangucIntegrationImpl.class);
    private static MishangucIntegrationImpl INSTANCE;

    private final boolean available;
    private final Class<?> textContextClass;
    private final Class<?> textBridgeClass;
    private final MethodHandle fromNbtHandle;
    private final MethodHandle defaultContextCloneHandle;
    private final MethodHandle translatableHandle;
    private final MethodHandle literalHandle;
    private final MethodHandle emptyHandle;
    private final Object editSignFinishPacketHandler;
    private final boolean hasPacketHandler;
    private final Object textCopyTool;
    private final boolean hasTextCopyTool;

    private MishangucIntegrationImpl() {
        boolean initAvailable = true;
        Class<?> tc = null;
        Class<?> tb = null;
        MethodHandle fh = null;
        MethodHandle dch = null;
        MethodHandle th = null;
        MethodHandle lh = null;
        MethodHandle eh = null;
        Object ph = null;
        boolean hasPh = false;
        Object tct = null;
        boolean hasTct = false;

        try {
            tc = Class.forName("pers.solid.mishang.uc.text.TextContext");
            tb = Class.forName("pers.solid.mishang.uc.util.TextBridge");

            Method fromNbtMethod = tc.getMethod("fromNbt", NbtElement.class, tc);
            fh = MethodHandles.lookup().unreflect(fromNbtMethod);

            Class<?> wallSignBeClass = Class.forName("pers.solid.mishang.uc.blockentity.WallSignBlockEntity");
            java.lang.reflect.Field defaultField = wallSignBeClass.getField("DEFAULT_TEXT_CONTEXT");
            defaultField.setAccessible(true);
            dch = MethodHandles.lookup().unreflectGetter(defaultField);

            Method translatableMethod = tb.getMethod("translatable", String.class);
            th = MethodHandles.lookup().unreflect(translatableMethod);

            Method literalMethod = tb.getMethod("literal", String.class);
            lh = MethodHandles.lookup().unreflect(literalMethod);

            Method emptyMethod = tb.getMethod("empty");
            eh = MethodHandles.lookup().unreflect(emptyMethod);

            Class<?> betClass = Class.forName("pers.solid.mishang.uc.blockentity.BlockEntityWithText");
            java.lang.reflect.Field packetHandlerField = betClass.getField("PACKET_HANDLER");
            packetHandlerField.setAccessible(true);
            ph = packetHandlerField.get(null);
            hasPh = true;

            Class<?> itemsClass = Class.forName("pers.solid.mishang.uc.item.MishangucItems");
            java.lang.reflect.Field textCopyToolField = itemsClass.getField("TEXT_COPY_TOOL");
            textCopyToolField.setAccessible(true);
            tct = textCopyToolField.get(null);
            hasTct = tct != null;
        } catch (ClassNotFoundException e) {
            LOGGER.warn("未找到 Mishanguc；运行在独立模式");
            initAvailable = false;
        } catch (NoSuchMethodException | NoSuchFieldException e) {
            LOGGER.warn("Mishanguc API 不匹配；部分功能可能不可用: {}", e.getMessage());
            initAvailable = false;
        } catch (IllegalAccessException e) {
            LOGGER.error("初始化 mishanguc 集成时访问被拒绝: {}", e.getMessage());
            initAvailable = false;
        } catch (Throwable t) {
            LOGGER.error("mishanguc 集成初始化时出现意外错误", t);
            initAvailable = false;
        }

        this.available = initAvailable;
        this.textContextClass = tc;
        this.textBridgeClass = tb;
        this.fromNbtHandle = fh;
        this.defaultContextCloneHandle = dch;
        this.translatableHandle = th;
        this.literalHandle = lh;
        this.emptyHandle = eh;
        this.editSignFinishPacketHandler = ph;
        this.hasPacketHandler = hasPh;
        this.textCopyTool = tct;
        this.hasTextCopyTool = hasTct;
    }

    /** 返回单例实例，首次访问时初始化 */
    public static MishangucIntegrationImpl getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MishangucIntegrationImpl();
        }
        return INSTANCE;
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    @Override
    public Object getDefaultTextContext() {
        if (!available) return null;
        try {
            Object defaultCtx = defaultContextCloneHandle.invoke();
            if (defaultCtx != null) {
                Method cloneMethod = textContextClass.getMethod("clone");
                return cloneMethod.invoke(defaultCtx);
            }
        } catch (Throwable t) {
            LOGGER.error("获取默认文本上下文失败", t);
        }
        return null;
    }

    @Override
    public List<?> readTextContextsFromNbt(NbtCompound nbt) {
        if (!available) return Collections.emptyList();

        try {
            Object defaultCtx = getDefaultTextContext();
            if (defaultCtx == null) {
                return ImmutableList.of();
            }

            NbtElement nbtText = nbt.get("text");
            if (nbtText instanceof NbtString) {
                return ImmutableList.of(fromNbtHandle.invoke(nbtText, defaultCtx));
            } else if (nbtText instanceof NbtCompound) {
                return ImmutableList.of(fromNbtHandle.invoke(nbtText, defaultCtx));
            } else if (nbtText instanceof NbtList) {
                ImmutableList.Builder<Object> builder = new ImmutableList.Builder<>();
                for (NbtElement element : (NbtList) nbtText) {
                    builder.add(fromNbtHandle.invoke(element, getDefaultTextContext()));
                }
                return builder.build();
            }
        } catch (Throwable t) {
            LOGGER.error("从 NBT 读取文本上下文失败", t);
        }
        return ImmutableList.of();
    }

    @Override
    public MutableText textContextToStyledText(Object context) {
        if (!available || context == null) return Text.literal("");

        try {
            Method asStyledText = textContextClass.getMethod("asStyledText");
            return (MutableText) asStyledText.invoke(context);
        } catch (Throwable t) {
            LOGGER.error("将文本上下文转换为样式化文本失败", t);
            return Text.literal("");
        }
    }

    @Override
    public MutableText translatable(String key) {
        if (!available) return Text.literal("");
        try {
            return (MutableText) translatableHandle.invoke(key);
        } catch (Throwable t) {
            LOGGER.error("创建可翻译文本失败: {}", key, t);
            return Text.literal("");
        }
    }

    @Override
    public MutableText literal(String text) {
        if (!available) return Text.literal(text);
        try {
            return (MutableText) literalHandle.invoke(text);
        } catch (Throwable t) {
            LOGGER.error("创建字面文本失败", t);
            return Text.literal(text);
        }
    }

    @Override
    public MutableText empty() {
        if (!available) return Text.literal("");
        try {
            return (MutableText) emptyHandle.invoke();
        } catch (Throwable t) {
            LOGGER.error("创建空文本失败", t);
            return Text.literal("");
        }
    }

    @Override
    public Object getEditSignFinishPacketHandler() {
        return hasPacketHandler ? editSignFinishPacketHandler : null;
    }

    @Override
    public Object getTextCopyTool() {
        return hasTextCopyTool ? textCopyTool : null;
    }
}
