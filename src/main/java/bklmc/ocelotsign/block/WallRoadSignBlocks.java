package bklmc.ocelotsign.block;

import bklmc.ocelotsign.OcelotSignMod;
import bklmc.ocelotsign.block.custom.WallRoadSignBlock;
import bklmc.ocelotsign.item.WallRoadSignBlockItem;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.block.AbstractBlock;

/**
 * 墙上道路指示牌方块注册中心
 *
 * @see bklmc.ocelotsign.block.custom.WallRoadSignBlock
 */
public class WallRoadSignBlocks {
    private static RegistryKey<Block> getBlockKey(String id) {
        return RegistryKey.of(Registries.BLOCK.getKey(), Identifier.of(OcelotSignMod.MOD_ID, id));
    }

    private static RegistryKey<Item> getItemKey(String id) {
        return RegistryKey.of(Registries.ITEM.getKey(), Identifier.of(OcelotSignMod.MOD_ID, id));
    }

    private static AbstractBlock.Settings createSettings(String id, Block baseBlock) {
        return AbstractBlock.Settings.copy(baseBlock)
                .strength(0.2f, 0.2f)
                .nonOpaque()
                .registryKey(getBlockKey(id));
    }

    private static Block registerWallRoadSignBlock(String id, Block baseBlock) {
        WallRoadSignBlock block = new WallRoadSignBlock(baseBlock, createSettings(id, baseBlock));
        Block registeredBlock = Registry.register(Registries.BLOCK, getBlockKey(id), block);
        Registry.register(Registries.ITEM, getItemKey(id),
                new WallRoadSignBlockItem(block, new Item.Settings().registryKey(getItemKey(id))));
        return registeredBlock;
    }

    public static final Block BLUE_WALL_ROAD_SIGN_LEFT_TOP = registerWallRoadSignBlock("wallroadsign/blue/left_top", Blocks.BLUE_CONCRETE);
    public static final Block BLUE_WALL_ROAD_SIGN_TOP = registerWallRoadSignBlock("wallroadsign/blue/top", Blocks.BLUE_CONCRETE);
    public static final Block BLUE_WALL_ROAD_SIGN_RIGHT_TOP = registerWallRoadSignBlock("wallroadsign/blue/right_top", Blocks.BLUE_CONCRETE);
    public static final Block BLUE_WALL_ROAD_SIGN_LEFT = registerWallRoadSignBlock("wallroadsign/blue/left", Blocks.BLUE_CONCRETE);
    public static final Block BLUE_WALL_ROAD_SIGN_MIDDLE = registerWallRoadSignBlock("wallroadsign/blue/middle", Blocks.BLUE_CONCRETE);
    public static final Block BLUE_WALL_ROAD_SIGN_RIGHT = registerWallRoadSignBlock("wallroadsign/blue/right", Blocks.BLUE_CONCRETE);
    public static final Block BLUE_WALL_ROAD_SIGN_LEFT_BOTTOM = registerWallRoadSignBlock("wallroadsign/blue/left_bottom", Blocks.BLUE_CONCRETE);
    public static final Block BLUE_WALL_ROAD_SIGN_BOTTOM = registerWallRoadSignBlock("wallroadsign/blue/bottom", Blocks.BLUE_CONCRETE);
    public static final Block BLUE_WALL_ROAD_SIGN_RIGHT_BOTTOM = registerWallRoadSignBlock("wallroadsign/blue/right_bottom", Blocks.BLUE_CONCRETE);
    public static final Block BLUE_WALL_ROAD_SIGN_HORIZONTAL_LEFT = registerWallRoadSignBlock("wallroadsign/blue/horizontal_left", Blocks.BLUE_CONCRETE);
    public static final Block BLUE_WALL_ROAD_SIGN_HORIZONTAL_MIDDLE = registerWallRoadSignBlock("wallroadsign/blue/horizontal_middle", Blocks.BLUE_CONCRETE);
    public static final Block BLUE_WALL_ROAD_SIGN_HORIZONTAL_RIGHT = registerWallRoadSignBlock("wallroadsign/blue/horizontal_right", Blocks.BLUE_CONCRETE);
    public static final Block BLUE_WALL_ROAD_SIGN_VERTICAL_TOP = registerWallRoadSignBlock("wallroadsign/blue/vertical_top", Blocks.BLUE_CONCRETE);
    public static final Block BLUE_WALL_ROAD_SIGN_VERTICAL_MIDDLE = registerWallRoadSignBlock("wallroadsign/blue/vertical_middle", Blocks.BLUE_CONCRETE);
    public static final Block BLUE_WALL_ROAD_SIGN_VERTICAL_BOTTOM = registerWallRoadSignBlock("wallroadsign/blue/vertical_bottom", Blocks.BLUE_CONCRETE);
    public static final Block BLUE_WALL_ROAD_SIGN_SMALL_A = registerWallRoadSignBlock("wallroadsign/blue/small_a", Blocks.BLUE_CONCRETE);
    public static final Block BLUE_WALL_ROAD_SIGN_SMALL_B = registerWallRoadSignBlock("wallroadsign/blue/small_b", Blocks.BLUE_CONCRETE);
    public static final Block BLUE_WALL_ROAD_SIGN_RHOMBUS = registerWallRoadSignBlock("wallroadsign/blue/rhombus", Blocks.BLUE_CONCRETE);

    public static final Block GREEN_WALL_ROAD_SIGN_LEFT_TOP = registerWallRoadSignBlock("wallroadsign/green/left_top", Blocks.GREEN_CONCRETE);
    public static final Block GREEN_WALL_ROAD_SIGN_TOP = registerWallRoadSignBlock("wallroadsign/green/top", Blocks.GREEN_CONCRETE);
    public static final Block GREEN_WALL_ROAD_SIGN_RIGHT_TOP = registerWallRoadSignBlock("wallroadsign/green/right_top", Blocks.GREEN_CONCRETE);
    public static final Block GREEN_WALL_ROAD_SIGN_LEFT = registerWallRoadSignBlock("wallroadsign/green/left", Blocks.GREEN_CONCRETE);
    public static final Block GREEN_WALL_ROAD_SIGN_MIDDLE = registerWallRoadSignBlock("wallroadsign/green/middle", Blocks.GREEN_CONCRETE);
    public static final Block GREEN_WALL_ROAD_SIGN_RIGHT = registerWallRoadSignBlock("wallroadsign/green/right", Blocks.GREEN_CONCRETE);
    public static final Block GREEN_WALL_ROAD_SIGN_LEFT_BOTTOM = registerWallRoadSignBlock("wallroadsign/green/left_bottom", Blocks.GREEN_CONCRETE);
    public static final Block GREEN_WALL_ROAD_SIGN_BOTTOM = registerWallRoadSignBlock("wallroadsign/green/bottom", Blocks.GREEN_CONCRETE);
    public static final Block GREEN_WALL_ROAD_SIGN_RIGHT_BOTTOM = registerWallRoadSignBlock("wallroadsign/green/right_bottom", Blocks.GREEN_CONCRETE);
    public static final Block GREEN_WALL_ROAD_SIGN_HORIZONTAL_LEFT = registerWallRoadSignBlock("wallroadsign/green/horizontal_left", Blocks.GREEN_CONCRETE);
    public static final Block GREEN_WALL_ROAD_SIGN_HORIZONTAL_MIDDLE = registerWallRoadSignBlock("wallroadsign/green/horizontal_middle", Blocks.GREEN_CONCRETE);
    public static final Block GREEN_WALL_ROAD_SIGN_HORIZONTAL_RIGHT = registerWallRoadSignBlock("wallroadsign/green/horizontal_right", Blocks.GREEN_CONCRETE);
    public static final Block GREEN_WALL_ROAD_SIGN_VERTICAL_TOP = registerWallRoadSignBlock("wallroadsign/green/vertical_top", Blocks.GREEN_CONCRETE);
    public static final Block GREEN_WALL_ROAD_SIGN_VERTICAL_MIDDLE = registerWallRoadSignBlock("wallroadsign/green/vertical_middle", Blocks.GREEN_CONCRETE);
    public static final Block GREEN_WALL_ROAD_SIGN_VERTICAL_BOTTOM = registerWallRoadSignBlock("wallroadsign/green/vertical_bottom", Blocks.GREEN_CONCRETE);
    public static final Block GREEN_WALL_ROAD_SIGN_SMALL_A = registerWallRoadSignBlock("wallroadsign/green/small_a", Blocks.GREEN_CONCRETE);
    public static final Block GREEN_WALL_ROAD_SIGN_SMALL_B = registerWallRoadSignBlock("wallroadsign/green/small_b", Blocks.GREEN_CONCRETE);
    public static final Block GREEN_WALL_ROAD_SIGN_RHOMBUS = registerWallRoadSignBlock("wallroadsign/green/rhombus", Blocks.GREEN_CONCRETE);

