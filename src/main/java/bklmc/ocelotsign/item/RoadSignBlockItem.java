package bklmc.ocelotsign.item;

import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

/**
 * 带柱道路指示牌方块对应的物品
 *
 * @see AbstractRoadSignBlockItem
 * @see bklmc.ocelotsign.block.RoadSignBlocks
 */
public class RoadSignBlockItem extends AbstractRoadSignBlockItem {

    private static final Text HINT_LINE_1 = Text.translatable(Tooltip.ROAD_SIGN_TOOLTIP_3).formatted(Formatting.GRAY);
    private static final Text HINT_LINE_2 = Text.translatable(Tooltip.ROAD_SIGN_TOOLTIP_4).formatted(Formatting.GRAY);
    private static final Text HINT_LINE_3 = Text.translatable(Tooltip.ROAD_SIGN_TOOLTIP_5).formatted(Formatting.GRAY);

    public RoadSignBlockItem(net.minecraft.block.Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    protected void addHintTooltipToList(List<Text> tooltip) {
        tooltip.add(HINT_LINE_1);
        tooltip.add(HINT_LINE_2);
        tooltip.add(HINT_LINE_3);
    }
}
