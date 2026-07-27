package bklmc.ocelotsign.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.List;

/**
 * 带柱道路指示牌方块对应的物品
 *
 * @see AbstractRoadSignBlockItem
 * @see bklmc.ocelotsign.block.RoadSignBlocks
 */
public class RoadSignBlockItem extends AbstractRoadSignBlockItem {

    private static final Component HINT_LINE_1 = Component.translatable(Tooltip.ROAD_SIGN_TOOLTIP_3).withStyle(ChatFormatting.GRAY);
    private static final Component HINT_LINE_2 = Component.translatable(Tooltip.ROAD_SIGN_TOOLTIP_4).withStyle(ChatFormatting.GRAY);
    private static final Component HINT_LINE_3 = Component.translatable(Tooltip.ROAD_SIGN_TOOLTIP_5).withStyle(ChatFormatting.GRAY);

    public RoadSignBlockItem(net.minecraft.world.level.block.Block block, Properties settings) {
        super(block, settings);
    }

    @Override
    protected void addHintTooltipToList(List<Component> tooltip) {
        tooltip.add(HINT_LINE_1);
        tooltip.add(HINT_LINE_2);
        tooltip.add(HINT_LINE_3);
    }
}