    public static final Block YELLOW_WALL_ROAD_SIGN_LEFT_TOP = registerWallRoadSignBlock("wallroadsign/yellow/left_top", Blocks.YELLOW_CONCRETE);
    public static final Block YELLOW_WALL_ROAD_SIGN_TOP = registerWallRoadSignBlock("wallroadsign/yellow/top", Blocks.YELLOW_CONCRETE);
    public static final Block YELLOW_WALL_ROAD_SIGN_RIGHT_TOP = registerWallRoadSignBlock("wallroadsign/yellow/right_top", Blocks.YELLOW_CONCRETE);
    public static final Block YELLOW_WALL_ROAD_SIGN_LEFT = registerWallRoadSignBlock("wallroadsign/yellow/left", Blocks.YELLOW_CONCRETE);
    public static final Block YELLOW_WALL_ROAD_SIGN_MIDDLE = registerWallRoadSignBlock("wallroadsign/yellow/middle", Blocks.YELLOW_CONCRETE);
    public static final Block YELLOW_WALL_ROAD_SIGN_RIGHT = registerWallRoadSignBlock("wallroadsign/yellow/right", Blocks.YELLOW_CONCRETE);
    public static final Block YELLOW_WALL_ROAD_SIGN_LEFT_BOTTOM = registerWallRoadSignBlock("wallroadsign/yellow/left_bottom", Blocks.YELLOW_CONCRETE);
    public static final Block YELLOW_WALL_ROAD_SIGN_BOTTOM = registerWallRoadSignBlock("wallroadsign/yellow/bottom", Blocks.YELLOW_CONCRETE);
    public static final Block YELLOW_WALL_ROAD_SIGN_RIGHT_BOTTOM = registerWallRoadSignBlock("wallroadsign/yellow/right_bottom", Blocks.YELLOW_CONCRETE);
    public static final Block YELLOW_WALL_ROAD_SIGN_HORIZONTAL_LEFT = registerWallRoadSignBlock("wallroadsign/yellow/horizontal_left", Blocks.YELLOW_CONCRETE);
    public static final Block YELLOW_WALL_ROAD_SIGN_HORIZONTAL_MIDDLE = registerWallRoadSignBlock("wallroadsign/yellow/horizontal_middle", Blocks.YELLOW_CONCRETE);
    public static final Block YELLOW_WALL_ROAD_SIGN_HORIZONTAL_RIGHT = registerWallRoadSignBlock("wallroadsign/yellow/horizontal_right", Blocks.YELLOW_CONCRETE);
    public static final Block YELLOW_WALL_ROAD_SIGN_VERTICAL_TOP = registerWallRoadSignBlock("wallroadsign/yellow/vertical_top", Blocks.YELLOW_CONCRETE);
    public static final Block YELLOW_WALL_ROAD_SIGN_VERTICAL_MIDDLE = registerWallRoadSignBlock("wallroadsign/yellow/vertical_middle", Blocks.YELLOW_CONCRETE);
    public static final Block YELLOW_WALL_ROAD_SIGN_VERTICAL_BOTTOM = registerWallRoadSignBlock("wallroadsign/yellow/vertical_bottom", Blocks.YELLOW_CONCRETE);
    public static final Block YELLOW_WALL_ROAD_SIGN_SMALL_A = registerWallRoadSignBlock("wallroadsign/yellow/small_a", Blocks.YELLOW_CONCRETE);
    public static final Block YELLOW_WALL_ROAD_SIGN_SMALL_B = registerWallRoadSignBlock("wallroadsign/yellow/small_b", Blocks.YELLOW_CONCRETE);
    public static final Block YELLOW_WALL_ROAD_SIGN_RHOMBUS = registerWallRoadSignBlock("wallroadsign/yellow/rhombus", Blocks.YELLOW_CONCRETE);

    public static final Block WHITE_WALL_ROAD_SIGN_LEFT_TOP = registerWallRoadSignBlock("wallroadsign/white/left_top", Blocks.WHITE_CONCRETE);
    public static final Block WHITE_WALL_ROAD_SIGN_TOP = registerWallRoadSignBlock("wallroadsign/white/top", Blocks.WHITE_CONCRETE);
    public static final Block WHITE_WALL_ROAD_SIGN_RIGHT_TOP = registerWallRoadSignBlock("wallroadsign/white/right_top", Blocks.WHITE_CONCRETE);
    public static final Block WHITE_WALL_ROAD_SIGN_LEFT = registerWallRoadSignBlock("wallroadsign/white/left", Blocks.WHITE_CONCRETE);
    public static final Block WHITE_WALL_ROAD_SIGN_MIDDLE = registerWallRoadSignBlock("wallroadsign/white/middle", Blocks.WHITE_CONCRETE);
    public static final Block WHITE_WALL_ROAD_SIGN_RIGHT = registerWallRoadSignBlock("wallroadsign/white/right", Blocks.WHITE_CONCRETE);
    public static final Block WHITE_WALL_ROAD_SIGN_LEFT_BOTTOM = registerWallRoadSignBlock("wallroadsign/white/left_bottom", Blocks.WHITE_CONCRETE);
    public static final Block WHITE_WALL_ROAD_SIGN_BOTTOM = registerWallRoadSignBlock("wallroadsign/white/bottom", Blocks.WHITE_CONCRETE);
    public static final Block WHITE_WALL_ROAD_SIGN_RIGHT_BOTTOM = registerWallRoadSignBlock("wallroadsign/white/right_bottom", Blocks.WHITE_CONCRETE);
    public static final Block WHITE_WALL_ROAD_SIGN_HORIZONTAL_LEFT = registerWallRoadSignBlock("wallroadsign/white/horizontal_left", Blocks.WHITE_CONCRETE);
    public static final Block WHITE_WALL_ROAD_SIGN_HORIZONTAL_MIDDLE = registerWallRoadSignBlock("wallroadsign/white/horizontal_middle", Blocks.WHITE_CONCRETE);
    public static final Block WHITE_WALL_ROAD_SIGN_HORIZONTAL_RIGHT = registerWallRoadSignBlock("wallroadsign/white/horizontal_right", Blocks.WHITE_CONCRETE);
    public static final Block WHITE_WALL_ROAD_SIGN_VERTICAL_TOP = registerWallRoadSignBlock("wallroadsign/white/vertical_top", Blocks.WHITE_CONCRETE);
    public static final Block WHITE_WALL_ROAD_SIGN_VERTICAL_MIDDLE = registerWallRoadSignBlock("wallroadsign/white/vertical_middle", Blocks.WHITE_CONCRETE);
    public static final Block WHITE_WALL_ROAD_SIGN_VERTICAL_BOTTOM = registerWallRoadSignBlock("wallroadsign/white/vertical_bottom", Blocks.WHITE_CONCRETE);
    public static final Block WHITE_WALL_ROAD_SIGN_SMALL_A = registerWallRoadSignBlock("wallroadsign/white/small_a", Blocks.WHITE_CONCRETE);
    public static final Block WHITE_WALL_ROAD_SIGN_SMALL_B = registerWallRoadSignBlock("wallroadsign/white/small_b", Blocks.WHITE_CONCRETE);
    public static final Block WHITE_WALL_ROAD_SIGN_RHOMBUS = registerWallRoadSignBlock("wallroadsign/white/rhombus", Blocks.WHITE_CONCRETE);

