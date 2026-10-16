package bklmc.ocelotsign.item;

import bklmc.ocelotsign.integration.mishanguc.MishangAccess;
import bklmc.ocelotsign.integration.mishanguc.TextContextNbtReader;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

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

    protected AbstractRoadSignBlockItem(Block block, Settings settings) {
        super(block, settings);
    }

    /** 追加物品提示信息，包括 NBT 中存储的文本预览 */
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world,
                             List<Text> tooltip, TooltipContext context) {
        addHintTooltip(tooltip);
        super.appendTooltip(stack, world, tooltip, context);

        NbtCompound nbt = stack.getSubNbt("BlockEntityTag");
        if (nbt == null) return;

        List<MutableText> texts = TextContextNbtReader.fromBlockEntityTagAsStyledText(nbt);
        if (!texts.isEmpty()) {
            tooltip.add(MishangAccess.translatable(Tooltip.ROAD_SIGN_BLOCK)
                    .formatted(Formatting.GRAY));
            tooltip.addAll(texts);
        }
    }

    /** 获取物品显示名称，若有 NBT 文本内容则追加预览 */
    @Override
    public Text getName(ItemStack stack) {
        NbtCompound nbt = stack.getSubNbt("BlockEntityTag");
        if (nbt == null) return super.getName(stack);

        MutableText text = super.getName(stack).copy();
        List<MutableText> texts = TextContextNbtReader.fromBlockEntityTagAsStyledText(nbt).stream()
                .limit(20)
                .collect(ImmutableList.toImmutableList());

        if (!texts.isEmpty()) {
            MutableText appendable = MishangAccess.empty();
            for (MutableText t : texts) {
                appendable.append(" ").append(t);
            }
            text.append(MishangAccess.literal(" -" + appendable.asTruncatedString(25))
                    .formatted(Formatting.GRAY));
        }
        return text;
    }

    /** 由子类实现以提供不同的提示内容 */
    protected abstract void addHintTooltip(List<Text> tooltip);
}
