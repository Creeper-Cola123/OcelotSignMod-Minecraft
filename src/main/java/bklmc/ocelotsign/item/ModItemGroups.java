package bklmc.ocelotsign.item;

import bklmc.ocelotsign.OcelotSignMod;
import bklmc.ocelotsign.block.*;
import bklmc.ocelotsign.integration.mishanguc.MishangAccess;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 物品创造栏分组注册
 *
 * @see ModItems
 * @see net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab
 */
public class ModItemGroups {
    private static final Logger LOGGER = LoggerFactory.getLogger(ModItemGroups.class); // 日志记录器

    /**
     * 道路指示牌分类。
     */
    public static final CreativeModeTab ROAD_SIGNS = FabricCreativeModeTab.builder()
            .icon(() -> new ItemStack(ModItems.ROAD_SIGNS_ICON))
            .title(Component.translatable("itemGroup.ocelotsign.road_signs"))
            .displayItems((context, entries) -> {
                addAllBlocks(entries,
                        RoadSignBlocks.BLUE_ROAD_SIGN_LEFT_TOP, RoadSignBlocks.BLUE_ROAD_SIGN_TOP,
                        RoadSignBlocks.BLUE_ROAD_SIGN_RIGHT_TOP, RoadSignBlocks.BLUE_ROAD_SIGN_LEFT,
                        RoadSignBlocks.BLUE_ROAD_SIGN_MIDDLE, RoadSignBlocks.BLUE_ROAD_SIGN_RIGHT,
                        RoadSignBlocks.BLUE_ROAD_SIGN_LEFT_BOTTOM, RoadSignBlocks.BLUE_ROAD_SIGN_BOTTOM,
                        RoadSignBlocks.BLUE_ROAD_SIGN_RIGHT_BOTTOM, RoadSignBlocks.BLUE_ROAD_SIGN_HORIZONTAL_LEFT,
                        RoadSignBlocks.BLUE_ROAD_SIGN_HORIZONTAL_MIDDLE, RoadSignBlocks.BLUE_ROAD_SIGN_HORIZONTAL_RIGHT,
                        RoadSignBlocks.BLUE_ROAD_SIGN_VERTICAL_TOP, RoadSignBlocks.BLUE_ROAD_SIGN_VERTICAL_MIDDLE,
                        RoadSignBlocks.BLUE_ROAD_SIGN_VERTICAL_BOTTOM, RoadSignBlocks.BLUE_ROAD_SIGN_SMALL_A,
                        RoadSignBlocks.BLUE_ROAD_SIGN_SMALL_B,

                        RoadSignBlocks.GREEN_ROAD_SIGN_LEFT_TOP, RoadSignBlocks.GREEN_ROAD_SIGN_TOP,
                        RoadSignBlocks.GREEN_ROAD_SIGN_RIGHT_TOP, RoadSignBlocks.GREEN_ROAD_SIGN_LEFT,
                        RoadSignBlocks.GREEN_ROAD_SIGN_MIDDLE, RoadSignBlocks.GREEN_ROAD_SIGN_RIGHT,
                        RoadSignBlocks.GREEN_ROAD_SIGN_LEFT_BOTTOM, RoadSignBlocks.GREEN_ROAD_SIGN_BOTTOM,
                        RoadSignBlocks.GREEN_ROAD_SIGN_RIGHT_BOTTOM, RoadSignBlocks.GREEN_ROAD_SIGN_HORIZONTAL_LEFT,
                        RoadSignBlocks.GREEN_ROAD_SIGN_HORIZONTAL_MIDDLE, RoadSignBlocks.GREEN_ROAD_SIGN_HORIZONTAL_RIGHT,
                        RoadSignBlocks.GREEN_ROAD_SIGN_VERTICAL_TOP, RoadSignBlocks.GREEN_ROAD_SIGN_VERTICAL_MIDDLE,
                        RoadSignBlocks.GREEN_ROAD_SIGN_VERTICAL_BOTTOM, RoadSignBlocks.GREEN_ROAD_SIGN_SMALL_A,
                        RoadSignBlocks.GREEN_ROAD_SIGN_SMALL_B,

                        RoadSignBlocks.YELLOW_ROAD_SIGN_LEFT_TOP, RoadSignBlocks.YELLOW_ROAD_SIGN_TOP,
                        RoadSignBlocks.YELLOW_ROAD_SIGN_RIGHT_TOP, RoadSignBlocks.YELLOW_ROAD_SIGN_LEFT,
                        RoadSignBlocks.YELLOW_ROAD_SIGN_MIDDLE, RoadSignBlocks.YELLOW_ROAD_SIGN_RIGHT,
                        RoadSignBlocks.YELLOW_ROAD_SIGN_LEFT_BOTTOM, RoadSignBlocks.YELLOW_ROAD_SIGN_BOTTOM,
                        RoadSignBlocks.YELLOW_ROAD_SIGN_RIGHT_BOTTOM, RoadSignBlocks.YELLOW_ROAD_SIGN_HORIZONTAL_LEFT,
                        RoadSignBlocks.YELLOW_ROAD_SIGN_HORIZONTAL_MIDDLE, RoadSignBlocks.YELLOW_ROAD_SIGN_HORIZONTAL_RIGHT,
                        RoadSignBlocks.YELLOW_ROAD_SIGN_VERTICAL_TOP, RoadSignBlocks.YELLOW_ROAD_SIGN_VERTICAL_MIDDLE,
                        RoadSignBlocks.YELLOW_ROAD_SIGN_VERTICAL_BOTTOM, RoadSignBlocks.YELLOW_ROAD_SIGN_SMALL_A,
                        RoadSignBlocks.YELLOW_ROAD_SIGN_SMALL_B,

                        RoadSignBlocks.WHITE_ROAD_SIGN_LEFT_TOP, RoadSignBlocks.WHITE_ROAD_SIGN_TOP,
                        RoadSignBlocks.WHITE_ROAD_SIGN_RIGHT_TOP, RoadSignBlocks.WHITE_ROAD_SIGN_LEFT,
                        RoadSignBlocks.WHITE_ROAD_SIGN_MIDDLE, RoadSignBlocks.WHITE_ROAD_SIGN_RIGHT,
                        RoadSignBlocks.WHITE_ROAD_SIGN_LEFT_BOTTOM, RoadSignBlocks.WHITE_ROAD_SIGN_BOTTOM,
                        RoadSignBlocks.WHITE_ROAD_SIGN_RIGHT_BOTTOM, RoadSignBlocks.WHITE_ROAD_SIGN_HORIZONTAL_LEFT,
                        RoadSignBlocks.WHITE_ROAD_SIGN_HORIZONTAL_MIDDLE, RoadSignBlocks.WHITE_ROAD_SIGN_HORIZONTAL_RIGHT,
                        RoadSignBlocks.WHITE_ROAD_SIGN_VERTICAL_TOP, RoadSignBlocks.WHITE_ROAD_SIGN_VERTICAL_MIDDLE,
                        RoadSignBlocks.WHITE_ROAD_SIGN_VERTICAL_BOTTOM, RoadSignBlocks.WHITE_ROAD_SIGN_SMALL_A,
                        RoadSignBlocks.WHITE_ROAD_SIGN_SMALL_B,

                        RoadSignBlocks.BROWN_ROAD_SIGN_LEFT_TOP, RoadSignBlocks.BROWN_ROAD_SIGN_TOP,
                        RoadSignBlocks.BROWN_ROAD_SIGN_RIGHT_TOP, RoadSignBlocks.BROWN_ROAD_SIGN_LEFT,
                        RoadSignBlocks.BROWN_ROAD_SIGN_MIDDLE, RoadSignBlocks.BROWN_ROAD_SIGN_RIGHT,
                        RoadSignBlocks.BROWN_ROAD_SIGN_LEFT_BOTTOM, RoadSignBlocks.BROWN_ROAD_SIGN_BOTTOM,
                        RoadSignBlocks.BROWN_ROAD_SIGN_RIGHT_BOTTOM, RoadSignBlocks.BROWN_ROAD_SIGN_HORIZONTAL_LEFT,
                        RoadSignBlocks.BROWN_ROAD_SIGN_HORIZONTAL_MIDDLE, RoadSignBlocks.BROWN_ROAD_SIGN_HORIZONTAL_RIGHT,
                        RoadSignBlocks.BROWN_ROAD_SIGN_VERTICAL_TOP, RoadSignBlocks.BROWN_ROAD_SIGN_VERTICAL_MIDDLE,
                        RoadSignBlocks.BROWN_ROAD_SIGN_VERTICAL_BOTTOM, RoadSignBlocks.BROWN_ROAD_SIGN_SMALL_A,
                        RoadSignBlocks.BROWN_ROAD_SIGN_SMALL_B,

                        RoadSignBlocks.ORANGE_ROAD_SIGN_LEFT_TOP, RoadSignBlocks.ORANGE_ROAD_SIGN_TOP,
                        RoadSignBlocks.ORANGE_ROAD_SIGN_RIGHT_TOP, RoadSignBlocks.ORANGE_ROAD_SIGN_LEFT,
                        RoadSignBlocks.ORANGE_ROAD_SIGN_MIDDLE, RoadSignBlocks.ORANGE_ROAD_SIGN_RIGHT,
                        RoadSignBlocks.ORANGE_ROAD_SIGN_LEFT_BOTTOM, RoadSignBlocks.ORANGE_ROAD_SIGN_BOTTOM,
                        RoadSignBlocks.ORANGE_ROAD_SIGN_RIGHT_BOTTOM, RoadSignBlocks.ORANGE_ROAD_SIGN_HORIZONTAL_LEFT,
                        RoadSignBlocks.ORANGE_ROAD_SIGN_HORIZONTAL_MIDDLE, RoadSignBlocks.ORANGE_ROAD_SIGN_HORIZONTAL_RIGHT,
                        RoadSignBlocks.ORANGE_ROAD_SIGN_VERTICAL_TOP, RoadSignBlocks.ORANGE_ROAD_SIGN_VERTICAL_MIDDLE,
                        RoadSignBlocks.ORANGE_ROAD_SIGN_VERTICAL_BOTTOM, RoadSignBlocks.ORANGE_ROAD_SIGN_SMALL_A,
                        RoadSignBlocks.ORANGE_ROAD_SIGN_SMALL_B
                );
            })
            .build();