    public static final Block BROWN_WALL_ROAD_SIGN_LEFT_TOP = registerWallRoadSignBlock("wallroadsign/brown/left_top", Blocks.BROWN_CONCRETE);
    public static final Block BROWN_WALL_ROAD_SIGN_TOP = registerWallRoadSignBlock("wallroadsign/brown/top", Blocks.BROWN_CONCRETE);
    public static final Block BROWN_WALL_ROAD_SIGN_RIGHT_TOP = registerWallRoadSignBlock("wallroadsign/brown/right_top", Blocks.BROWN_CONCRETE);
    public static final Block BROWN_WALL_ROAD_SIGN_LEFT = registerWallRoadSignBlock("wallroadsign/brown/left", Blocks.BROWN_CONCRETE);
    public static final Block BROWN_WALL_ROAD_SIGN_MIDDLE = registerWallRoadSignBlock("wallroadsign/brown/middle", Blocks.BROWN_CONCRETE);
    public static final Block BROWN_WALL_ROAD_SIGN_RIGHT = registerWallRoadSignBlock("wallroadsign/brown/right", Blocks.BROWN_CONCRETE);
    public static final Block BROWN_WALL_ROAD_SIGN_LEFT_BOTTOM = registerWallRoadSignBlock("wallroadsign/brown/left_bottom", Blocks.BROWN_CONCRETE);
    public static final Block BROWN_WALL_ROAD_SIGN_BOTTOM = registerWallRoadSignBlock("wallroadsign/brown/bottom", Blocks.BROWN_CONCRETE);
    public static final Block BROWN_WALL_ROAD_SIGN_RIGHT_BOTTOM = registerWallRoadSignBlock("wallroadsign/brown/right_bottom", Blocks.BROWN_CONCRETE);
    public static final Block BROWN_WALL_ROAD_SIGN_HORIZONTAL_LEFT = registerWallRoadSignBlock("wallroadsign/brown/horizontal_left", Blocks.BROWN_CONCRETE);
    public static final Block BROWN_WALL_ROAD_SIGN_HORIZONTAL_MIDDLE = registerWallRoadSignBlock("wallroadsign/brown/horizontal_middle", Blocks.BROWN_CONCRETE);
    public static final Block BROWN_WALL_ROAD_SIGN_HORIZONTAL_RIGHT = registerWallRoadSignBlock("wallroadsign/brown/horizontal_right", Blocks.BROWN_CONCRETE);
    public static final Block BROWN_WALL_ROAD_SIGN_VERTICAL_TOP = registerWallRoadSignBlock("wallroadsign/brown/vertical_top", Blocks.BROWN_CONCRETE);
    public static final Block BROWN_WALL_ROAD_SIGN_VERTICAL_MIDDLE = registerWallRoadSignBlock("wallroadsign/brown/vertical_middle", Blocks.BROWN_CONCRETE);
    public static final Block BROWN_WALL_ROAD_SIGN_VERTICAL_BOTTOM = registerWallRoadSignBlock("wallroadsign/brown/vertical_bottom", Blocks.BROWN_CONCRETE);
    public static final Block BROWN_WALL_ROAD_SIGN_SMALL_A = registerWallRoadSignBlock("wallroadsign/brown/small_a", Blocks.BROWN_CONCRETE);
    public static final Block BROWN_WALL_ROAD_SIGN_SMALL_B = registerWallRoadSignBlock("wallroadsign/brown/small_b", Blocks.BROWN_CONCRETE);
    public static final Block BROWN_WALL_ROAD_SIGN_RHOMBUS = registerWallRoadSignBlock("wallroadsign/brown/rhombus", Blocks.BROWN_CONCRETE);

    public static final Block ORANGE_WALL_ROAD_SIGN_LEFT_TOP = registerWallRoadSignBlock("wallroadsign/orange/left_top", Blocks.ORANGE_CONCRETE);
    public static final Block ORANGE_WALL_ROAD_SIGN_TOP = registerWallRoadSignBlock("wallroadsign/orange/top", Blocks.ORANGE_CONCRETE);
    public static final Block ORANGE_WALL_ROAD_SIGN_RIGHT_TOP = registerWallRoadSignBlock("wallroadsign/orange/right_top", Blocks.ORANGE_CONCRETE);
    public static final Block ORANGE_WALL_ROAD_SIGN_LEFT = registerWallRoadSignBlock("wallroadsign/orange/left", Blocks.ORANGE_CONCRETE);
    public static final Block ORANGE_WALL_ROAD_SIGN_MIDDLE = registerWallRoadSignBlock("wallroadsign/orange/middle", Blocks.ORANGE_CONCRETE);
    public static final Block ORANGE_WALL_ROAD_SIGN_RIGHT = registerWallRoadSignBlock("wallroadsign/orange/right", Blocks.ORANGE_CONCRETE);
    public static final Block ORANGE_WALL_ROAD_SIGN_LEFT_BOTTOM = registerWallRoadSignBlock("wallroadsign/orange/left_bottom", Blocks.ORANGE_CONCRETE);
    public static final Block ORANGE_WALL_ROAD_SIGN_BOTTOM = registerWallRoadSignBlock("wallroadsign/orange/bottom", Blocks.ORANGE_CONCRETE);
    public static final Block ORANGE_WALL_ROAD_SIGN_RIGHT_BOTTOM = registerWallRoadSignBlock("wallroadsign/orange/right_bottom", Blocks.ORANGE_CONCRETE);
    public static final Block ORANGE_WALL_ROAD_SIGN_HORIZONTAL_LEFT = registerWallRoadSignBlock("wallroadsign/orange/horizontal_left", Blocks.ORANGE_CONCRETE);
    public static final Block ORANGE_WALL_ROAD_SIGN_HORIZONTAL_MIDDLE = registerWallRoadSignBlock("wallroadsign/orange/horizontal_middle", Blocks.ORANGE_CONCRETE);
    public static final Block ORANGE_WALL_ROAD_SIGN_HORIZONTAL_RIGHT = registerWallRoadSignBlock("wallroadsign/orange/horizontal_right", Blocks.ORANGE_CONCRETE);
    public static final Block ORANGE_WALL_ROAD_SIGN_VERTICAL_TOP = registerWallRoadSignBlock("wallroadsign/orange/vertical_top", Blocks.ORANGE_CONCRETE);
    public static final Block ORANGE_WALL_ROAD_SIGN_VERTICAL_MIDDLE = registerWallRoadSignBlock("wallroadsign/orange/vertical_middle", Blocks.ORANGE_CONCRETE);
    public static final Block ORANGE_WALL_ROAD_SIGN_VERTICAL_BOTTOM = registerWallRoadSignBlock("wallroadsign/orange/vertical_bottom", Blocks.ORANGE_CONCRETE);
    public static final Block ORANGE_WALL_ROAD_SIGN_SMALL_A = registerWallRoadSignBlock("wallroadsign/orange/small_a", Blocks.ORANGE_CONCRETE);
    public static final Block ORANGE_WALL_ROAD_SIGN_SMALL_B = registerWallRoadSignBlock("wallroadsign/orange/small_b", Blocks.ORANGE_CONCRETE);

