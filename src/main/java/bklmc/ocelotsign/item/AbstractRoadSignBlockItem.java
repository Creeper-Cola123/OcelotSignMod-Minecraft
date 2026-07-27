package bklmc.ocelotsign.item;

import bklmc.ocelotsign.integration.mishanguc.MishangAccess;
import bklmc.ocelotsign.integration.mishanguc.TextContextNbtReader;
import com.google.common.collect.ImmutableList;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.block.Block;

import java.util.List;

/**
 * 道路指示牌方块对应的物品基类
 *
 * @see RoadSignBlockItem
 * @see WallRoadSignBlockItem
 * @see bklmc.ocelotsign.blockentity.RoadSignBlockEntity
 */
public abstract class AbstractRoadSignBlockItem extends pers.solid.mishang.uc.item.NamedBlockItem {

    protected static final class Tooltip {
        public static final String ROAD_SIGN_TOOLTIP_1 = "block.ocelotsignmod.roadsign.tooltip.1";
        public static final String ROAD_SIGN_TOOLTIP_2 = "block.ocelotsignmod.roadsign.tooltip.2";
        public static final String ROAD_SIGN_TOOLTIP_3 = "block.ocelotsignmod.roadsign.tooltip.3";
        public static final String ROAD_SIGN_TOOLTIP_4 = "block.ocelotsignmod.roadsign.tooltip.4";
        public static final String ROAD_SIGN_TOOLTIP_5 = "block.ocelotsignmod.roadsign.tooltip.5";

        public static final String WALL_ROAD_SIGN_TOOLTIP_1 = "block.ocelotsignmod.wallroadsign.tooltip.1";
        public static final String WALL_ROAD_SIGN_TOOLTIP_2 = "block.ocelotsignmod.wallroadsign.tooltip.2";
        public static final String WALL_ROAD_SIGN_TOOLTIP_3 = "block.ocelotsignmod.wallroadsign.tooltip.3";
        public static final String WALL_ROAD_SIGN_TOOLTIP_4 = "block.ocelotsignmod.wallroadsign.tooltip.4";
        public static final String WALL_ROAD_SIGN_TOOLTIP_5 = "block.ocelotsignmod.wallroadsign.tooltip.5";
        public static final String WALL_ROAD_SIGN_TOOLTIP_6 = "block.ocelotsignmod.wallroadsign.tooltip.6";
        public static final String WALL_ROAD_SIGN_TOOLTIP_7 = "block.ocelotsignmod.wallroadsign.tooltip.7";

        public static final String ROAD_SIGN_BLOCK = "block.ocelotsignmod.tooltip.road_sign_block";
    }

    protected AbstractRoadSignBlockItem(Block block, Properties settings) {
        super(block, settings);
    }

    /** 获取物品显示名称，若有 NBT 文本内容则追加预览 */
    @Override
    public Component getName(net.minecraft.world.item.ItemStack stack) {
        CompoundTag nbt = getBlockEntityNbt(stack);
        if (nbt == null) return super.getName(stack);

        MutableComponent text = super.getName(stack).copy();
        List<MutableComponent> texts = TextContextNbtReader.fromBlockEntityTagAsStyledText(nbt).stream()
                .limit(20)
                .collect(ImmutableList.toImmutableList());

        if (!texts.isEmpty()) {
            MutableComponent appendable = MishangAccess.empty();
            for (MutableComponent t : texts) {
                appendable.append(" ").append(t);
            }
            // asTruncatedString 不再存在，使用 substring
            String truncated = appendable.getString();
            if (truncated.length() > 25) {
                truncated = truncated.substring(0, 25) + "...";
            }
            text.append(MishangAccess.literal(" -" + truncated)
                    .withStyle(ChatFormatting.GRAY));
        }
        return text;
    }

    /**
     * 从 ItemStack 中读取 BlockEntityTag 数据。
     * 使用 DataComponents.BLOCK_ENTITY_DATA 组件（26.1.2 起为 TypedEntityData）。
     */
    private static CompoundTag getBlockEntityNbt(net.minecraft.world.item.ItemStack stack) {
        var blockEntityData = stack.get(DataComponents.BLOCK_ENTITY_DATA);
        if (blockEntityData == null) return null;
        return blockEntityData.getUnsafe();
    }

    /** 由子类实现以提供不同的提示内容 */
    protected void addHintTooltipToList(List<Component> tooltip) {
    }
}