    /**
     * 墙道路指示牌分类。
     */
    public static final CreativeModeTab WALL_ROAD_SIGNS = FabricCreativeModeTab.builder()
            .icon(() -> new ItemStack(ModItems.WALL_ROAD_SIGNS_ICON))
            .title(Component.translatable("itemGroup.ocelotsign.wall_road_signs"))
            .displayItems((context, entries) -> {
                addAllBlocks(entries,
                        WallRoadSignBlocks.BLUE_WALL_ROAD_SIGN_LEFT_TOP, WallRoadSignBlocks.BLUE_WALL_ROAD_SIGN_TOP,
                        WallRoadSignBlocks.BLUE_WALL_ROAD_SIGN_RIGHT_TOP, WallRoadSignBlocks.BLUE_WALL_ROAD_SIGN_LEFT,
                        WallRoadSignBlocks.BLUE_WALL_ROAD_SIGN_MIDDLE, WallRoadSignBlocks.BLUE_WALL_ROAD_SIGN_RIGHT,
                        WallRoadSignBlocks.BLUE_WALL_ROAD_SIGN_LEFT_BOTTOM, WallRoadSignBlocks.BLUE_WALL_ROAD_SIGN_BOTTOM,
                        WallRoadSignBlocks.BLUE_WALL_ROAD_SIGN_RIGHT_BOTTOM, WallRoadSignBlocks.BLUE_WALL_ROAD_SIGN_HORIZONTAL_LEFT,
                        WallRoadSignBlocks.BLUE_WALL_ROAD_SIGN_HORIZONTAL_MIDDLE, WallRoadSignBlocks.BLUE_WALL_ROAD_SIGN_HORIZONTAL_RIGHT,
                        WallRoadSignBlocks.BLUE_WALL_ROAD_SIGN_VERTICAL_TOP, WallRoadSignBlocks.BLUE_WALL_ROAD_SIGN_VERTICAL_MIDDLE,
                        WallRoadSignBlocks.BLUE_WALL_ROAD_SIGN_VERTICAL_BOTTOM, WallRoadSignBlocks.BLUE_WALL_ROAD_SIGN_SMALL_A,
                        WallRoadSignBlocks.BLUE_WALL_ROAD_SIGN_SMALL_B, WallRoadSignBlocks.BLUE_WALL_ROAD_SIGN_RHOMBUS,

                        WallRoadSignBlocks.GREEN_WALL_ROAD_SIGN_LEFT_TOP, WallRoadSignBlocks.GREEN_WALL_ROAD_SIGN_TOP,
                        WallRoadSignBlocks.GREEN_WALL_ROAD_SIGN_RIGHT_TOP, WallRoadSignBlocks.GREEN_WALL_ROAD_SIGN_LEFT,
                        WallRoadSignBlocks.GREEN_WALL_ROAD_SIGN_MIDDLE, WallRoadSignBlocks.GREEN_WALL_ROAD_SIGN_RIGHT,
                        WallRoadSignBlocks.GREEN_WALL_ROAD_SIGN_LEFT_BOTTOM, WallRoadSignBlocks.GREEN_WALL_ROAD_SIGN_BOTTOM,
                        WallRoadSignBlocks.GREEN_WALL_ROAD_SIGN_RIGHT_BOTTOM, WallRoadSignBlocks.GREEN_WALL_ROAD_SIGN_HORIZONTAL_LEFT,
                        WallRoadSignBlocks.GREEN_WALL_ROAD_SIGN_HORIZONTAL_MIDDLE, WallRoadSignBlocks.GREEN_WALL_ROAD_SIGN_HORIZONTAL_RIGHT,
                        WallRoadSignBlocks.GREEN_WALL_ROAD_SIGN_VERTICAL_TOP, WallRoadSignBlocks.GREEN_WALL_ROAD_SIGN_VERTICAL_MIDDLE,
                        WallRoadSignBlocks.GREEN_WALL_ROAD_SIGN_VERTICAL_BOTTOM, WallRoadSignBlocks.GREEN_WALL_ROAD_SIGN_SMALL_A,
                        WallRoadSignBlocks.GREEN_WALL_ROAD_SIGN_SMALL_B, WallRoadSignBlocks.GREEN_WALL_ROAD_SIGN_RHOMBUS,

                        WallRoadSignBlocks.YELLOW_WALL_ROAD_SIGN_LEFT_TOP, WallRoadSignBlocks.YELLOW_WALL_ROAD_SIGN_TOP,
                        WallRoadSignBlocks.YELLOW_WALL_ROAD_SIGN_RIGHT_TOP, WallRoadSignBlocks.YELLOW_WALL_ROAD_SIGN_LEFT,
                        WallRoadSignBlocks.YELLOW_WALL_ROAD_SIGN_MIDDLE, WallRoadSignBlocks.YELLOW_WALL_ROAD_SIGN_RIGHT,
                        WallRoadSignBlocks.YELLOW_WALL_ROAD_SIGN_LEFT_BOTTOM, WallRoadSignBlocks.YELLOW_WALL_ROAD_SIGN_BOTTOM,
                        WallRoadSignBlocks.YELLOW_WALL_ROAD_SIGN_RIGHT_BOTTOM, WallRoadSignBlocks.YELLOW_WALL_ROAD_SIGN_HORIZONTAL_LEFT,
                        WallRoadSignBlocks.YELLOW_WALL_ROAD_SIGN_HORIZONTAL_MIDDLE, WallRoadSignBlocks.YELLOW_WALL_ROAD_SIGN_HORIZONTAL_RIGHT,
                        WallRoadSignBlocks.YELLOW_WALL_ROAD_SIGN_VERTICAL_TOP, WallRoadSignBlocks.YELLOW_WALL_ROAD_SIGN_VERTICAL_MIDDLE,
                        WallRoadSignBlocks.YELLOW_WALL_ROAD_SIGN_VERTICAL_BOTTOM, WallRoadSignBlocks.YELLOW_WALL_ROAD_SIGN_SMALL_A,
                        WallRoadSignBlocks.YELLOW_WALL_ROAD_SIGN_SMALL_B, WallRoadSignBlocks.YELLOW_WALL_ROAD_SIGN_RHOMBUS,

                        WallRoadSignBlocks.WHITE_WALL_ROAD_SIGN_LEFT_TOP, WallRoadSignBlocks.WHITE_WALL_ROAD_SIGN_TOP,
                        WallRoadSignBlocks.WHITE_WALL_ROAD_SIGN_RIGHT_TOP, WallRoadSignBlocks.WHITE_WALL_ROAD_SIGN_LEFT,
                        WallRoadSignBlocks.WHITE_WALL_ROAD_SIGN_MIDDLE, WallRoadSignBlocks.WHITE_WALL_ROAD_SIGN_RIGHT,
                        WallRoadSignBlocks.WHITE_WALL_ROAD_SIGN_LEFT_BOTTOM, WallRoadSignBlocks.WHITE_WALL_ROAD_SIGN_BOTTOM,
                        WallRoadSignBlocks.WHITE_WALL_ROAD_SIGN_RIGHT_BOTTOM, WallRoadSignBlocks.WHITE_WALL_ROAD_SIGN_HORIZONTAL_LEFT,
                        WallRoadSignBlocks.WHITE_WALL_ROAD_SIGN_HORIZONTAL_MIDDLE, WallRoadSignBlocks.WHITE_WALL_ROAD_SIGN_HORIZONTAL_RIGHT,
                        WallRoadSignBlocks.WHITE_WALL_ROAD_SIGN_VERTICAL_TOP, WallRoadSignBlocks.WHITE_WALL_ROAD_SIGN_VERTICAL_MIDDLE,
                        WallRoadSignBlocks.WHITE_WALL_ROAD_SIGN_VERTICAL_BOTTOM, WallRoadSignBlocks.WHITE_WALL_ROAD_SIGN_SMALL_A,
                        WallRoadSignBlocks.WHITE_WALL_ROAD_SIGN_SMALL_B, WallRoadSignBlocks.WHITE_WALL_ROAD_SIGN_RHOMBUS,

                        WallRoadSignBlocks.BROWN_WALL_ROAD_SIGN_LEFT_TOP, WallRoadSignBlocks.BROWN_WALL_ROAD_SIGN_TOP,
                        WallRoadSignBlocks.BROWN_WALL_ROAD_SIGN_RIGHT_TOP, WallRoadSignBlocks.BROWN_WALL_ROAD_SIGN_LEFT,
                        WallRoadSignBlocks.BROWN_WALL_ROAD_SIGN_MIDDLE, WallRoadSignBlocks.BROWN_WALL_ROAD_SIGN_RIGHT,
                        WallRoadSignBlocks.BROWN_WALL_ROAD_SIGN_LEFT_BOTTOM, WallRoadSignBlocks.BROWN_WALL_ROAD_SIGN_BOTTOM,
                        WallRoadSignBlocks.BROWN_WALL_ROAD_SIGN_RIGHT_BOTTOM, WallRoadSignBlocks.BROWN_WALL_ROAD_SIGN_HORIZONTAL_LEFT,
                        WallRoadSignBlocks.BROWN_WALL_ROAD_SIGN_HORIZONTAL_MIDDLE, WallRoadSignBlocks.BROWN_WALL_ROAD_SIGN_HORIZONTAL_RIGHT,
                        WallRoadSignBlocks.BROWN_WALL_ROAD_SIGN_VERTICAL_TOP, WallRoadSignBlocks.BROWN_WALL_ROAD_SIGN_VERTICAL_MIDDLE,
                        WallRoadSignBlocks.BROWN_WALL_ROAD_SIGN_VERTICAL_BOTTOM, WallRoadSignBlocks.BROWN_WALL_ROAD_SIGN_SMALL_A,
                        WallRoadSignBlocks.BROWN_WALL_ROAD_SIGN_SMALL_B, WallRoadSignBlocks.BROWN_WALL_ROAD_SIGN_RHOMBUS,

                        WallRoadSignBlocks.ORANGE_WALL_ROAD_SIGN_LEFT_TOP, WallRoadSignBlocks.ORANGE_WALL_ROAD_SIGN_TOP,
                        WallRoadSignBlocks.ORANGE_WALL_ROAD_SIGN_RIGHT_TOP, WallRoadSignBlocks.ORANGE_WALL_ROAD_SIGN_LEFT,
                        WallRoadSignBlocks.ORANGE_WALL_ROAD_SIGN_MIDDLE, WallRoadSignBlocks.ORANGE_WALL_ROAD_SIGN_RIGHT,
                        WallRoadSignBlocks.ORANGE_WALL_ROAD_SIGN_LEFT_BOTTOM, WallRoadSignBlocks.ORANGE_WALL_ROAD_SIGN_BOTTOM,
                        WallRoadSignBlocks.ORANGE_WALL_ROAD_SIGN_RIGHT_BOTTOM, WallRoadSignBlocks.ORANGE_WALL_ROAD_SIGN_HORIZONTAL_LEFT,
                        WallRoadSignBlocks.ORANGE_WALL_ROAD_SIGN_HORIZONTAL_MIDDLE, WallRoadSignBlocks.ORANGE_WALL_ROAD_SIGN_HORIZONTAL_RIGHT,
                        WallRoadSignBlocks.ORANGE_WALL_ROAD_SIGN_VERTICAL_TOP, WallRoadSignBlocks.ORANGE_WALL_ROAD_SIGN_VERTICAL_MIDDLE,
                        WallRoadSignBlocks.ORANGE_WALL_ROAD_SIGN_VERTICAL_BOTTOM, WallRoadSignBlocks.ORANGE_WALL_ROAD_SIGN_SMALL_A,
                        WallRoadSignBlocks.ORANGE_WALL_ROAD_SIGN_SMALL_B,

                        WallRoadSignBlocks.NO_PARKING, WallRoadSignBlocks.NO_LONG_TERM_PARKING,
                        WallRoadSignBlocks.FORBID_2, WallRoadSignBlocks.BLUE_ROUND, WallRoadSignBlocks.RED_ROUND,
                        WallRoadSignBlocks.GREEN_ROUND, WallRoadSignBlocks.YELLOW_ROUND, WallRoadSignBlocks.WHITE_ROUND,
                        WallRoadSignBlocks.FORBID_1, WallRoadSignBlocks.NO_ENTRY, WallRoadSignBlocks.HEIGHT_LIMIT,
                        WallRoadSignBlocks.WIDTH_LIMIT, WallRoadSignBlocks.WEIGHT_LIMIT, WallRoadSignBlocks.LIFT_SPEED_LIMIT,
                        WallRoadSignBlocks.LOW_SPEED_LIMIT, WallRoadSignBlocks.MAX_SPEED_LIMIT, WallRoadSignBlocks.WARNING,
                        WallRoadSignBlocks.WARNING_RED_STROKE, WallRoadSignBlocks.STOP, WallRoadSignBlocks.NATIONAL_EXPRESSWAY,
                        WallRoadSignBlocks.PROVINCIAL_EXPRESSWAY, WallRoadSignBlocks.NATIONAL_HIGHWAY,
                        WallRoadSignBlocks.PROVINCIAL_HIGHWAY, WallRoadSignBlocks.COUNTRY_HIGHWAY, WallRoadSignBlocks.YIELD,
                        WallRoadSignBlocks.GREEN_MILEAGE_SIGN, WallRoadSignBlocks.BLUE_MILEAGE_SIGN,
                        WallRoadSignBlocks.GREEN_HECTOMETER_SIGN, WallRoadSignBlocks.BLUE_HECTOMETER_SIGN,
                        WallRoadSignBlocks.YIELD_RED_FILL, WallRoadSignBlocks.FORBID_3, WallRoadSignBlocks.FORBID_4,
                        WallRoadSignBlocks.MAX_SPEED_LIMIT_YELLOW_FILL, WallRoadSignBlocks.WARNING_RED_STROKE_YELLOW_FILL,
                        WallRoadSignBlocks.LIFT_SPEED_LIMIT_YELLOW_FILL, WallRoadSignBlocks.WARNING_BLUE_FILL,
                        WallRoadSignBlocks.WARNING_YELLOW_FILL, WallRoadSignBlocks.WARNING_GREEN_FILL,
                        WallRoadSignBlocks.WARNING_2,

                        WallRoadSignBlocks.BLUE_CHEVRON_ALIGNMENT_A_LEFT, WallRoadSignBlocks.BLUE_CHEVRON_ALIGNMENT_A_RIGHT,
                        WallRoadSignBlocks.BLUE_CHEVRON_ALIGNMENT_B_LEFT, WallRoadSignBlocks.BLUE_CHEVRON_ALIGNMENT_B_RIGHT,
                        WallRoadSignBlocks.BLUE_CHEVRON_ALIGNMENT_C_LEFT, WallRoadSignBlocks.BLUE_CHEVRON_ALIGNMENT_C_RIGHT,
                        WallRoadSignBlocks.BLUE_CHEVRON_ALIGNMENT_C_UP,

                        WallRoadSignBlocks.GREEN_CHEVRON_ALIGNMENT_A_LEFT, WallRoadSignBlocks.GREEN_CHEVRON_ALIGNMENT_A_RIGHT,
                        WallRoadSignBlocks.GREEN_CHEVRON_ALIGNMENT_B_LEFT, WallRoadSignBlocks.GREEN_CHEVRON_ALIGNMENT_B_RIGHT,
                        WallRoadSignBlocks.GREEN_CHEVRON_ALIGNMENT_C_LEFT, WallRoadSignBlocks.GREEN_CHEVRON_ALIGNMENT_C_RIGHT,
                        WallRoadSignBlocks.GREEN_CHEVRON_ALIGNMENT_C_UP,

                        WallRoadSignBlocks.RED_CHEVRON_ALIGNMENT_A_LEFT, WallRoadSignBlocks.RED_CHEVRON_ALIGNMENT_A_RIGHT,
                        WallRoadSignBlocks.RED_CHEVRON_ALIGNMENT_B_LEFT, WallRoadSignBlocks.RED_CHEVRON_ALIGNMENT_B_RIGHT,
                        WallRoadSignBlocks.RED_CHEVRON_ALIGNMENT_C_LEFT, WallRoadSignBlocks.RED_CHEVRON_ALIGNMENT_C_RIGHT,
                        WallRoadSignBlocks.RED_CHEVRON_ALIGNMENT_C_UP,

                        WallRoadSignBlocks.YELLOW_CHEVRON_ALIGNMENT_A_LEFT, WallRoadSignBlocks.YELLOW_CHEVRON_ALIGNMENT_A_RIGHT,
                        WallRoadSignBlocks.YELLOW_CHEVRON_ALIGNMENT_B_LEFT, WallRoadSignBlocks.YELLOW_CHEVRON_ALIGNMENT_B_RIGHT,
                        WallRoadSignBlocks.YELLOW_CHEVRON_ALIGNMENT_C_LEFT, WallRoadSignBlocks.YELLOW_CHEVRON_ALIGNMENT_C_RIGHT,
                        WallRoadSignBlocks.YELLOW_CHEVRON_ALIGNMENT_C_UP,

                        WallRoadSignBlocks.BLUE_WALL_ROAD_SIGN_MINI_01,
                        WallRoadSignBlocks.BLUE_WALL_ROAD_SIGN_MINI_02,
                        WallRoadSignBlocks.BLUE_WALL_ROAD_SIGN_MINI_03,
                        WallRoadSignBlocks.BLUE_WALL_ROAD_SIGN_MINI_04,
                        WallRoadSignBlocks.BLUE_WALL_ROAD_SIGN_MINI_05,
                        WallRoadSignBlocks.BLUE_WALL_ROAD_SIGN_MINI_06,
                        WallRoadSignBlocks.BLUE_WALL_ROAD_SIGN_MINI_07,
                        WallRoadSignBlocks.BLUE_WALL_ROAD_SIGN_MINI_08,
                        WallRoadSignBlocks.BLUE_WALL_ROAD_SIGN_MINI_09,
                        WallRoadSignBlocks.BLUE_WALL_ROAD_SIGN_MINI_10,
                        WallRoadSignBlocks.BLUE_WALL_ROAD_SIGN_MINI_11,
                        WallRoadSignBlocks.BLUE_WALL_ROAD_SIGN_MINI_12,
                        WallRoadSignBlocks.BLUE_WALL_ROAD_SIGN_MINI_13,
                        WallRoadSignBlocks.BLUE_WALL_ROAD_SIGN_MINI_14,
                        WallRoadSignBlocks.BLUE_WALL_ROAD_SIGN_MINI_15,
                        WallRoadSignBlocks.BLUE_WALL_ROAD_SIGN_MINI_16,
                        WallRoadSignBlocks.BLUE_WALL_ROAD_SIGN_MINI_17,
                        WallRoadSignBlocks.BLUE_WALL_ROAD_SIGN_MINI_18,
                        WallRoadSignBlocks.BLUE_WALL_ROAD_SIGN_MINI_19,
                        WallRoadSignBlocks.GREEN_WALL_ROAD_SIGN_MINI_01,
                        WallRoadSignBlocks.GREEN_WALL_ROAD_SIGN_MINI_02,
                        WallRoadSignBlocks.GREEN_WALL_ROAD_SIGN_MINI_03,
                        WallRoadSignBlocks.GREEN_WALL_ROAD_SIGN_MINI_04,
                        WallRoadSignBlocks.GREEN_WALL_ROAD_SIGN_MINI_05,
                        WallRoadSignBlocks.GREEN_WALL_ROAD_SIGN_MINI_06,
                        WallRoadSignBlocks.GREEN_WALL_ROAD_SIGN_MINI_07,
                        WallRoadSignBlocks.GREEN_WALL_ROAD_SIGN_MINI_08,
                        WallRoadSignBlocks.GREEN_WALL_ROAD_SIGN_MINI_09,
                        WallRoadSignBlocks.GREEN_WALL_ROAD_SIGN_MINI_10,
                        WallRoadSignBlocks.GREEN_WALL_ROAD_SIGN_MINI_11,
                        WallRoadSignBlocks.GREEN_WALL_ROAD_SIGN_MINI_12,
                        WallRoadSignBlocks.GREEN_WALL_ROAD_SIGN_MINI_13,
                        WallRoadSignBlocks.GREEN_WALL_ROAD_SIGN_MINI_14,
                        WallRoadSignBlocks.GREEN_WALL_ROAD_SIGN_MINI_15,
                        WallRoadSignBlocks.GREEN_WALL_ROAD_SIGN_MINI_16,
                        WallRoadSignBlocks.GREEN_WALL_ROAD_SIGN_MINI_17,
                        WallRoadSignBlocks.GREEN_WALL_ROAD_SIGN_MINI_18,
                        WallRoadSignBlocks.GREEN_WALL_ROAD_SIGN_MINI_19,
                        WallRoadSignBlocks.YELLOW_WALL_ROAD_SIGN_MINI_01,
                        WallRoadSignBlocks.YELLOW_WALL_ROAD_SIGN_MINI_02,
                        WallRoadSignBlocks.YELLOW_WALL_ROAD_SIGN_MINI_03,
                        WallRoadSignBlocks.YELLOW_WALL_ROAD_SIGN_MINI_04,
                        WallRoadSignBlocks.YELLOW_WALL_ROAD_SIGN_MINI_05,
                        WallRoadSignBlocks.YELLOW_WALL_ROAD_SIGN_MINI_06,
                        WallRoadSignBlocks.YELLOW_WALL_ROAD_SIGN_MINI_07,
                        WallRoadSignBlocks.YELLOW_WALL_ROAD_SIGN_MINI_08,
                        WallRoadSignBlocks.YELLOW_WALL_ROAD_SIGN_MINI_09,
                        WallRoadSignBlocks.YELLOW_WALL_ROAD_SIGN_MINI_10,
                        WallRoadSignBlocks.YELLOW_WALL_ROAD_SIGN_MINI_11,
                        WallRoadSignBlocks.YELLOW_WALL_ROAD_SIGN_MINI_12,
                        WallRoadSignBlocks.YELLOW_WALL_ROAD_SIGN_MINI_13,
                        WallRoadSignBlocks.YELLOW_WALL_ROAD_SIGN_MINI_14,
                        WallRoadSignBlocks.YELLOW_WALL_ROAD_SIGN_MINI_15,
                        WallRoadSignBlocks.YELLOW_WALL_ROAD_SIGN_MINI_16,
                        WallRoadSignBlocks.YELLOW_WALL_ROAD_SIGN_MINI_17,
                        WallRoadSignBlocks.YELLOW_WALL_ROAD_SIGN_MINI_18,
                        WallRoadSignBlocks.YELLOW_WALL_ROAD_SIGN_MINI_19,
                        WallRoadSignBlocks.WHITE_WALL_ROAD_SIGN_MINI_01,
                        WallRoadSignBlocks.WHITE_WALL_ROAD_SIGN_MINI_02,
                        WallRoadSignBlocks.WHITE_WALL_ROAD_SIGN_MINI_03,
                        WallRoadSignBlocks.WHITE_WALL_ROAD_SIGN_MINI_04,
                        WallRoadSignBlocks.WHITE_WALL_ROAD_SIGN_MINI_05,
                        WallRoadSignBlocks.WHITE_WALL_ROAD_SIGN_MINI_06,
                        WallRoadSignBlocks.WHITE_WALL_ROAD_SIGN_MINI_07,
                        WallRoadSignBlocks.WHITE_WALL_ROAD_SIGN_MINI_08,
                        WallRoadSignBlocks.WHITE_WALL_ROAD_SIGN_MINI_09,
                        WallRoadSignBlocks.WHITE_WALL_ROAD_SIGN_MINI_10,
                        WallRoadSignBlocks.WHITE_WALL_ROAD_SIGN_MINI_11,
                        WallRoadSignBlocks.WHITE_WALL_ROAD_SIGN_MINI_12,
                        WallRoadSignBlocks.WHITE_WALL_ROAD_SIGN_MINI_13,
                        WallRoadSignBlocks.WHITE_WALL_ROAD_SIGN_MINI_14,
                        WallRoadSignBlocks.WHITE_WALL_ROAD_SIGN_MINI_15,
                        WallRoadSignBlocks.WHITE_WALL_ROAD_SIGN_MINI_16,
                        WallRoadSignBlocks.WHITE_WALL_ROAD_SIGN_MINI_17,
                        WallRoadSignBlocks.WHITE_WALL_ROAD_SIGN_MINI_18,
                        WallRoadSignBlocks.WHITE_WALL_ROAD_SIGN_MINI_19,
                        WallRoadSignBlocks.BROWN_WALL_ROAD_SIGN_MINI_01,
                        WallRoadSignBlocks.BROWN_WALL_ROAD_SIGN_MINI_02,
                        WallRoadSignBlocks.BROWN_WALL_ROAD_SIGN_MINI_03,
                        WallRoadSignBlocks.BROWN_WALL_ROAD_SIGN_MINI_04,
                        WallRoadSignBlocks.BROWN_WALL_ROAD_SIGN_MINI_05,
                        WallRoadSignBlocks.BROWN_WALL_ROAD_SIGN_MINI_06,
                        WallRoadSignBlocks.BROWN_WALL_ROAD_SIGN_MINI_07,
                        WallRoadSignBlocks.BROWN_WALL_ROAD_SIGN_MINI_08,
                        WallRoadSignBlocks.BROWN_WALL_ROAD_SIGN_MINI_09,
                        WallRoadSignBlocks.BROWN_WALL_ROAD_SIGN_MINI_10,
                        WallRoadSignBlocks.BROWN_WALL_ROAD_SIGN_MINI_11,
                        WallRoadSignBlocks.BROWN_WALL_ROAD_SIGN_MINI_12,
                        WallRoadSignBlocks.BROWN_WALL_ROAD_SIGN_MINI_13,
                        WallRoadSignBlocks.BROWN_WALL_ROAD_SIGN_MINI_14,
                        WallRoadSignBlocks.BROWN_WALL_ROAD_SIGN_MINI_15,
                        WallRoadSignBlocks.BROWN_WALL_ROAD_SIGN_MINI_16,
                        WallRoadSignBlocks.BROWN_WALL_ROAD_SIGN_MINI_17,
                        WallRoadSignBlocks.BROWN_WALL_ROAD_SIGN_MINI_18,
                        WallRoadSignBlocks.BROWN_WALL_ROAD_SIGN_MINI_19,
                        WallRoadSignBlocks.ORANGE_WALL_ROAD_SIGN_MINI_01,
                        WallRoadSignBlocks.ORANGE_WALL_ROAD_SIGN_MINI_02,
                        WallRoadSignBlocks.ORANGE_WALL_ROAD_SIGN_MINI_03,
                        WallRoadSignBlocks.ORANGE_WALL_ROAD_SIGN_MINI_04,
                        WallRoadSignBlocks.ORANGE_WALL_ROAD_SIGN_MINI_05,
                        WallRoadSignBlocks.ORANGE_WALL_ROAD_SIGN_MINI_06,
                        WallRoadSignBlocks.ORANGE_WALL_ROAD_SIGN_MINI_07,
                        WallRoadSignBlocks.ORANGE_WALL_ROAD_SIGN_MINI_08,
                        WallRoadSignBlocks.ORANGE_WALL_ROAD_SIGN_MINI_09,
                        WallRoadSignBlocks.ORANGE_WALL_ROAD_SIGN_MINI_10,
                        WallRoadSignBlocks.ORANGE_WALL_ROAD_SIGN_MINI_11,
                        WallRoadSignBlocks.ORANGE_WALL_ROAD_SIGN_MINI_12,
                        WallRoadSignBlocks.ORANGE_WALL_ROAD_SIGN_MINI_13,
                        WallRoadSignBlocks.ORANGE_WALL_ROAD_SIGN_MINI_14,
                        WallRoadSignBlocks.ORANGE_WALL_ROAD_SIGN_MINI_15,
                        WallRoadSignBlocks.ORANGE_WALL_ROAD_SIGN_MINI_16,
                        WallRoadSignBlocks.ORANGE_WALL_ROAD_SIGN_MINI_17,
                        WallRoadSignBlocks.ORANGE_WALL_ROAD_SIGN_MINI_18,
                        WallRoadSignBlocks.ORANGE_WALL_ROAD_SIGN_MINI_19,
                        WallRoadSignBlocks.OVERHEAD_ROAD_EDGE_MARKER
                );
            })
            .build();