    public static final Block NO_PARKING = registerWallRoadSignBlock("wallroadsign/extra/no_parking", Blocks.BROWN_CONCRETE);
    public static final Block NO_LONG_TERM_PARKING = registerWallRoadSignBlock("wallroadsign/extra/no_long_term_parking", Blocks.BROWN_CONCRETE);
    public static final Block FORBID_2 = registerWallRoadSignBlock("wallroadsign/extra/forbid_2", Blocks.BROWN_CONCRETE);
    public static final Block FORBID_3 = registerWallRoadSignBlock("wallroadsign/extra/forbid_3", Blocks.BROWN_CONCRETE);
    public static final Block BLUE_ROUND = registerWallRoadSignBlock("wallroadsign/extra/blue_round", Blocks.BROWN_CONCRETE);
    public static final Block RED_ROUND = registerWallRoadSignBlock("wallroadsign/extra/red_round", Blocks.BROWN_CONCRETE);
    public static final Block GREEN_ROUND = registerWallRoadSignBlock("wallroadsign/extra/green_round", Blocks.BROWN_CONCRETE);
    public static final Block YELLOW_ROUND = registerWallRoadSignBlock("wallroadsign/extra/yellow_round", Blocks.BROWN_CONCRETE);
    public static final Block WHITE_ROUND = registerWallRoadSignBlock("wallroadsign/extra/white_round", Blocks.BROWN_CONCRETE);
    public static final Block FORBID_1 = registerWallRoadSignBlock("wallroadsign/extra/forbid_1", Blocks.BROWN_CONCRETE);
    public static final Block NO_ENTRY = registerWallRoadSignBlock("wallroadsign/extra/no_entry", Blocks.BROWN_CONCRETE);
    public static final Block HEIGHT_LIMIT = registerWallRoadSignBlock("wallroadsign/extra/height_limit", Blocks.BROWN_CONCRETE);
    public static final Block WIDTH_LIMIT = registerWallRoadSignBlock("wallroadsign/extra/width_limit", Blocks.BROWN_CONCRETE);
    public static final Block WEIGHT_LIMIT = registerWallRoadSignBlock("wallroadsign/extra/weight_limit", Blocks.BROWN_CONCRETE);
    public static final Block LIFT_SPEED_LIMIT = registerWallRoadSignBlock("wallroadsign/extra/lift_speed_limit", Blocks.BROWN_CONCRETE);
    public static final Block LOW_SPEED_LIMIT = registerWallRoadSignBlock("wallroadsign/extra/low_speed_limit", Blocks.BROWN_CONCRETE);
    public static final Block MAX_SPEED_LIMIT = registerWallRoadSignBlock("wallroadsign/extra/max_speed_limit", Blocks.BROWN_CONCRETE);
    public static final Block WARNING = registerWallRoadSignBlock("wallroadsign/extra/warning", Blocks.BROWN_CONCRETE);
    public static final Block WARNING_2 = registerWallRoadSignBlock("wallroadsign/extra/warning_2", Blocks.BROWN_CONCRETE);
    public static final Block WARNING_RED_STROKE = registerWallRoadSignBlock("wallroadsign/extra/warning_red_stroke", Blocks.BROWN_CONCRETE);
    public static final Block STOP = registerWallRoadSignBlock("wallroadsign/extra/stop", Blocks.BROWN_CONCRETE);
    public static final Block NATIONAL_EXPRESSWAY = registerWallRoadSignBlock("wallroadsign/extra/national_expressway", Blocks.BROWN_CONCRETE);
    public static final Block PROVINCIAL_EXPRESSWAY = registerWallRoadSignBlock("wallroadsign/extra/provincial_expressway", Blocks.BROWN_CONCRETE);
    public static final Block NATIONAL_HIGHWAY = registerWallRoadSignBlock("wallroadsign/extra/national_highway", Blocks.BROWN_CONCRETE);
    public static final Block PROVINCIAL_HIGHWAY = registerWallRoadSignBlock("wallroadsign/extra/provincial_highway", Blocks.BROWN_CONCRETE);
    public static final Block COUNTRY_HIGHWAY = registerWallRoadSignBlock("wallroadsign/extra/country_highway", Blocks.BROWN_CONCRETE);
    public static final Block YIELD = registerWallRoadSignBlock("wallroadsign/extra/yield", Blocks.BROWN_CONCRETE);
    public static final Block GREEN_MILEAGE_SIGN = registerWallRoadSignBlock("wallroadsign/extra/green_mileage_sign", Blocks.BROWN_CONCRETE);
    public static final Block BLUE_MILEAGE_SIGN = registerWallRoadSignBlock("wallroadsign/extra/blue_mileage_sign", Blocks.BROWN_CONCRETE);
    public static final Block GREEN_HECTOMETER_SIGN = registerWallRoadSignBlock("wallroadsign/extra/green_hectometer_sign", Blocks.BROWN_CONCRETE);
    public static final Block BLUE_HECTOMETER_SIGN = registerWallRoadSignBlock("wallroadsign/extra/blue_hectometer_sign", Blocks.BROWN_CONCRETE);

    public static final Block YIELD_RED_FILL = registerWallRoadSignBlock("wallroadsign/extra/yield_red_fill", Blocks.BROWN_CONCRETE);
    public static final Block FORBID_4 = registerWallRoadSignBlock("wallroadsign/extra/forbid_4", Blocks.BROWN_CONCRETE);
    public static final Block MAX_SPEED_LIMIT_YELLOW_FILL = registerWallRoadSignBlock("wallroadsign/extra/max_speed_limit_yellow_fill", Blocks.BROWN_CONCRETE);
    public static final Block WARNING_RED_STROKE_YELLOW_FILL = registerWallRoadSignBlock("wallroadsign/extra/warning_red_stroke_yellow_fill", Blocks.BROWN_CONCRETE);
    public static final Block LIFT_SPEED_LIMIT_YELLOW_FILL = registerWallRoadSignBlock("wallroadsign/extra/lift_speed_limit_yellow_fill", Blocks.BROWN_CONCRETE);
    public static final Block WARNING_BLUE_FILL = registerWallRoadSignBlock("wallroadsign/extra/warning_blue_fill", Blocks.BROWN_CONCRETE);
    public static final Block WARNING_YELLOW_FILL = registerWallRoadSignBlock("wallroadsign/extra/warning_yellow_fill", Blocks.BROWN_CONCRETE);
    public static final Block WARNING_GREEN_FILL = registerWallRoadSignBlock("wallroadsign/extra/warning_green_fill", Blocks.BROWN_CONCRETE);

