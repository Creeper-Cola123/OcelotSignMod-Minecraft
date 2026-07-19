package bklmc.ocelotsign.integration.mishanguc;

import com.google.common.collect.ImmutableList;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 提供类型安全、防御性的 mishanguc API 访问
 *
 * @see IMishangucIntegration
 * @see MishangucIntegrationImpl
 */
public final class MishangAccess {
    private static final Logger LOGGER = LoggerFactory.getLogger("OcelotSignMod/MishangAccess");
    private static final MishangucIntegrationImpl IMPL = MishangucIntegrationImpl.getInstance();

    private MishangAccess() {
    }

    /** 检查 mishanguc 是否已加载且 API 可访问 */
    public static boolean isAvailable() {
        return IMPL.isAvailable();
    }

    /** 获取告示牌的默认文本上下文克隆 */
    public static Object getDefaultTextContext() {
        return IMPL.getDefaultTextContext();
    }

    /** 从方块实体 NBT 读取文本上下文 */
    public static List<?> readTextContextsFromNbt(NbtCompound nbt) {
        return IMPL.readTextContextsFromNbt(nbt);
    }

    /** 将文本上下文转换为用于显示的样式化文本 */
    public static MutableText toStyledText(Object context) {
        MutableText result = IMPL.textContextToStyledText(context);
        return result != null ? result : Text.literal("");
    }

    /** 通过 mishanguc 的文本桥接器创建可翻译文本 */
    public static MutableText translatable(String key) {
        MutableText result = IMPL.translatable(key);
        return result != null ? result : Text.literal("");
    }

    /** 通过 mishanguc 的文本桥接器创建字面文本 */
    public static MutableText literal(String text) {
        MutableText result = IMPL.literal(text);
        return result != null ? result : Text.literal(text);
    }

    /** 通过 mishanguc 的文本桥接器创建空文本 */
    public static MutableText empty() {
        MutableText result = IMPL.empty();
        return result != null ? result : Text.literal("");
    }

    /** 获取告示牌编辑同步的数据包 ID（1.21.1 中的 {@code PacketType} 标识符） */
    public static Identifier getEditSignFinishPacketId() {
        return IMPL.getEditSignFinishPacketId();
    }

    /** 获取用于告示牌编辑同步的 {@code PlayPayloadHandler} 对象 */
    public static Object getEditSignFinishPacketHandler() {
        return IMPL.getEditSignFinishPacketHandler();
    }

    /** 从 mishanguc 获取文本复制工具物品 */
    public static Object getTextCopyTool() {
        return IMPL.getTextCopyTool();
    }
}
