package bklmc.ocelotsign.integration.mishanguc;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;

/**
 * mishanguc 集成的抽象接口
 *
 * @see MishangucIntegrationImpl
 * @see MishangAccess
 */
public interface IMishangucIntegration {

    /** 检查 mishanguc 是否已加载且可用 */
    boolean isAvailable();

    /** 获取 mishanguc 告示牌的默认文本上下文（克隆） */
    @Nullable
    Object getDefaultTextContext();

    /** 从方块实体 NBT 中读取文本上下文列表 */
    @NotNull
    List<?> readTextContextsFromNbt(@NotNull CompoundTag nbt);

    /** 将文本上下文转换为用于显示的样式化文本 */
    @NotNull
    MutableComponent textContextToStyledText(@NotNull Object context);

    /** 通过 mishanguc 的桥接器创建可翻译文本 */
    @NotNull
    MutableComponent translatable(@NotNull String key);

    /** 通过 mishanguc 的桥接器创建字面文本 */
    @NotNull
    MutableComponent literal(@NotNull String text);

    /** 通过 mishanguc 的桥接器创建空文本 */
    @NotNull
    MutableComponent empty();

    /** 获取用于告示牌编辑同步的数据包 ID（1.21.1 中用于 {@code PlayPayloadHandler} 注册） */
    @Nullable
    Identifier getEditSignFinishPacketId();

    /** 获取用于告示牌编辑同步的 {@code PlayPayloadHandler} 对象（{@code BlockEntityWithText.PACKET_HANDLER}） */
    @Nullable
    Object getEditSignFinishPacketHandler();

    /** 从 mishanguc 获取文本复制工具物品 */
    @Nullable
    Object getTextCopyTool();
}