    public static final Block BLUE_CHEVRON_ALIGNMENT_A_LEFT = registerWallRoadSignBlock("wallroadsign/chevron_alignment/blue/a_left", Blocks.BLUE_CONCRETE);
    public static final Block BLUE_CHEVRON_ALIGNMENT_A_RIGHT = registerWallRoadSignBlock("wallroadsign/chevron_alignment/blue/a_right", Blocks.BLUE_CONCRETE);
    public static final Block BLUE_CHEVRON_ALIGNMENT_B_LEFT = registerWallRoadSignBlock("wallroadsign/chevron_alignment/blue/b_left", Blocks.BLUE_CONCRETE);
    public static final Block BLUE_CHEVRON_ALIGNMENT_B_RIGHT = registerWallRoadSignBlock("wallroadsign/chevron_alignment/blue/b_right", Blocks.BLUE_CONCRETE);
    public static final Block BLUE_CHEVRON_ALIGNMENT_C_LEFT = registerWallRoadSignBlock("wallroadsign/chevron_alignment/blue/c_left", Blocks.BLUE_CONCRETE);
    public static final Block BLUE_CHEVRON_ALIGNMENT_C_RIGHT = registerWallRoadSignBlock("wallroadsign/chevron_alignment/blue/c_right", Blocks.BLUE_CONCRETE);
    public static final Block BLUE_CHEVRON_ALIGNMENT_C_UP = registerWallRoadSignBlock("wallroadsign/chevron_alignment/blue/c_up", Blocks.BLUE_CONCRETE);

    public static final Block GREEN_CHEVRON_ALIGNMENT_A_LEFT = registerWallRoadSignBlock("wallroadsign/chevron_alignment/green/a_left", Blocks.GREEN_CONCRETE);
    public static final Block GREEN_CHEVRON_ALIGNMENT_A_RIGHT = registerWallRoadSignBlock("wallroadsign/chevron_alignment/green/a_right", Blocks.GREEN_CONCRETE);
    public static final Block GREEN_CHEVRON_ALIGNMENT_B_LEFT = registerWallRoadSignBlock("wallroadsign/chevron_alignment/green/b_left", Blocks.GREEN_CONCRETE);
    public static final Block GREEN_CHEVRON_ALIGNMENT_B_RIGHT = registerWallRoadSignBlock("wallroadsign/chevron_alignment/green/b_right", Blocks.GREEN_CONCRETE);
    public static final Block GREEN_CHEVRON_ALIGNMENT_C_LEFT = registerWallRoadSignBlock("wallroadsign/chevron_alignment/green/c_left", Blocks.GREEN_CONCRETE);
    public static final Block GREEN_CHEVRON_ALIGNMENT_C_RIGHT = registerWallRoadSignBlock("wallroadsign/chevron_alignment/green/c_right", Blocks.GREEN_CONCRETE);
    public static final Block GREEN_CHEVRON_ALIGNMENT_C_UP = registerWallRoadSignBlock("wallroadsign/chevron_alignment/green/c_up", Blocks.GREEN_CONCRETE);

    public static final Block RED_CHEVRON_ALIGNMENT_A_LEFT = registerWallRoadSignBlock("wallroadsign/chevron_alignment/red/a_left", Blocks.RED_CONCRETE);
    public static final Block RED_CHEVRON_ALIGNMENT_A_RIGHT = registerWallRoadSignBlock("wallroadsign/chevron_alignment/red/a_right", Blocks.RED_CONCRETE);
    public static final Block RED_CHEVRON_ALIGNMENT_B_LEFT = registerWallRoadSignBlock("wallroadsign/chevron_alignment/red/b_left", Blocks.RED_CONCRETE);
    public static final Block RED_CHEVRON_ALIGNMENT_B_RIGHT = registerWallRoadSignBlock("wallroadsign/chevron_alignment/red/b_right", Blocks.RED_CONCRETE);
    public static final Block RED_CHEVRON_ALIGNMENT_C_LEFT = registerWallRoadSignBlock("wallroadsign/chevron_alignment/red/c_left", Blocks.RED_CONCRETE);
    public static final Block RED_CHEVRON_ALIGNMENT_C_RIGHT = registerWallRoadSignBlock("wallroadsign/chevron_alignment/red/c_right", Blocks.RED_CONCRETE);
    public static final Block RED_CHEVRON_ALIGNMENT_C_UP = registerWallRoadSignBlock("wallroadsign/chevron_alignment/red/c_up", Blocks.RED_CONCRETE);

    public static final Block YELLOW_CHEVRON_ALIGNMENT_A_LEFT = registerWallRoadSignBlock("wallroadsign/chevron_alignment/yellow/a_left", Blocks.YELLOW_CONCRETE);
    public static final Block YELLOW_CHEVRON_ALIGNMENT_A_RIGHT = registerWallRoadSignBlock("wallroadsign/chevron_alignment/yellow/a_right", Blocks.YELLOW_CONCRETE);
    public static final Block YELLOW_CHEVRON_ALIGNMENT_B_LEFT = registerWallRoadSignBlock("wallroadsign/chevron_alignment/yellow/b_left", Blocks.YELLOW_CONCRETE);
    public static final Block YELLOW_CHEVRON_ALIGNMENT_B_RIGHT = registerWallRoadSignBlock("wallroadsign/chevron_alignment/yellow/b_right", Blocks.YELLOW_CONCRETE);
    public static final Block YELLOW_CHEVRON_ALIGNMENT_C_LEFT = registerWallRoadSignBlock("wallroadsign/chevron_alignment/yellow/c_left", Blocks.YELLOW_CONCRETE);
    public static final Block YELLOW_CHEVRON_ALIGNMENT_C_RIGHT = registerWallRoadSignBlock("wallroadsign/chevron_alignment/yellow/c_right", Blocks.YELLOW_CONCRETE);
    public static final Block YELLOW_CHEVRON_ALIGNMENT_C_UP = registerWallRoadSignBlock("wallroadsign/chevron_alignment/yellow/c_up", Blocks.YELLOW_CONCRETE);

    public static final Block OVERHEAD_ROAD_EDGE_MARKER = registerWallRoadSignBlock("wallroadsign/extra/overhead_road_edge_marker", Blocks.WHITE_CONCRETE);

