package bklmc.ocelotsign.integration.mishanguc;

import com.google.common.collect.ImmutableList;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * {@link IMishangucIntegration} 的运行时实现，通过反射桥接 mishanguc
 * 在不同小版本间不稳定的私有 API。
 *
 * <p>字段名、签名、NBT key 等反射目标均针对 mishanguc 1.6.1-1.21.4 校正；
 * 若升级/回退 mishanguc 版本，需要重新核对下列入口：
 * <ul>
 *   <li>{@code pers.solid.mishang.uc.text.TextContext#fromNbt}</li>
 *   <li>{@code pers.solid.mishang.uc.util.TextBridge}（{@code translatable / literal / empty}）</li>
 *   <li>{@code pers.solid.mishang.uc.blockentity.WallSignBlockEntity#DEFAULT_TEXT_CONTEXT}</li>
 *   <li>{@code pers.solid.mishang.uc.blockentity.BlockEntityWithText#PACKET_HANDLER}</li>
 *   <li>{@code pers.solid.mishang.uc.networking.SignEditFinishPayload.ID}</li>
 *   <li>{@code pers.solid.mishang.uc.item.TextCopyToolItem.TEXT_COPY_TOOL} /
 *       {@code pers.solid.mishang.uc.item.MishangucItems.TEXT_COPY_TOOL}</li>
 * </ul>
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
    private final Identifier editSignFinishPacketId;
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
        Identifier pid = null;
        boolean hasPh = false;
        Object tct = null;
        boolean hasTct = false;

        try {
            tc = Class.forName("pers.solid.mishang.uc.text.TextContext");
            tb = Class.forName("pers.solid.mishang.uc.util.TextBridge");

            // TextContext.fromNbt(NbtElement, RegistryWrapper.WrapperLookup)
            Method fromNbtMethod = tc.getMethod("fromNbt", NbtElement.class, RegistryWrapper.WrapperLookup.class);
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
            hasPh = ph != null;

            // 1.21.1 之前 SignEditFinishPayload 的 ID 直接是 Identifier，1.21.1 起改为 PacketType。
            // 通过 reflect 来兼容两种形态：先取 ID 字段，再尝试 getId / getPacketId 把 PacketType 还原为 Identifier。
            Class<?> payloadClass = Class.forName("pers.solid.mishang.uc.networking.SignEditFinishPayload");
            java.lang.reflect.Field idField = payloadClass.getField("ID");
            idField.setAccessible(true);
            Object payloadType = idField.get(null);
            if (payloadType != null) {
                pid = extractIdentifier(payloadType);
            }

            try {
                Class<?> itemsClass = Class.forName("pers.solid.mishang.uc.item.TextCopyToolItem");
                java.lang.reflect.Field textCopyToolField = itemsClass.getField("TEXT_COPY_TOOL");
                textCopyToolField.setAccessible(true);
                tct = textCopyToolField.get(null);
                hasTct = tct != null;
            } catch (ClassNotFoundException | NoSuchFieldException ignored) {
                // 若字段已迁移，回退查找 MishangucItems.TEXT_COPY_TOOL
                try {
                    Class<?> itemsClass = Class.forName("pers.solid.mishang.uc.item.MishangucItems");
                    java.lang.reflect.Field textCopyToolField = itemsClass.getField("TEXT_COPY_TOOL");
                    textCopyToolField.setAccessible(true);
                    tct = textCopyToolField.get(null);
                    hasTct = tct != null;
                } catch (ClassNotFoundException | NoSuchFieldException ignored2) {
                    // 既不是旧字段也没有新字段时忽略，保持其它功能可用
                }
            }
        } catch (ClassNotFoundException e) {
            // mishanguc 不在 classpath 时直接走不可用分支，本模组继续运行。
            LOGGER.warn("未找到 Mishanguc 相关类，回退到不可用模式");
            initAvailable = false;
        } catch (NoSuchMethodException | NoSuchFieldException e) {
            LOGGER.warn("Mishanguc API 签名与预期不一致: {}", e.getMessage());
            initAvailable = false;
        } catch (IllegalAccessException e) {
            LOGGER.error("无法访问 mishanguc 反射目标: {}", e.getMessage());
            initAvailable = false;
        } catch (Throwable t) {
            LOGGER.error("初始化 mishanguc 集成失败", t);
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
        this.editSignFinishPacketId = pid;
        this.hasPacketHandler = hasPh;
        this.textCopyTool = tct;
        this.hasTextCopyTool = hasTct;
    }

    /** 获取集成单例，懒加载且内部已做好反射初始化 */
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
            LOGGER.error("读取默认 TextContext 失败", t);
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

            // 1.21.1 起 WallSignBlockEntity 的 NBT 字段从 "text" 改为 "texts"；
            // 这里做向后兼容，旧存档仍能读取。
            NbtElement nbtText = nbt.get("texts");
            if (nbtText == null) {
                // 兼容 1.20.4 之前的 "text" 键
                nbtText = nbt.get("text");
            }
            if (nbtText == null) {
                return ImmutableList.of();
            }

            // 取 MinecraftClient.world 的 registryLookup，避免主入口持有
            // 客户端句柄的硬耦合；当客户端不可用时退化为空的 dummy registry。
            RegistryWrapper.WrapperLookup registryLookup = resolveRegistryLookup();

            if (nbtText instanceof NbtList) {
                ImmutableList.Builder<Object> builder = new ImmutableList.Builder<>();
                for (NbtElement element : (NbtList) nbtText) {
                    builder.add(fromNbtHandle.invoke(element, registryLookup));
                }
                return builder.build();
            } else if (nbtText instanceof NbtCompound || nbtText instanceof NbtString) {
                return ImmutableList.of(fromNbtHandle.invoke(nbtText, registryLookup));
            }
        } catch (Throwable t) {
            LOGGER.error("从 NBT 反序列化 TextContext 失败", t);
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
            LOGGER.error("转换 TextContext 为样式化文本失败", t);
            return Text.literal("");
        }
    }

    @Override
    public MutableText translatable(String key) {
        if (!available) return Text.literal("");
        try {
            return (MutableText) translatableHandle.invoke(key);
        } catch (Throwable t) {
            LOGGER.error("构造 translatable 文本失败: {}", key, t);
            return Text.literal("");
        }
    }

    @Override
    public MutableText literal(String text) {
        if (!available) return Text.literal(text);
        try {
            return (MutableText) literalHandle.invoke(text);
        } catch (Throwable t) {
            LOGGER.error("构造 literal 文本失败", t);
            return Text.literal(text);
        }
    }

    @Override
    public MutableText empty() {
        if (!available) return Text.literal("");
        try {
            return (MutableText) emptyHandle.invoke();
        } catch (Throwable t) {
            LOGGER.error("构造空文本失败", t);
            return Text.literal("");
        }
    }

    @Override
    public Identifier getEditSignFinishPacketId() {
        return editSignFinishPacketId;
    }

    @Override
    public Object getEditSignFinishPacketHandler() {
        return hasPacketHandler ? editSignFinishPacketHandler : null;
    }

    @Override
    public Object getTextCopyTool() {
        return hasTextCopyTool ? textCopyTool : null;
    }

    /**
     * 解析当前可用的 RegistryWrapper.WrapperLookup，
     * 优先复用客户端已加载的世界，回退到 DummyWrapperLookup。
     */
    private static RegistryWrapper.WrapperLookup resolveRegistryLookup() {
        try {
            Class<?> clientClass = Class.forName("net.minecraft.client.MinecraftClient");
            java.lang.reflect.Method getInstance = clientClass.getMethod("getInstance");
            Object client = getInstance.invoke(null);
            if (client != null) {
                java.lang.reflect.Method getWorld = clientClass.getMethod("getWorld");
                Object world = getWorld.invoke(client);
                if (world != null) {
                    java.lang.reflect.Method getRegistryManager = world.getClass().getMethod("getRegistryManager");
                    return (RegistryWrapper.WrapperLookup) getRegistryManager.invoke(world);
                }
            }
        } catch (Throwable t) {
            // 客户端入口不可用时退回 DummyWrapperLookup，不影响主流程
        }
        // 兜底：当客户端未初始化时返回 DummyWrapperLookup，
        // TextContext.fromNbt 会按需查询 registries，缺失即按空值处理。
        return DummyWrapperLookup.INSTANCE;
    }

    /**
     * 从 mishanguc 的 PacketType（1.21.1 起）中提取 Identifier，
     * 兼容 getId / getPacketId 两种命名。
     */
    private static Identifier extractIdentifier(Object payloadType) {
        try {
            Method getIdMethod = payloadType.getClass().getMethod("getId");
            Object idValue = getIdMethod.invoke(payloadType);
            if (idValue instanceof Identifier id) {
                return id;
            }
        } catch (NoSuchMethodException ignored) {
            try {
                Method getIdMethod = payloadType.getClass().getMethod("getPacketId");
                Object idValue = getIdMethod.invoke(payloadType);
                if (idValue instanceof Identifier id) {
                    return id;
                }
            } catch (NoSuchMethodException ignored2) {
                // 旧版 PayloadType 直接持有 Identifier 字段，已在调用方读完，这里忽略
            } catch (Throwable ignored3) {
                // 反射其他异常忽略
            }
        } catch (Throwable ignored) {
            // 反射其他异常忽略
        }
        return null;
    }

    /**
     * 极简的 registry lookup 实现，作为缺失环境的兜底；
     * 仅在 TextContext.fromNbt 调用 RegistryLookup 解析时返回空 Option。
     */
    @SuppressWarnings("rawtypes")
    private static final class DummyWrapperLookup implements RegistryWrapper.WrapperLookup {
        static final DummyWrapperLookup INSTANCE = new DummyWrapperLookup();

        private DummyWrapperLookup() {
        }

        @Override
        public <T> Optional<RegistryWrapper.Impl<T>> getOptional(net.minecraft.registry.RegistryKey<? extends net.minecraft.registry.Registry<? extends T>> key) {
            return Optional.empty();
        }

        @Override
        public Stream<net.minecraft.registry.RegistryKey<? extends net.minecraft.registry.Registry<?>>> streamAllRegistryKeys() {
            return Stream.empty();
        }
    }
}