    /**
     * 支柱分类。
     */
    public static final CreativeModeTab PILLARS = FabricCreativeModeTab.builder()
            .icon(() -> new ItemStack(ModItems.PILLARS_ICON))
            .title(Component.translatable("itemGroup.ocelotsign.pillars"))
            .displayItems((context, entries) -> {
                addAllBlocks(entries,
                        PillarBlocks.ROAD_PILLAR_FOUR_SIDES_A, PillarBlocks.ROAD_PILLAR_FOUR_SIDES_A_TOP,
                        PillarBlocks.ROAD_PILLAR_THREE_SIDES_A_LEFT, PillarBlocks.ROAD_PILLAR_THREE_SIDES_A_RIGHT,
                        PillarBlocks.ROAD_PILLAR_THREE_SIDES_A_TOP_LEFT, PillarBlocks.ROAD_PILLAR_THREE_SIDES_A_TOP_RIGHT,
                        PillarBlocks.ROAD_PILLAR_HORIZONTAL_A, PillarBlocks.ROAD_PILLAR_UPRIGHT_A,
                        PillarBlocks.ROAD_PILLAR_UPRIGHT_B_DOUBLE, PillarBlocks.ROAD_PILLAR_UPRIGHT_B_LEFT,
                        PillarBlocks.ROAD_PILLAR_UPRIGHT_B_RIGHT,
                        PillarBlocks.ROAD_PILLAR_UPRIGHT_C, PillarBlocks.ROAD_PILLAR_UPRIGHT_C_HALF,
                        PillarBlocks.ROAD_PILLAR_UPRIGHT_C_INCLINED_LEFT, PillarBlocks.ROAD_PILLAR_UPRIGHT_C_INCLINED_RIGHT,
                        PillarBlocks.ROAD_PILLAR_FOUR_SIDES_D, PillarBlocks.ROAD_PILLAR_FOUR_SIDES_D_LEFT,
                        PillarBlocks.ROAD_PILLAR_FOUR_SIDES_D_RIGHT, PillarBlocks.ROAD_PILLAR_HORIZONTAL_D,
                        PillarBlocks.ROAD_PILLAR_HORIZONTAL_D_DOWN, PillarBlocks.ROAD_PILLAR_HORIZONTAL_D_UP
                );

                entries.accept(OcelotSignMod.CUSTOM_MODEL_BLOCK);
                entries.accept(OcelotSignMod.MODEL_WAND);

                addMishangucTextCopyTool(entries);
            })
            .build();