    public static final Block BLUE_WALL_ROAD_SIGN_MINI_01 = registerWallRoadSignBlock("wallroadsign/blue/mini_01", Blocks.BLUE_CONCRETE);
    public static final Block BLUE_WALL_ROAD_SIGN_MINI_02 = registerWallRoadSignBlock("wallroadsign/blue/mini_02", Blocks.BLUE_CONCRETE);
    public static final Block BLUE_WALL_ROAD_SIGN_MINI_03 = registerWallRoadSignBlock("wallroadsign/blue/mini_03", Blocks.BLUE_CONCRETE);
    public static final Block BLUE_WALL_ROAD_SIGN_MINI_04 = registerWallRoadSignBlock("wallroadsign/blue/mini_04", Blocks.BLUE_CONCRETE);
    public static final Block BLUE_WALL_ROAD_SIGN_MINI_05 = registerWallRoadSignBlock("wallroadsign/blue/mini_05", Blocks.BLUE_CONCRETE);
    public static final Block BLUE_WALL_ROAD_SIGN_MINI_06 = registerWallRoadSignBlock("wallroadsign/blue/mini_06", Blocks.BLUE_CONCRETE);
    public static final Block BLUE_WALL_ROAD_SIGN_MINI_07 = registerWallRoadSignBlock("wallroadsign/blue/mini_07", Blocks.BLUE_CONCRETE);
    public static final Block BLUE_WALL_ROAD_SIGN_MINI_08 = registerWallRoadSignBlock("wallroadsign/blue/mini_08", Blocks.BLUE_CONCRETE);
    public static final Block BLUE_WALL_ROAD_SIGN_MINI_09 = registerWallRoadSignBlock("wallroadsign/blue/mini_09", Blocks.BLUE_CONCRETE);
    public static final Block BLUE_WALL_ROAD_SIGN_MINI_10 = registerWallRoadSignBlock("wallroadsign/blue/mini_10", Blocks.BLUE_CONCRETE);
    public static final Block BLUE_WALL_ROAD_SIGN_MINI_11 = registerWallRoadSignBlock("wallroadsign/blue/mini_11", Blocks.BLUE_CONCRETE);
    public static final Block BLUE_WALL_ROAD_SIGN_MINI_12 = registerWallRoadSignBlock("wallroadsign/blue/mini_12", Blocks.BLUE_CONCRETE);
    public static final Block BLUE_WALL_ROAD_SIGN_MINI_13 = registerWallRoadSignBlock("wallroadsign/blue/mini_13", Blocks.BLUE_CONCRETE);
    public static final Block BLUE_WALL_ROAD_SIGN_MINI_14 = registerWallRoadSignBlock("wallroadsign/blue/mini_14", Blocks.BLUE_CONCRETE);
    public static final Block BLUE_WALL_ROAD_SIGN_MINI_15 = registerWallRoadSignBlock("wallroadsign/blue/mini_15", Blocks.BLUE_CONCRETE);
    public static final Block BLUE_WALL_ROAD_SIGN_MINI_16 = registerWallRoadSignBlock("wallroadsign/blue/mini_16", Blocks.BLUE_CONCRETE);
    public static final Block BLUE_WALL_ROAD_SIGN_MINI_17 = registerWallRoadSignBlock("wallroadsign/blue/mini_17", Blocks.BLUE_CONCRETE);
    public static final Block BLUE_WALL_ROAD_SIGN_MINI_18 = registerWallRoadSignBlock("wallroadsign/blue/mini_18", Blocks.BLUE_CONCRETE);
    public static final Block BLUE_WALL_ROAD_SIGN_MINI_19 = registerWallRoadSignBlock("wallroadsign/blue/mini_19", Blocks.BLUE_CONCRETE);

    public static final Block GREEN_WALL_ROAD_SIGN_MINI_01 = registerWallRoadSignBlock("wallroadsign/green/mini_01", Blocks.GREEN_CONCRETE);
    public static final Block GREEN_WALL_ROAD_SIGN_MINI_02 = registerWallRoadSignBlock("wallroadsign/green/mini_02", Blocks.GREEN_CONCRETE);
    public static final Block GREEN_WALL_ROAD_SIGN_MINI_03 = registerWallRoadSignBlock("wallroadsign/green/mini_03", Blocks.GREEN_CONCRETE);
    public static final Block GREEN_WALL_ROAD_SIGN_MINI_04 = registerWallRoadSignBlock("wallroadsign/green/mini_04", Blocks.GREEN_CONCRETE);
    public static final Block GREEN_WALL_ROAD_SIGN_MINI_05 = registerWallRoadSignBlock("wallroadsign/green/mini_05", Blocks.GREEN_CONCRETE);
    public static final Block GREEN_WALL_ROAD_SIGN_MINI_06 = registerWallRoadSignBlock("wallroadsign/green/mini_06", Blocks.GREEN_CONCRETE);
    public static final Block GREEN_WALL_ROAD_SIGN_MINI_07 = registerWallRoadSignBlock("wallroadsign/green/mini_07", Blocks.GREEN_CONCRETE);
    public static final Block GREEN_WALL_ROAD_SIGN_MINI_08 = registerWallRoadSignBlock("wallroadsign/green/mini_08", Blocks.GREEN_CONCRETE);
    public static final Block GREEN_WALL_ROAD_SIGN_MINI_09 = registerWallRoadSignBlock("wallroadsign/green/mini_09", Blocks.GREEN_CONCRETE);
    public static final Block GREEN_WALL_ROAD_SIGN_MINI_10 = registerWallRoadSignBlock("wallroadsign/green/mini_10", Blocks.GREEN_CONCRETE);
    public static final Block GREEN_WALL_ROAD_SIGN_MINI_11 = registerWallRoadSignBlock("wallroadsign/green/mini_11", Blocks.GREEN_CONCRETE);
    public static final Block GREEN_WALL_ROAD_SIGN_MINI_12 = registerWallRoadSignBlock("wallroadsign/green/mini_12", Blocks.GREEN_CONCRETE);
    public static final Block GREEN_WALL_ROAD_SIGN_MINI_13 = registerWallRoadSignBlock("wallroadsign/green/mini_13", Blocks.GREEN_CONCRETE);
    public static final Block GREEN_WALL_ROAD_SIGN_MINI_14 = registerWallRoadSignBlock("wallroadsign/green/mini_14", Blocks.GREEN_CONCRETE);
    public static final Block GREEN_WALL_ROAD_SIGN_MINI_15 = registerWallRoadSignBlock("wallroadsign/green/mini_15", Blocks.GREEN_CONCRETE);
    public static final Block GREEN_WALL_ROAD_SIGN_MINI_16 = registerWallRoadSignBlock("wallroadsign/green/mini_16", Blocks.GREEN_CONCRETE);
    public static final Block GREEN_WALL_ROAD_SIGN_MINI_17 = registerWallRoadSignBlock("wallroadsign/green/mini_17", Blocks.GREEN_CONCRETE);
    public static final Block GREEN_WALL_ROAD_SIGN_MINI_18 = registerWallRoadSignBlock("wallroadsign/green/mini_18", Blocks.GREEN_CONCRETE);
    public static final Block GREEN_WALL_ROAD_SIGN_MINI_19 = registerWallRoadSignBlock("wallroadsign/green/mini_19", Blocks.GREEN_CONCRETE);

