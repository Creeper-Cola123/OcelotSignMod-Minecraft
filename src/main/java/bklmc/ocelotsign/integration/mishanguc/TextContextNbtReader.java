package bklmc.ocelotsign.integration.mishanguc;

import com.google.common.collect.ImmutableList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

/**
 * 从方块实体 NBT 读取 {@code TextContext} 数据
 *
 * @see MishangAccess
 */
public final class TextContextNbtReader {
    private TextContextNbtReader() {
    }

    /** 从方块实体 NBT 读取文本上下文 */
    public static @NotNull @Unmodifiable List<?> fromBlockEntityTag(@NotNull CompoundTag nbt) {
        return MishangAccess.readTextContextsFromNbt(nbt);
    }

    /** 从方块实体 NBT 读取文本上下文并转换为样式化文本 */
    public static @NotNull @Unmodifiable List<MutableComponent> fromBlockEntityTagAsStyledText(@NotNull CompoundTag nbt) {
        return MishangAccess.readTextContextsFromNbt(nbt).stream()
                .map(MishangAccess::toStyledText)
                .collect(ImmutableList.toImmutableList());
    }
}