    /**
     * 向分类中添加 mishanguc 的文本复制工具（如果可用）。
     *
     * @param entries 物品分类条目
     */
    private static void addMishangucTextCopyTool(CreativeModeTab.Output entries) {
        if (!MishangAccess.isAvailable()) {
            LOGGER.debug("Mishanguc 不可用；跳过 TEXT_COPY_TOOL");
            return;
        }

        Object tool = MishangAccess.getTextCopyTool();
        if (tool instanceof net.minecraft.world.item.Item item) {
            entries.accept(item);
        }
    }

    /**
     * 批量向分类中添加方块物品。
     *
     * @param entries 物品分类条目
     * @param blocks  要添加的方块数组
     */
    @SafeVarargs
    private static void addAllBlocks(CreativeModeTab.Output entries, net.minecraft.world.level.block.Block... blocks) {
        for (net.minecraft.world.level.block.Block block : blocks) {
            entries.accept(block.asItem());
        }
    }

    /**
     * 注册三个物品分类。
     *
     * <p>三个分类连续注册，ID 前缀统一，防止被其他模组拆散。
     * ArrowBlocks 另行注册到 mishanguc 的 {@code roads} 分组中。</p>
     */
    public static void registerItemGroups() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, OcelotSignMod.id("ocelotsign_1_road_signs"), ROAD_SIGNS);
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, OcelotSignMod.id("ocelotsign_2_wall_road_signs"), WALL_ROAD_SIGNS);
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, OcelotSignMod.id("ocelotsign_3_pillars"), PILLARS);
    }

    /** 将 ArrowBlocks 注入到 mishanguc 的 roads 分组中，不影响本模组分类顺序 */
    static {
        var roadsGroupKey = net.minecraft.resources.ResourceKey.create(
                net.minecraft.core.registries.Registries.CREATIVE_MODE_TAB,
                Identifier.fromNamespaceAndPath("mishanguc", "roads")
        );
        CreativeModeTabEvents.modifyOutputEvent(roadsGroupKey)
                .register(entries -> {
                    addAllBlocks(entries,
                            ArrowBlocks.SPEED_BUMP, ArrowBlocks.YIELD, ArrowBlocks.DISTANCE_CONFIRM,

                            ArrowBlocks.ADVANCE_ARROW_STRAIGHT, ArrowBlocks.ADVANCE_ARROW_LEFT,
                            ArrowBlocks.ADVANCE_ARROW_RIGHT, ArrowBlocks.ADVANCE_ARROW_LEFT_UTURN,
                            ArrowBlocks.ADVANCE_ARROW_RIGHT_UTURN, ArrowBlocks.ADVANCE_ARROW_LEFT_RIGHT,
                            ArrowBlocks.ADVANCE_ARROW_STRAIGHT_LEFT_RIGHT, ArrowBlocks.ADVANCE_ARROW_STRAIGHT_LEFT,
                            ArrowBlocks.ADVANCE_ARROW_STRAIGHT_RIGHT, ArrowBlocks.ADVANCE_ARROW_UTURN_LEFT,
                            ArrowBlocks.ADVANCE_ARROW_UTURN_RIGHT, ArrowBlocks.ADVANCE_ARROW_STRAIGHT_UTURN_LEFT,
                            ArrowBlocks.ADVANCE_ARROW_STRAIGHT_UTURN_RIGHT,

                            ArrowBlocks.ORANGE_ARROW_STRAIGHT, ArrowBlocks.ORANGE_ARROW_LEFT,
                            ArrowBlocks.ORANGE_ARROW_RIGHT, ArrowBlocks.ORANGE_ARROW_LEFT_UTURN,
                            ArrowBlocks.ORANGE_ARROW_RIGHT_UTURN, ArrowBlocks.ORANGE_ARROW_LEFT_RIGHT,
                            ArrowBlocks.ORANGE_ARROW_STRAIGHT_LEFT_RIGHT, ArrowBlocks.ORANGE_ARROW_STRAIGHT_LEFT,
                            ArrowBlocks.ORANGE_ARROW_STRAIGHT_RIGHT, ArrowBlocks.ORANGE_ARROW_UTURN_LEFT,
                            ArrowBlocks.ORANGE_ARROW_UTURN_RIGHT, ArrowBlocks.ORANGE_ARROW_STRAIGHT_UTURN_LEFT,
                            ArrowBlocks.ORANGE_ARROW_STRAIGHT_UTURN_RIGHT,

                            ArrowBlocks.ARROW_PROHIBITED, ArrowBlocks.PROHIBITED_ARROW_STRAIGHT,
                            ArrowBlocks.PROHIBITED_ARROW_LEFT, ArrowBlocks.PROHIBITED_ARROW_RIGHT,
                            ArrowBlocks.PROHIBITED_ARROW_LEFT_UTURN, ArrowBlocks.PROHIBITED_ARROW_RIGHT_UTURN,
                            ArrowBlocks.PROHIBITED_ARROW_LEFT_RIGHT, ArrowBlocks.PROHIBITED_ARROW_STRAIGHT_LEFT_RIGHT,
                            ArrowBlocks.PROHIBITED_ARROW_STRAIGHT_LEFT, ArrowBlocks.PROHIBITED_ARROW_STRAIGHT_RIGHT,
                            ArrowBlocks.PROHIBITED_ARROW_UTURN_LEFT, ArrowBlocks.PROHIBITED_ARROW_UTURN_RIGHT,
                            ArrowBlocks.PROHIBITED_ARROW_STRAIGHT_UTURN_LEFT, ArrowBlocks.PROHIBITED_ARROW_STRAIGHT_UTURN_RIGHT,

                            ArrowBlocksLarge.ARROW_LEFT, ArrowBlocksLarge.ARROW_LEFT_MERGE,
                            ArrowBlocksLarge.ARROW_LEFT_RIGHT, ArrowBlocksLarge.ARROW_LEFT_UTURN,
                            ArrowBlocksLarge.ARROW_RIGHT, ArrowBlocksLarge.ARROW_RIGHT_MERGE,
                            ArrowBlocksLarge.ARROW_RIGHT_UTURN, ArrowBlocksLarge.ARROW_STRAIGHT,
                            ArrowBlocksLarge.ARROW_STRAIGHT_LEFT, ArrowBlocksLarge.ARROW_STRAIGHT_LEFT_RIGHT,
                            ArrowBlocksLarge.ARROW_STRAIGHT_RIGHT, ArrowBlocksLarge.ARROW_STRAIGHT_UTURN_LEFT,
                            ArrowBlocksLarge.ARROW_STRAIGHT_UTURN_RIGHT, ArrowBlocksLarge.ARROW_UTURN_LEFT,
                            ArrowBlocksLarge.ARROW_UTURN_RIGHT,

                            ArrowBlocksLarge.DECELERATION_CROSSROADS, ArrowBlocksLarge.SPEED_BUMP,
                            ArrowBlocksLarge.YIELD, ArrowBlocksLarge.DISTANCE_CONFIRM,

                            ArrowBlocksLarge.ADVANCE_ARROW_STRAIGHT, ArrowBlocksLarge.ADVANCE_ARROW_LEFT,
                            ArrowBlocksLarge.ADVANCE_ARROW_RIGHT, ArrowBlocksLarge.ADVANCE_ARROW_LEFT_UTURN,
                            ArrowBlocksLarge.ADVANCE_ARROW_RIGHT_UTURN, ArrowBlocksLarge.ADVANCE_ARROW_LEFT_RIGHT,
                            ArrowBlocksLarge.ADVANCE_ARROW_STRAIGHT_LEFT_RIGHT, ArrowBlocksLarge.ADVANCE_ARROW_STRAIGHT_LEFT,
                            ArrowBlocksLarge.ADVANCE_ARROW_STRAIGHT_RIGHT, ArrowBlocksLarge.ADVANCE_ARROW_UTURN_LEFT,
                            ArrowBlocksLarge.ADVANCE_ARROW_UTURN_RIGHT, ArrowBlocksLarge.ADVANCE_ARROW_STRAIGHT_UTURN_LEFT,
                            ArrowBlocksLarge.ADVANCE_ARROW_STRAIGHT_UTURN_RIGHT,

                            ArrowBlocksLarge.ARROW_PROHIBITED, ArrowBlocksLarge.PROHIBITED_ARROW_STRAIGHT,
                            ArrowBlocksLarge.PROHIBITED_ARROW_LEFT, ArrowBlocksLarge.PROHIBITED_ARROW_RIGHT,
                            ArrowBlocksLarge.PROHIBITED_ARROW_LEFT_UTURN, ArrowBlocksLarge.PROHIBITED_ARROW_RIGHT_UTURN,
                            ArrowBlocksLarge.PROHIBITED_ARROW_LEFT_RIGHT, ArrowBlocksLarge.PROHIBITED_ARROW_STRAIGHT_LEFT_RIGHT,
                            ArrowBlocksLarge.PROHIBITED_ARROW_STRAIGHT_LEFT, ArrowBlocksLarge.PROHIBITED_ARROW_STRAIGHT_RIGHT,
                            ArrowBlocksLarge.PROHIBITED_ARROW_UTURN_LEFT, ArrowBlocksLarge.PROHIBITED_ARROW_UTURN_RIGHT,
                            ArrowBlocksLarge.PROHIBITED_ARROW_STRAIGHT_UTURN_LEFT, ArrowBlocksLarge.PROHIBITED_ARROW_STRAIGHT_UTURN_RIGHT,

                            ArrowBlocksLarge.ORANGE_ARROW_STRAIGHT, ArrowBlocksLarge.ORANGE_ARROW_LEFT,
                            ArrowBlocksLarge.ORANGE_ARROW_RIGHT, ArrowBlocksLarge.ORANGE_ARROW_LEFT_UTURN,
                            ArrowBlocksLarge.ORANGE_ARROW_RIGHT_UTURN, ArrowBlocksLarge.ORANGE_ARROW_LEFT_RIGHT,
                            ArrowBlocksLarge.ORANGE_ARROW_STRAIGHT_LEFT_RIGHT, ArrowBlocksLarge.ORANGE_ARROW_STRAIGHT_LEFT,
                            ArrowBlocksLarge.ORANGE_ARROW_STRAIGHT_RIGHT, ArrowBlocksLarge.ORANGE_ARROW_UTURN_LEFT,
                            ArrowBlocksLarge.ORANGE_ARROW_UTURN_RIGHT, ArrowBlocksLarge.ORANGE_ARROW_STRAIGHT_UTURN_LEFT,
                            ArrowBlocksLarge.ORANGE_ARROW_STRAIGHT_UTURN_RIGHT,

                            ArrowBlocksStyle2.ARROW_PROHIBITED, ArrowBlocksStyle2.PROHIBITED_ARROW_STRAIGHT,
                            ArrowBlocksStyle2.PROHIBITED_ARROW_LEFT, ArrowBlocksStyle2.PROHIBITED_ARROW_RIGHT,
                            ArrowBlocksStyle2.PROHIBITED_ARROW_DOUBLE_LEFT, ArrowBlocksStyle2.PROHIBITED_ARROW_DOUBLE_RIGHT,
                            ArrowBlocksStyle2.PROHIBITED_ARROW_LEFT_RIGHT, ArrowBlocksStyle2.PROHIBITED_ARROW_STRAIGHT_LEFT_RIGHT,
                            ArrowBlocksStyle2.PROHIBITED_ARROW_STRAIGHT_LEFT, ArrowBlocksStyle2.PROHIBITED_ARROW_STRAIGHT_RIGHT,
                            ArrowBlocksStyle2.PROHIBITED_ARROW_UTURN_LEFT, ArrowBlocksStyle2.PROHIBITED_ARROW_UTURN_RIGHT,

                            ArrowBlocksStyle2.ADVANCE_ARROW_STRAIGHT, ArrowBlocksStyle2.ADVANCE_ARROW_LEFT,
                            ArrowBlocksStyle2.ADVANCE_ARROW_RIGHT, ArrowBlocksStyle2.ADVANCE_ARROW_LEFT_RIGHT,
                            ArrowBlocksStyle2.ADVANCE_ARROW_STRAIGHT_LEFT_RIGHT, ArrowBlocksStyle2.ADVANCE_ARROW_STRAIGHT_LEFT,
                            ArrowBlocksStyle2.ADVANCE_ARROW_STRAIGHT_RIGHT, ArrowBlocksStyle2.ADVANCE_ARROW_UTURN_LEFT,
                            ArrowBlocksStyle2.ADVANCE_ARROW_UTURN_RIGHT,

                            ArrowBlocksStyle2.ORANGE_ARROW_STRAIGHT, ArrowBlocksStyle2.ORANGE_ARROW_LEFT,
                            ArrowBlocksStyle2.ORANGE_ARROW_RIGHT, ArrowBlocksStyle2.ORANGE_ARROW_LEFT_RIGHT,
                            ArrowBlocksStyle2.ORANGE_ARROW_STRAIGHT_LEFT_RIGHT, ArrowBlocksStyle2.ORANGE_ARROW_STRAIGHT_LEFT,
                            ArrowBlocksStyle2.ORANGE_ARROW_STRAIGHT_RIGHT, ArrowBlocksStyle2.ORANGE_ARROW_UTURN_LEFT,
                            ArrowBlocksStyle2.ORANGE_ARROW_UTURN_RIGHT,
                            ArrowBlocksStyle2.ORANGE_ARROW_DOUBLE_LEFT, ArrowBlocksStyle2.ORANGE_ARROW_DOUBLE_RIGHT,

                            ArrowBlocksStyle2.ARROW_STRAIGHT, ArrowBlocksStyle2.ARROW_LEFT,
                            ArrowBlocksStyle2.ARROW_RIGHT, ArrowBlocksStyle2.ARROW_DOUBLE_LEFT,
                            ArrowBlocksStyle2.ARROW_DOUBLE_RIGHT, ArrowBlocksStyle2.ARROW_LEFT_RIGHT,
                            ArrowBlocksStyle2.ARROW_STRAIGHT_LEFT_RIGHT, ArrowBlocksStyle2.ARROW_STRAIGHT_LEFT,
                            ArrowBlocksStyle2.ARROW_STRAIGHT_RIGHT, ArrowBlocksStyle2.ARROW_UTURN_LEFT,
                            ArrowBlocksStyle2.ARROW_UTURN_RIGHT,

                            ArrowBlocksStyle3.ARROW_PROHIBITED, ArrowBlocksStyle3.PROHIBITED_ARROW_STRAIGHT,
                            ArrowBlocksStyle3.PROHIBITED_ARROW_LEFT, ArrowBlocksStyle3.PROHIBITED_ARROW_RIGHT,
                            ArrowBlocksStyle3.PROHIBITED_ARROW_LEFT_UTURN, ArrowBlocksStyle3.PROHIBITED_ARROW_RIGHT_UTURN,
                            ArrowBlocksStyle3.PROHIBITED_ARROW_LEFT_RIGHT, ArrowBlocksStyle3.PROHIBITED_ARROW_STRAIGHT_LEFT_RIGHT,
                            ArrowBlocksStyle3.PROHIBITED_ARROW_STRAIGHT_LEFT, ArrowBlocksStyle3.PROHIBITED_ARROW_STRAIGHT_RIGHT,
                            ArrowBlocksStyle3.PROHIBITED_ARROW_UTURN_LEFT, ArrowBlocksStyle3.PROHIBITED_ARROW_UTURN_RIGHT,

                            ArrowBlocksStyle3.ORANGE_ARROW_STRAIGHT, ArrowBlocksStyle3.ORANGE_ARROW_LEFT,
                            ArrowBlocksStyle3.ORANGE_ARROW_RIGHT, ArrowBlocksStyle3.ORANGE_ARROW_LEFT_RIGHT,
                            ArrowBlocksStyle3.ORANGE_ARROW_STRAIGHT_LEFT_RIGHT, ArrowBlocksStyle3.ORANGE_ARROW_STRAIGHT_LEFT,
                            ArrowBlocksStyle3.ORANGE_ARROW_STRAIGHT_RIGHT, ArrowBlocksStyle3.ORANGE_ARROW_UTURN_LEFT,
                            ArrowBlocksStyle3.ORANGE_ARROW_UTURN_RIGHT,

                            ArrowBlocksStyle3.ARROW_STRAIGHT, ArrowBlocksStyle3.ARROW_LEFT,
                            ArrowBlocksStyle3.ARROW_RIGHT, ArrowBlocksStyle3.ARROW_LEFT_RIGHT,
                            ArrowBlocksStyle3.ARROW_STRAIGHT_LEFT_RIGHT, ArrowBlocksStyle3.ARROW_STRAIGHT_LEFT,
                            ArrowBlocksStyle3.ARROW_STRAIGHT_RIGHT, ArrowBlocksStyle3.ARROW_UTURN_LEFT,
                            ArrowBlocksStyle3.ARROW_UTURN_RIGHT
                    );
                });
    }
}