    public static final Block YELLOW_WALL_ROAD_SIGN_MINI_01 = registerWallRoadSignBlock("wallroadsign/yellow/mini_01", Blocks.YELLOW_CONCRETE);
    public static final Block YELLOW_WALL_ROAD_SIGN_MINI_02 = registerWallRoadSignBlock("wallroadsign/yellow/mini_02", Blocks.YELLOW_CONCRETE);
    public static final Block YELLOW_WALL_ROAD_SIGN_MINI_03 = registerWallRoadSignBlock("wallroadsign/yellow/mini_03", Blocks.YELLOW_CONCRETE);
    public static final Block YELLOW_WALL_ROAD_SIGN_MINI_04 = registerWallRoadSignBlock("wallroadsign/yellow/mini_04", Blocks.YELLOW_CONCRETE);
    public static final Block YELLOW_WALL_ROAD_SIGN_MINI_05 = registerWallRoadSignBlock("wallroadsign/yellow/mini_05", Blocks.YELLOW_CONCRETE);
    public static final Block YELLOW_WALL_ROAD_SIGN_MINI_06 = registerWallRoadSignBlock("wallroadsign/yellow/mini_06", Blocks.YELLOW_CONCRETE);
    public static final Block YELLOW_WALL_ROAD_SIGN_MINI_07 = registerWallRoadSignBlock("wallroadsign/yellow/mini_07", Blocks.YELLOW_CONCRETE);
    public static final Block YELLOW_WALL_ROAD_SIGN_MINI_08 = registerWallRoadSignBlock("wallroadsign/yellow/mini_08", Blocks.YELLOW_CONCRETE);
    public static final Block YELLOW_WALL_ROAD_SIGN_MINI_09 = registerWallRoadSignBlock("wallroadsign/yellow/mini_09", Blocks.YELLOW_CONCRETE);
    public static final Block YELLOW_WALL_ROAD_SIGN_MINI_10 = registerWallRoadSignBlock("wallroadsign/yellow/mini_10", Blocks.YELLOW_CONCRETE);
    public static final Block YELLOW_WALL_ROAD_SIGN_MINI_11 = registerWallRoadSignBlock("wallroadsign/yellow/mini_11", Blocks.YELLOW_CONCRETE);
    public static final Block YELLOW_WALL_ROAD_SIGN_MINI_12 = registerWallRoadSignBlock("wallroadsign/yellow/mini_12", Blocks.YELLOW_CONCRETE);
    public static final Block YELLOW_WALL_ROAD_SIGN_MINI_13 = registerWallRoadSignBlock("wallroadsign/yellow/mini_13", Blocks.YELLOW_CONCRETE);
    public static final Block YELLOW_WALL_ROAD_SIGN_MINI_14 = registerWallRoadSignBlock("wallroadsign/yellow/mini_14", Blocks.YELLOW_CONCRETE);
    public static final Block YELLOW_WALL_ROAD_SIGN_MINI_15 = registerWallRoadSignBlock("wallroadsign/yellow/mini_15", Blocks.YELLOW_CONCRETE);
    public static final Block YELLOW_WALL_ROAD_SIGN_MINI_16 = registerWallRoadSignBlock("wallroadsign/yellow/mini_16", Blocks.YELLOW_CONCRETE);
    public static final Block YELLOW_WALL_ROAD_SIGN_MINI_17 = registerWallRoadSignBlock("wallroadsign/yellow/mini_17", Blocks.YELLOW_CONCRETE);
    public static final Block YELLOW_WALL_ROAD_SIGN_MINI_18 = registerWallRoadSignBlock("wallroadsign/yellow/mini_18", Blocks.YELLOW_CONCRETE);
    public static final Block YELLOW_WALL_ROAD_SIGN_MINI_19 = registerWallRoadSignBlock("wallroadsign/yellow/mini_19", Blocks.YELLOW_CONCRETE);

    public static final Block WHITE_WALL_ROAD_SIGN_MINI_01 = registerWallRoadSignBlock("wallroadsign/white/mini_01", Blocks.WHITE_CONCRETE);
    public static final Block WHITE_WALL_ROAD_SIGN_MINI_02 = registerWallRoadSignBlock("wallroadsign/white/mini_02", Blocks.WHITE_CONCRETE);
    public static final Block WHITE_WALL_ROAD_SIGN_MINI_03 = registerWallRoadSignBlock("wallroadsign/white/mini_03", Blocks.WHITE_CONCRETE);
    public static final Block WHITE_WALL_ROAD_SIGN_MINI_04 = registerWallRoadSignBlock("wallroadsign/white/mini_04", Blocks.WHITE_CONCRETE);
    public static final Block WHITE_WALL_ROAD_SIGN_MINI_05 = registerWallRoadSignBlock("wallroadsign/white/mini_05", Blocks.WHITE_CONCRETE);
    public static final Block WHITE_WALL_ROAD_SIGN_MINI_06 = registerWallRoadSignBlock("wallroadsign/white/mini_06", Blocks.WHITE_CONCRETE);
    public static final Block WHITE_WALL_ROAD_SIGN_MINI_07 = registerWallRoadSignBlock("wallroadsign/white/mini_07", Blocks.WHITE_CONCRETE);
    public static final Block WHITE_WALL_ROAD_SIGN_MINI_08 = registerWallRoadSignBlock("wallroadsign/white/mini_08", Blocks.WHITE_CONCRETE);
    public static final Block WHITE_WALL_ROAD_SIGN_MINI_09 = registerWallRoadSignBlock("wallroadsign/white/mini_09", Blocks.WHITE_CONCRETE);
    public static final Block WHITE_WALL_ROAD_SIGN_MINI_10 = registerWallRoadSignBlock("wallroadsign/white/mini_10", Blocks.WHITE_CONCRETE);
    public static final Block WHITE_WALL_ROAD_SIGN_MINI_11 = registerWallRoadSignBlock("wallroadsign/white/mini_11", Blocks.WHITE_CONCRETE);
    public static final Block WHITE_WALL_ROAD_SIGN_MINI_12 = registerWallRoadSignBlock("wallroadsign/white/mini_12", Blocks.WHITE_CONCRETE);
    public static final Block WHITE_WALL_ROAD_SIGN_MINI_13 = registerWallRoadSignBlock("wallroadsign/white/mini_13", Blocks.WHITE_CONCRETE);
    public static final Block WHITE_WALL_ROAD_SIGN_MINI_14 = registerWallRoadSignBlock("wallroadsign/white/mini_14", Blocks.WHITE_CONCRETE);
    public static final Block WHITE_WALL_ROAD_SIGN_MINI_15 = registerWallRoadSignBlock("wallroadsign/white/mini_15", Blocks.WHITE_CONCRETE);
    public static final Block WHITE_WALL_ROAD_SIGN_MINI_16 = registerWallRoadSignBlock("wallroadsign/white/mini_16", Blocks.WHITE_CONCRETE);
    public static final Block WHITE_WALL_ROAD_SIGN_MINI_17 = registerWallRoadSignBlock("wallroadsign/white/mini_17", Blocks.WHITE_CONCRETE);
    public static final Block WHITE_WALL_ROAD_SIGN_MINI_18 = registerWallRoadSignBlock("wallroadsign/white/mini_18", Blocks.WHITE_CONCRETE);
    public static final Block WHITE_WALL_ROAD_SIGN_MINI_19 = registerWallRoadSignBlock("wallroadsign/white/mini_19", Blocks.WHITE_CONCRETE);
    public static final Block BROWN_WALL_ROAD_SIGN_MINI_01 = registerWallRoadSignBlock("wallroadsign/brown/mini_01", Blocks.BROWN_CONCRETE);
    public static final Block BROWN_WALL_ROAD_SIGN_MINI_02 = registerWallRoadSignBlock("wallroadsign/brown/mini_02", Blocks.BROWN_CONCRETE);
    public static final Block BROWN_WALL_ROAD_SIGN_MINI_03 = registerWallRoadSignBlock("wallroadsign/brown/mini_03", Blocks.BROWN_CONCRETE);
    public static final Block BROWN_WALL_ROAD_SIGN_MINI_04 = registerWallRoadSignBlock("wallroadsign/brown/mini_04", Blocks.BROWN_CONCRETE);
    public static final Block BROWN_WALL_ROAD_SIGN_MINI_05 = registerWallRoadSignBlock("wallroadsign/brown/mini_05", Blocks.BROWN_CONCRETE);
    public static final Block BROWN_WALL_ROAD_SIGN_MINI_06 = registerWallRoadSignBlock("wallroadsign/brown/mini_06", Blocks.BROWN_CONCRETE);
    public static final Block BROWN_WALL_ROAD_SIGN_MINI_07 = registerWallRoadSignBlock("wallroadsign/brown/mini_07", Blocks.BROWN_CONCRETE);
    public static final Block BROWN_WALL_ROAD_SIGN_MINI_08 = registerWallRoadSignBlock("wallroadsign/brown/mini_08", Blocks.BROWN_CONCRETE);
    public static final Block BROWN_WALL_ROAD_SIGN_MINI_09 = registerWallRoadSignBlock("wallroadsign/brown/mini_09", Blocks.BROWN_CONCRETE);
    public static final Block BROWN_WALL_ROAD_SIGN_MINI_10 = registerWallRoadSignBlock("wallroadsign/brown/mini_10", Blocks.BROWN_CONCRETE);
    public static final Block BROWN_WALL_ROAD_SIGN_MINI_11 = registerWallRoadSignBlock("wallroadsign/brown/mini_11", Blocks.BROWN_CONCRETE);
    public static final Block BROWN_WALL_ROAD_SIGN_MINI_12 = registerWallRoadSignBlock("wallroadsign/brown/mini_12", Blocks.BROWN_CONCRETE);
    public static final Block BROWN_WALL_ROAD_SIGN_MINI_13 = registerWallRoadSignBlock("wallroadsign/brown/mini_13", Blocks.BROWN_CONCRETE);
    public static final Block BROWN_WALL_ROAD_SIGN_MINI_14 = registerWallRoadSignBlock("wallroadsign/brown/mini_14", Blocks.BROWN_CONCRETE);
    public static final Block BROWN_WALL_ROAD_SIGN_MINI_15 = registerWallRoadSignBlock("wallroadsign/brown/mini_15", Blocks.BROWN_CONCRETE);
    public static final Block BROWN_WALL_ROAD_SIGN_MINI_16 = registerWallRoadSignBlock("wallroadsign/brown/mini_16", Blocks.BROWN_CONCRETE);
    public static final Block BROWN_WALL_ROAD_SIGN_MINI_17 = registerWallRoadSignBlock("wallroadsign/brown/mini_17", Blocks.BROWN_CONCRETE);
    public static final Block BROWN_WALL_ROAD_SIGN_MINI_18 = registerWallRoadSignBlock("wallroadsign/brown/mini_18", Blocks.BROWN_CONCRETE);
    public static final Block BROWN_WALL_ROAD_SIGN_MINI_19 = registerWallRoadSignBlock("wallroadsign/brown/mini_19", Blocks.BROWN_CONCRETE);

    public static final Block ORANGE_WALL_ROAD_SIGN_MINI_01 = registerWallRoadSignBlock("wallroadsign/orange/mini_01", Blocks.ORANGE_CONCRETE);
    public static final Block ORANGE_WALL_ROAD_SIGN_MINI_02 = registerWallRoadSignBlock("wallroadsign/orange/mini_02", Blocks.ORANGE_CONCRETE);
    public static final Block ORANGE_WALL_ROAD_SIGN_MINI_03 = registerWallRoadSignBlock("wallroadsign/orange/mini_03", Blocks.ORANGE_CONCRETE);
    public static final Block ORANGE_WALL_ROAD_SIGN_MINI_04 = registerWallRoadSignBlock("wallroadsign/orange/mini_04", Blocks.ORANGE_CONCRETE);
    public static final Block ORANGE_WALL_ROAD_SIGN_MINI_05 = registerWallRoadSignBlock("wallroadsign/orange/mini_05", Blocks.ORANGE_CONCRETE);
    public static final Block ORANGE_WALL_ROAD_SIGN_MINI_06 = registerWallRoadSignBlock("wallroadsign/orange/mini_06", Blocks.ORANGE_CONCRETE);
    public static final Block ORANGE_WALL_ROAD_SIGN_MINI_07 = registerWallRoadSignBlock("wallroadsign/orange/mini_07", Blocks.ORANGE_CONCRETE);
    public static final Block ORANGE_WALL_ROAD_SIGN_MINI_08 = registerWallRoadSignBlock("wallroadsign/orange/mini_08", Blocks.ORANGE_CONCRETE);
    public static final Block ORANGE_WALL_ROAD_SIGN_MINI_09 = registerWallRoadSignBlock("wallroadsign/orange/mini_09", Blocks.ORANGE_CONCRETE);
    public static final Block ORANGE_WALL_ROAD_SIGN_MINI_10 = registerWallRoadSignBlock("wallroadsign/orange/mini_10", Blocks.ORANGE_CONCRETE);
    public static final Block ORANGE_WALL_ROAD_SIGN_MINI_11 = registerWallRoadSignBlock("wallroadsign/orange/mini_11", Blocks.ORANGE_CONCRETE);
    public static final Block ORANGE_WALL_ROAD_SIGN_MINI_12 = registerWallRoadSignBlock("wallroadsign/orange/mini_12", Blocks.ORANGE_CONCRETE);
    public static final Block ORANGE_WALL_ROAD_SIGN_MINI_13 = registerWallRoadSignBlock("wallroadsign/orange/mini_13", Blocks.ORANGE_CONCRETE);
    public static final Block ORANGE_WALL_ROAD_SIGN_MINI_14 = registerWallRoadSignBlock("wallroadsign/orange/mini_14", Blocks.ORANGE_CONCRETE);
    public static final Block ORANGE_WALL_ROAD_SIGN_MINI_15 = registerWallRoadSignBlock("wallroadsign/orange/mini_15", Blocks.ORANGE_CONCRETE);
    public static final Block ORANGE_WALL_ROAD_SIGN_MINI_16 = registerWallRoadSignBlock("wallroadsign/orange/mini_16", Blocks.ORANGE_CONCRETE);
    public static final Block ORANGE_WALL_ROAD_SIGN_MINI_17 = registerWallRoadSignBlock("wallroadsign/orange/mini_17", Blocks.ORANGE_CONCRETE);
    public static final Block ORANGE_WALL_ROAD_SIGN_MINI_18 = registerWallRoadSignBlock("wallroadsign/orange/mini_18", Blocks.ORANGE_CONCRETE);
    public static final Block ORANGE_WALL_ROAD_SIGN_MINI_19 = registerWallRoadSignBlock("wallroadsign/orange/mini_19", Blocks.ORANGE_CONCRETE);

    /**
     * 初始化墙上道路指示牌方块注册。
     */
    public static void registerWallRoadSignBlocks() {
    }
}
