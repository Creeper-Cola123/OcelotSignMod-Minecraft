package bklmc.ocelotsign.item;

import bklmc.ocelotsign.OcelotSignMod;
import bklmc.ocelotsign.block.RoadSignBlocks;
import bklmc.ocelotsign.block.WallRoadSignBlocks;
import bklmc.ocelotsign.block.PillarBlocks;
import bklmc.ocelotsign.integration.mishanguc.MishangAccess;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static bklmc.ocelotsign.block.RoadSignBlocks.*;
import static bklmc.ocelotsign.block.WallRoadSignBlocks.*;
import static bklmc.ocelotsign.block.PillarBlocks.*;

/**
 * 物品创造栏分组注册
 *
 * @see ModItems
 */
public class ModItemGroups {
    private static final Logger LOGGER = LoggerFactory.getLogger(ModItemGroups.class);

    /**
     * 道路指示牌分类
     */
    public static ItemGroup getRoadSigns() {
        return ROAD_SIGNS;
    }

    private static final ItemGroup ROAD_SIGNS = FabricItemGroupBuilder.create(
                    new Identifier(OcelotSignMod.MOD_ID, "road_signs"))
            .icon(() -> new ItemStack(ModItems.ROAD_SIGNS_ICON))
            .appendItems(stacks -> {
                // 蓝色
                stacks.add(new ItemStack(BLUE_ROAD_SIGN_LEFT_TOP));
                stacks.add(new ItemStack(BLUE_ROAD_SIGN_TOP));
                stacks.add(new ItemStack(BLUE_ROAD_SIGN_RIGHT_TOP));
                stacks.add(new ItemStack(BLUE_ROAD_SIGN_LEFT));
                stacks.add(new ItemStack(BLUE_ROAD_SIGN_MIDDLE));
                stacks.add(new ItemStack(BLUE_ROAD_SIGN_RIGHT));
                stacks.add(new ItemStack(BLUE_ROAD_SIGN_LEFT_BOTTOM));
                stacks.add(new ItemStack(BLUE_ROAD_SIGN_BOTTOM));
                stacks.add(new ItemStack(BLUE_ROAD_SIGN_RIGHT_BOTTOM));
                stacks.add(new ItemStack(BLUE_ROAD_SIGN_HORIZONTAL_LEFT));
                stacks.add(new ItemStack(BLUE_ROAD_SIGN_HORIZONTAL_MIDDLE));
                stacks.add(new ItemStack(BLUE_ROAD_SIGN_HORIZONTAL_RIGHT));
                stacks.add(new ItemStack(BLUE_ROAD_SIGN_VERTICAL_TOP));
                stacks.add(new ItemStack(BLUE_ROAD_SIGN_VERTICAL_MIDDLE));
                stacks.add(new ItemStack(BLUE_ROAD_SIGN_VERTICAL_BOTTOM));
                stacks.add(new ItemStack(BLUE_ROAD_SIGN_SMALL_A));
                stacks.add(new ItemStack(BLUE_ROAD_SIGN_SMALL_B));

                // 绿色
                stacks.add(new ItemStack(GREEN_ROAD_SIGN_LEFT_TOP));
                stacks.add(new ItemStack(GREEN_ROAD_SIGN_TOP));
                stacks.add(new ItemStack(GREEN_ROAD_SIGN_RIGHT_TOP));
                stacks.add(new ItemStack(GREEN_ROAD_SIGN_LEFT));
                stacks.add(new ItemStack(GREEN_ROAD_SIGN_MIDDLE));
                stacks.add(new ItemStack(GREEN_ROAD_SIGN_RIGHT));
                stacks.add(new ItemStack(GREEN_ROAD_SIGN_LEFT_BOTTOM));
                stacks.add(new ItemStack(GREEN_ROAD_SIGN_BOTTOM));
                stacks.add(new ItemStack(GREEN_ROAD_SIGN_RIGHT_BOTTOM));
                stacks.add(new ItemStack(GREEN_ROAD_SIGN_HORIZONTAL_LEFT));
                stacks.add(new ItemStack(GREEN_ROAD_SIGN_HORIZONTAL_MIDDLE));
                stacks.add(new ItemStack(GREEN_ROAD_SIGN_HORIZONTAL_RIGHT));
                stacks.add(new ItemStack(GREEN_ROAD_SIGN_VERTICAL_TOP));
                stacks.add(new ItemStack(GREEN_ROAD_SIGN_VERTICAL_MIDDLE));
                stacks.add(new ItemStack(GREEN_ROAD_SIGN_VERTICAL_BOTTOM));
                stacks.add(new ItemStack(GREEN_ROAD_SIGN_SMALL_A));
                stacks.add(new ItemStack(GREEN_ROAD_SIGN_SMALL_B));

                // 黄色
                stacks.add(new ItemStack(YELLOW_ROAD_SIGN_LEFT_TOP));
                stacks.add(new ItemStack(YELLOW_ROAD_SIGN_TOP));
                stacks.add(new ItemStack(YELLOW_ROAD_SIGN_RIGHT_TOP));
                stacks.add(new ItemStack(YELLOW_ROAD_SIGN_LEFT));
                stacks.add(new ItemStack(YELLOW_ROAD_SIGN_MIDDLE));
                stacks.add(new ItemStack(YELLOW_ROAD_SIGN_RIGHT));
                stacks.add(new ItemStack(YELLOW_ROAD_SIGN_LEFT_BOTTOM));
                stacks.add(new ItemStack(YELLOW_ROAD_SIGN_BOTTOM));
                stacks.add(new ItemStack(YELLOW_ROAD_SIGN_RIGHT_BOTTOM));
                stacks.add(new ItemStack(YELLOW_ROAD_SIGN_HORIZONTAL_LEFT));
                stacks.add(new ItemStack(YELLOW_ROAD_SIGN_HORIZONTAL_MIDDLE));
                stacks.add(new ItemStack(YELLOW_ROAD_SIGN_HORIZONTAL_RIGHT));
                stacks.add(new ItemStack(YELLOW_ROAD_SIGN_VERTICAL_TOP));
                stacks.add(new ItemStack(YELLOW_ROAD_SIGN_VERTICAL_MIDDLE));
                stacks.add(new ItemStack(YELLOW_ROAD_SIGN_VERTICAL_BOTTOM));
                stacks.add(new ItemStack(YELLOW_ROAD_SIGN_SMALL_A));
                stacks.add(new ItemStack(YELLOW_ROAD_SIGN_SMALL_B));

                // 白色
                stacks.add(new ItemStack(WHITE_ROAD_SIGN_LEFT_TOP));
                stacks.add(new ItemStack(WHITE_ROAD_SIGN_TOP));
                stacks.add(new ItemStack(WHITE_ROAD_SIGN_RIGHT_TOP));
                stacks.add(new ItemStack(WHITE_ROAD_SIGN_LEFT));
                stacks.add(new ItemStack(WHITE_ROAD_SIGN_MIDDLE));
                stacks.add(new ItemStack(WHITE_ROAD_SIGN_RIGHT));
                stacks.add(new ItemStack(WHITE_ROAD_SIGN_LEFT_BOTTOM));
                stacks.add(new ItemStack(WHITE_ROAD_SIGN_BOTTOM));
                stacks.add(new ItemStack(WHITE_ROAD_SIGN_RIGHT_BOTTOM));
                stacks.add(new ItemStack(WHITE_ROAD_SIGN_HORIZONTAL_LEFT));
                stacks.add(new ItemStack(WHITE_ROAD_SIGN_HORIZONTAL_MIDDLE));
                stacks.add(new ItemStack(WHITE_ROAD_SIGN_HORIZONTAL_RIGHT));
                stacks.add(new ItemStack(WHITE_ROAD_SIGN_VERTICAL_TOP));
                stacks.add(new ItemStack(WHITE_ROAD_SIGN_VERTICAL_MIDDLE));
                stacks.add(new ItemStack(WHITE_ROAD_SIGN_VERTICAL_BOTTOM));
                stacks.add(new ItemStack(WHITE_ROAD_SIGN_SMALL_A));
                stacks.add(new ItemStack(WHITE_ROAD_SIGN_SMALL_B));

                // 棕色
                stacks.add(new ItemStack(BROWN_ROAD_SIGN_LEFT_TOP));
                stacks.add(new ItemStack(BROWN_ROAD_SIGN_TOP));
                stacks.add(new ItemStack(BROWN_ROAD_SIGN_RIGHT_TOP));
                stacks.add(new ItemStack(BROWN_ROAD_SIGN_LEFT));
                stacks.add(new ItemStack(BROWN_ROAD_SIGN_MIDDLE));
                stacks.add(new ItemStack(BROWN_ROAD_SIGN_RIGHT));
                stacks.add(new ItemStack(BROWN_ROAD_SIGN_LEFT_BOTTOM));
                stacks.add(new ItemStack(BROWN_ROAD_SIGN_BOTTOM));
                stacks.add(new ItemStack(BROWN_ROAD_SIGN_RIGHT_BOTTOM));
                stacks.add(new ItemStack(BROWN_ROAD_SIGN_HORIZONTAL_LEFT));
                stacks.add(new ItemStack(BROWN_ROAD_SIGN_HORIZONTAL_MIDDLE));
                stacks.add(new ItemStack(BROWN_ROAD_SIGN_HORIZONTAL_RIGHT));
                stacks.add(new ItemStack(BROWN_ROAD_SIGN_VERTICAL_TOP));
                stacks.add(new ItemStack(BROWN_ROAD_SIGN_VERTICAL_MIDDLE));
                stacks.add(new ItemStack(BROWN_ROAD_SIGN_VERTICAL_BOTTOM));
                stacks.add(new ItemStack(BROWN_ROAD_SIGN_SMALL_A));
                stacks.add(new ItemStack(BROWN_ROAD_SIGN_SMALL_B));

                // 橙色
                stacks.add(new ItemStack(ORANGE_ROAD_SIGN_LEFT_TOP));
                stacks.add(new ItemStack(ORANGE_ROAD_SIGN_TOP));
                stacks.add(new ItemStack(ORANGE_ROAD_SIGN_RIGHT_TOP));
                stacks.add(new ItemStack(ORANGE_ROAD_SIGN_LEFT));
                stacks.add(new ItemStack(ORANGE_ROAD_SIGN_MIDDLE));
                stacks.add(new ItemStack(ORANGE_ROAD_SIGN_RIGHT));
                stacks.add(new ItemStack(ORANGE_ROAD_SIGN_LEFT_BOTTOM));
                stacks.add(new ItemStack(ORANGE_ROAD_SIGN_BOTTOM));
                stacks.add(new ItemStack(ORANGE_ROAD_SIGN_RIGHT_BOTTOM));
                stacks.add(new ItemStack(ORANGE_ROAD_SIGN_HORIZONTAL_LEFT));
                stacks.add(new ItemStack(ORANGE_ROAD_SIGN_HORIZONTAL_MIDDLE));
                stacks.add(new ItemStack(ORANGE_ROAD_SIGN_HORIZONTAL_RIGHT));
                stacks.add(new ItemStack(ORANGE_ROAD_SIGN_VERTICAL_TOP));
                stacks.add(new ItemStack(ORANGE_ROAD_SIGN_VERTICAL_MIDDLE));
                stacks.add(new ItemStack(ORANGE_ROAD_SIGN_VERTICAL_BOTTOM));
                stacks.add(new ItemStack(ORANGE_ROAD_SIGN_SMALL_A));
                stacks.add(new ItemStack(ORANGE_ROAD_SIGN_SMALL_B));
            })
            .build();

    /**
     * 墙道路指示牌分类
     */
    public static ItemGroup getWallRoadSigns() {
        return WALL_ROAD_SIGNS;
    }

    private static final ItemGroup WALL_ROAD_SIGNS = FabricItemGroupBuilder.create(
                    new Identifier(OcelotSignMod.MOD_ID, "wall_road_signs"))
            .icon(() -> new ItemStack(ModItems.WALL_ROAD_SIGNS_ICON))
            .appendItems(stacks -> {
                // 蓝色墙道路指示牌
                stacks.add(new ItemStack(BLUE_WALL_ROAD_SIGN_LEFT_TOP));
                stacks.add(new ItemStack(BLUE_WALL_ROAD_SIGN_TOP));
                stacks.add(new ItemStack(BLUE_WALL_ROAD_SIGN_RIGHT_TOP));
                stacks.add(new ItemStack(BLUE_WALL_ROAD_SIGN_LEFT));
                stacks.add(new ItemStack(BLUE_WALL_ROAD_SIGN_MIDDLE));
                stacks.add(new ItemStack(BLUE_WALL_ROAD_SIGN_RIGHT));
                stacks.add(new ItemStack(BLUE_WALL_ROAD_SIGN_LEFT_BOTTOM));
                stacks.add(new ItemStack(BLUE_WALL_ROAD_SIGN_BOTTOM));
                stacks.add(new ItemStack(BLUE_WALL_ROAD_SIGN_RIGHT_BOTTOM));
                stacks.add(new ItemStack(BLUE_WALL_ROAD_SIGN_HORIZONTAL_LEFT));
                stacks.add(new ItemStack(BLUE_WALL_ROAD_SIGN_HORIZONTAL_MIDDLE));
                stacks.add(new ItemStack(BLUE_WALL_ROAD_SIGN_HORIZONTAL_RIGHT));
                stacks.add(new ItemStack(BLUE_WALL_ROAD_SIGN_VERTICAL_TOP));
                stacks.add(new ItemStack(BLUE_WALL_ROAD_SIGN_VERTICAL_MIDDLE));
                stacks.add(new ItemStack(BLUE_WALL_ROAD_SIGN_VERTICAL_BOTTOM));
                stacks.add(new ItemStack(BLUE_WALL_ROAD_SIGN_SMALL_A));
                stacks.add(new ItemStack(BLUE_WALL_ROAD_SIGN_SMALL_B));
                stacks.add(new ItemStack(BLUE_WALL_ROAD_SIGN_RHOMBUS));

                // 绿色墙道路指示牌
                stacks.add(new ItemStack(GREEN_WALL_ROAD_SIGN_LEFT_TOP));
                stacks.add(new ItemStack(GREEN_WALL_ROAD_SIGN_TOP));
                stacks.add(new ItemStack(GREEN_WALL_ROAD_SIGN_RIGHT_TOP));
                stacks.add(new ItemStack(GREEN_WALL_ROAD_SIGN_LEFT));
                stacks.add(new ItemStack(GREEN_WALL_ROAD_SIGN_MIDDLE));
                stacks.add(new ItemStack(GREEN_WALL_ROAD_SIGN_RIGHT));
                stacks.add(new ItemStack(GREEN_WALL_ROAD_SIGN_LEFT_BOTTOM));
                stacks.add(new ItemStack(GREEN_WALL_ROAD_SIGN_BOTTOM));
                stacks.add(new ItemStack(GREEN_WALL_ROAD_SIGN_RIGHT_BOTTOM));
                stacks.add(new ItemStack(GREEN_WALL_ROAD_SIGN_HORIZONTAL_LEFT));
                stacks.add(new ItemStack(GREEN_WALL_ROAD_SIGN_HORIZONTAL_MIDDLE));
                stacks.add(new ItemStack(GREEN_WALL_ROAD_SIGN_HORIZONTAL_RIGHT));
                stacks.add(new ItemStack(GREEN_WALL_ROAD_SIGN_VERTICAL_TOP));
                stacks.add(new ItemStack(GREEN_WALL_ROAD_SIGN_VERTICAL_MIDDLE));
                stacks.add(new ItemStack(GREEN_WALL_ROAD_SIGN_VERTICAL_BOTTOM));
                stacks.add(new ItemStack(GREEN_WALL_ROAD_SIGN_SMALL_A));
                stacks.add(new ItemStack(GREEN_WALL_ROAD_SIGN_SMALL_B));
                stacks.add(new ItemStack(GREEN_WALL_ROAD_SIGN_RHOMBUS));

                // 黄色墙道路指示牌
                stacks.add(new ItemStack(YELLOW_WALL_ROAD_SIGN_LEFT_TOP));
                stacks.add(new ItemStack(YELLOW_WALL_ROAD_SIGN_TOP));
                stacks.add(new ItemStack(YELLOW_WALL_ROAD_SIGN_RIGHT_TOP));
                stacks.add(new ItemStack(YELLOW_WALL_ROAD_SIGN_LEFT));
                stacks.add(new ItemStack(YELLOW_WALL_ROAD_SIGN_MIDDLE));
                stacks.add(new ItemStack(YELLOW_WALL_ROAD_SIGN_RIGHT));
                stacks.add(new ItemStack(YELLOW_WALL_ROAD_SIGN_LEFT_BOTTOM));
                stacks.add(new ItemStack(YELLOW_WALL_ROAD_SIGN_BOTTOM));
                stacks.add(new ItemStack(YELLOW_WALL_ROAD_SIGN_RIGHT_BOTTOM));
                stacks.add(new ItemStack(YELLOW_WALL_ROAD_SIGN_HORIZONTAL_LEFT));
                stacks.add(new ItemStack(YELLOW_WALL_ROAD_SIGN_HORIZONTAL_MIDDLE));
                stacks.add(new ItemStack(YELLOW_WALL_ROAD_SIGN_HORIZONTAL_RIGHT));
                stacks.add(new ItemStack(YELLOW_WALL_ROAD_SIGN_VERTICAL_TOP));
                stacks.add(new ItemStack(YELLOW_WALL_ROAD_SIGN_VERTICAL_MIDDLE));
                stacks.add(new ItemStack(YELLOW_WALL_ROAD_SIGN_VERTICAL_BOTTOM));
                stacks.add(new ItemStack(YELLOW_WALL_ROAD_SIGN_SMALL_A));
                stacks.add(new ItemStack(YELLOW_WALL_ROAD_SIGN_SMALL_B));
                stacks.add(new ItemStack(YELLOW_WALL_ROAD_SIGN_RHOMBUS));

                // 白色墙道路指示牌
                stacks.add(new ItemStack(WHITE_WALL_ROAD_SIGN_LEFT_TOP));
                stacks.add(new ItemStack(WHITE_WALL_ROAD_SIGN_TOP));
                stacks.add(new ItemStack(WHITE_WALL_ROAD_SIGN_RIGHT_TOP));
                stacks.add(new ItemStack(WHITE_WALL_ROAD_SIGN_LEFT));
                stacks.add(new ItemStack(WHITE_WALL_ROAD_SIGN_MIDDLE));
                stacks.add(new ItemStack(WHITE_WALL_ROAD_SIGN_RIGHT));
                stacks.add(new ItemStack(WHITE_WALL_ROAD_SIGN_LEFT_BOTTOM));
                stacks.add(new ItemStack(WHITE_WALL_ROAD_SIGN_BOTTOM));
                stacks.add(new ItemStack(WHITE_WALL_ROAD_SIGN_RIGHT_BOTTOM));
                stacks.add(new ItemStack(WHITE_WALL_ROAD_SIGN_HORIZONTAL_LEFT));
                stacks.add(new ItemStack(WHITE_WALL_ROAD_SIGN_HORIZONTAL_MIDDLE));
                stacks.add(new ItemStack(WHITE_WALL_ROAD_SIGN_HORIZONTAL_RIGHT));
                stacks.add(new ItemStack(WHITE_WALL_ROAD_SIGN_VERTICAL_TOP));
                stacks.add(new ItemStack(WHITE_WALL_ROAD_SIGN_VERTICAL_MIDDLE));
                stacks.add(new ItemStack(WHITE_WALL_ROAD_SIGN_VERTICAL_BOTTOM));
                stacks.add(new ItemStack(WHITE_WALL_ROAD_SIGN_SMALL_A));
                stacks.add(new ItemStack(WHITE_WALL_ROAD_SIGN_SMALL_B));
                stacks.add(new ItemStack(WHITE_WALL_ROAD_SIGN_RHOMBUS));

                // 棕色墙道路指示牌
                stacks.add(new ItemStack(BROWN_WALL_ROAD_SIGN_LEFT_TOP));
                stacks.add(new ItemStack(BROWN_WALL_ROAD_SIGN_TOP));
                stacks.add(new ItemStack(BROWN_WALL_ROAD_SIGN_RIGHT_TOP));
                stacks.add(new ItemStack(BROWN_WALL_ROAD_SIGN_LEFT));
                stacks.add(new ItemStack(BROWN_WALL_ROAD_SIGN_MIDDLE));
                stacks.add(new ItemStack(BROWN_WALL_ROAD_SIGN_RIGHT));
                stacks.add(new ItemStack(BROWN_WALL_ROAD_SIGN_LEFT_BOTTOM));
                stacks.add(new ItemStack(BROWN_WALL_ROAD_SIGN_BOTTOM));
                stacks.add(new ItemStack(BROWN_WALL_ROAD_SIGN_RIGHT_BOTTOM));
                stacks.add(new ItemStack(BROWN_WALL_ROAD_SIGN_HORIZONTAL_LEFT));
                stacks.add(new ItemStack(BROWN_WALL_ROAD_SIGN_HORIZONTAL_MIDDLE));
                stacks.add(new ItemStack(BROWN_WALL_ROAD_SIGN_HORIZONTAL_RIGHT));
                stacks.add(new ItemStack(BROWN_WALL_ROAD_SIGN_VERTICAL_TOP));
                stacks.add(new ItemStack(BROWN_WALL_ROAD_SIGN_VERTICAL_MIDDLE));
                stacks.add(new ItemStack(BROWN_WALL_ROAD_SIGN_VERTICAL_BOTTOM));
                stacks.add(new ItemStack(BROWN_WALL_ROAD_SIGN_SMALL_A));
                stacks.add(new ItemStack(BROWN_WALL_ROAD_SIGN_SMALL_B));
                stacks.add(new ItemStack(BROWN_WALL_ROAD_SIGN_RHOMBUS));

                // 橙色墙道路指示牌
                stacks.add(new ItemStack(ORANGE_WALL_ROAD_SIGN_LEFT_TOP));
                stacks.add(new ItemStack(ORANGE_WALL_ROAD_SIGN_TOP));
                stacks.add(new ItemStack(ORANGE_WALL_ROAD_SIGN_RIGHT_TOP));
                stacks.add(new ItemStack(ORANGE_WALL_ROAD_SIGN_LEFT));
                stacks.add(new ItemStack(ORANGE_WALL_ROAD_SIGN_MIDDLE));
                stacks.add(new ItemStack(ORANGE_WALL_ROAD_SIGN_RIGHT));
                stacks.add(new ItemStack(ORANGE_WALL_ROAD_SIGN_LEFT_BOTTOM));
                stacks.add(new ItemStack(ORANGE_WALL_ROAD_SIGN_BOTTOM));
                stacks.add(new ItemStack(ORANGE_WALL_ROAD_SIGN_RIGHT_BOTTOM));
                stacks.add(new ItemStack(ORANGE_WALL_ROAD_SIGN_HORIZONTAL_LEFT));
                stacks.add(new ItemStack(ORANGE_WALL_ROAD_SIGN_HORIZONTAL_MIDDLE));
                stacks.add(new ItemStack(ORANGE_WALL_ROAD_SIGN_HORIZONTAL_RIGHT));
                stacks.add(new ItemStack(ORANGE_WALL_ROAD_SIGN_VERTICAL_TOP));
                stacks.add(new ItemStack(ORANGE_WALL_ROAD_SIGN_VERTICAL_MIDDLE));
                stacks.add(new ItemStack(ORANGE_WALL_ROAD_SIGN_VERTICAL_BOTTOM));
                stacks.add(new ItemStack(ORANGE_WALL_ROAD_SIGN_SMALL_A));
                stacks.add(new ItemStack(ORANGE_WALL_ROAD_SIGN_SMALL_B));

                // 特殊标志
                stacks.add(new ItemStack(NO_PARKING));
                stacks.add(new ItemStack(NO_LONG_TERM_PARKING));
                stacks.add(new ItemStack(FORBID_2));
                stacks.add(new ItemStack(BLUE_ROUND));
                stacks.add(new ItemStack(RED_ROUND));
                stacks.add(new ItemStack(GREEN_ROUND));
                stacks.add(new ItemStack(YELLOW_ROUND));
                stacks.add(new ItemStack(WHITE_ROUND));
                stacks.add(new ItemStack(FORBID_1));
                stacks.add(new ItemStack(NO_ENTRY));
                stacks.add(new ItemStack(HEIGHT_LIMIT));
                stacks.add(new ItemStack(WIDTH_LIMIT));
                stacks.add(new ItemStack(WEIGHT_LIMIT));
                stacks.add(new ItemStack(LIFT_SPEED_LIMIT));
                stacks.add(new ItemStack(LOW_SPEED_LIMIT));
                stacks.add(new ItemStack(MAX_SPEED_LIMIT));
                stacks.add(new ItemStack(WARNING));
                stacks.add(new ItemStack(WARNING_RED_STROKE));
                stacks.add(new ItemStack(STOP));
                stacks.add(new ItemStack(NATIONAL_EXPRESSWAY));
                stacks.add(new ItemStack(PROVINCIAL_EXPRESSWAY));
                stacks.add(new ItemStack(NATIONAL_HIGHWAY));
                stacks.add(new ItemStack(PROVINCIAL_HIGHWAY));
                stacks.add(new ItemStack(COUNTRY_HIGHWAY));
                stacks.add(new ItemStack(YIELD));
                stacks.add(new ItemStack(GREEN_MILEAGE_SIGN));
                stacks.add(new ItemStack(BLUE_MILEAGE_SIGN));
                stacks.add(new ItemStack(GREEN_HECTOMETER_SIGN));
                stacks.add(new ItemStack(BLUE_HECTOMETER_SIGN));
                stacks.add(new ItemStack(YIELD_RED_FILL));
                stacks.add(new ItemStack(FORBID_3));
                stacks.add(new ItemStack(FORBID_4));
                stacks.add(new ItemStack(MAX_SPEED_LIMIT_YELLOW_FILL));
                stacks.add(new ItemStack(WARNING_RED_STROKE_YELLOW_FILL));
                stacks.add(new ItemStack(LIFT_SPEED_LIMIT_YELLOW_FILL));
                stacks.add(new ItemStack(WARNING_BLUE_FILL));
                stacks.add(new ItemStack(WARNING_YELLOW_FILL));
                stacks.add(new ItemStack(WARNING_GREEN_FILL));
                stacks.add(new ItemStack(WARNING_2));

                // 蓝色箭头
                stacks.add(new ItemStack(BLUE_CHEVRON_ALIGNMENT_A_LEFT));
                stacks.add(new ItemStack(BLUE_CHEVRON_ALIGNMENT_A_RIGHT));
                stacks.add(new ItemStack(BLUE_CHEVRON_ALIGNMENT_B_LEFT));
                stacks.add(new ItemStack(BLUE_CHEVRON_ALIGNMENT_B_RIGHT));
                stacks.add(new ItemStack(BLUE_CHEVRON_ALIGNMENT_C_LEFT));
                stacks.add(new ItemStack(BLUE_CHEVRON_ALIGNMENT_C_RIGHT));
                stacks.add(new ItemStack(BLUE_CHEVRON_ALIGNMENT_C_UP));

                // 绿色箭头
                stacks.add(new ItemStack(GREEN_CHEVRON_ALIGNMENT_A_LEFT));
                stacks.add(new ItemStack(GREEN_CHEVRON_ALIGNMENT_A_RIGHT));
                stacks.add(new ItemStack(GREEN_CHEVRON_ALIGNMENT_B_LEFT));
                stacks.add(new ItemStack(GREEN_CHEVRON_ALIGNMENT_B_RIGHT));
                stacks.add(new ItemStack(GREEN_CHEVRON_ALIGNMENT_C_LEFT));
                stacks.add(new ItemStack(GREEN_CHEVRON_ALIGNMENT_C_RIGHT));
                stacks.add(new ItemStack(GREEN_CHEVRON_ALIGNMENT_C_UP));

                // 红色箭头
                stacks.add(new ItemStack(RED_CHEVRON_ALIGNMENT_A_LEFT));
                stacks.add(new ItemStack(RED_CHEVRON_ALIGNMENT_A_RIGHT));
                stacks.add(new ItemStack(RED_CHEVRON_ALIGNMENT_B_LEFT));
                stacks.add(new ItemStack(RED_CHEVRON_ALIGNMENT_B_RIGHT));
                stacks.add(new ItemStack(RED_CHEVRON_ALIGNMENT_C_LEFT));
                stacks.add(new ItemStack(RED_CHEVRON_ALIGNMENT_C_RIGHT));
                stacks.add(new ItemStack(RED_CHEVRON_ALIGNMENT_C_UP));

                // 黄色箭头
                stacks.add(new ItemStack(YELLOW_CHEVRON_ALIGNMENT_A_LEFT));
                stacks.add(new ItemStack(YELLOW_CHEVRON_ALIGNMENT_A_RIGHT));
                stacks.add(new ItemStack(YELLOW_CHEVRON_ALIGNMENT_B_LEFT));
                stacks.add(new ItemStack(YELLOW_CHEVRON_ALIGNMENT_B_RIGHT));
                stacks.add(new ItemStack(YELLOW_CHEVRON_ALIGNMENT_C_LEFT));
                stacks.add(new ItemStack(YELLOW_CHEVRON_ALIGNMENT_C_RIGHT));
                stacks.add(new ItemStack(YELLOW_CHEVRON_ALIGNMENT_C_UP));

                // 迷你标志 - 蓝色
                stacks.add(new ItemStack(BLUE_WALL_ROAD_SIGN_MINI_01));
                stacks.add(new ItemStack(BLUE_WALL_ROAD_SIGN_MINI_02));
                stacks.add(new ItemStack(BLUE_WALL_ROAD_SIGN_MINI_03));
                stacks.add(new ItemStack(BLUE_WALL_ROAD_SIGN_MINI_04));
                stacks.add(new ItemStack(BLUE_WALL_ROAD_SIGN_MINI_05));
                stacks.add(new ItemStack(BLUE_WALL_ROAD_SIGN_MINI_06));
                stacks.add(new ItemStack(BLUE_WALL_ROAD_SIGN_MINI_07));
                stacks.add(new ItemStack(BLUE_WALL_ROAD_SIGN_MINI_08));
                stacks.add(new ItemStack(BLUE_WALL_ROAD_SIGN_MINI_09));
                stacks.add(new ItemStack(BLUE_WALL_ROAD_SIGN_MINI_10));
                stacks.add(new ItemStack(BLUE_WALL_ROAD_SIGN_MINI_11));
                stacks.add(new ItemStack(BLUE_WALL_ROAD_SIGN_MINI_12));
                stacks.add(new ItemStack(BLUE_WALL_ROAD_SIGN_MINI_13));
                stacks.add(new ItemStack(BLUE_WALL_ROAD_SIGN_MINI_14));
                stacks.add(new ItemStack(BLUE_WALL_ROAD_SIGN_MINI_15));
                stacks.add(new ItemStack(BLUE_WALL_ROAD_SIGN_MINI_16));
                stacks.add(new ItemStack(BLUE_WALL_ROAD_SIGN_MINI_17));
                stacks.add(new ItemStack(BLUE_WALL_ROAD_SIGN_MINI_18));
                stacks.add(new ItemStack(BLUE_WALL_ROAD_SIGN_MINI_19));

                // 迷你标志 - 绿色
                stacks.add(new ItemStack(GREEN_WALL_ROAD_SIGN_MINI_01));
                stacks.add(new ItemStack(GREEN_WALL_ROAD_SIGN_MINI_02));
                stacks.add(new ItemStack(GREEN_WALL_ROAD_SIGN_MINI_03));
                stacks.add(new ItemStack(GREEN_WALL_ROAD_SIGN_MINI_04));
                stacks.add(new ItemStack(GREEN_WALL_ROAD_SIGN_MINI_05));
                stacks.add(new ItemStack(GREEN_WALL_ROAD_SIGN_MINI_06));
                stacks.add(new ItemStack(GREEN_WALL_ROAD_SIGN_MINI_07));
                stacks.add(new ItemStack(GREEN_WALL_ROAD_SIGN_MINI_08));
                stacks.add(new ItemStack(GREEN_WALL_ROAD_SIGN_MINI_09));
                stacks.add(new ItemStack(GREEN_WALL_ROAD_SIGN_MINI_10));
                stacks.add(new ItemStack(GREEN_WALL_ROAD_SIGN_MINI_11));
                stacks.add(new ItemStack(GREEN_WALL_ROAD_SIGN_MINI_12));
                stacks.add(new ItemStack(GREEN_WALL_ROAD_SIGN_MINI_13));
                stacks.add(new ItemStack(GREEN_WALL_ROAD_SIGN_MINI_14));
                stacks.add(new ItemStack(GREEN_WALL_ROAD_SIGN_MINI_15));
                stacks.add(new ItemStack(GREEN_WALL_ROAD_SIGN_MINI_16));
                stacks.add(new ItemStack(GREEN_WALL_ROAD_SIGN_MINI_17));
                stacks.add(new ItemStack(GREEN_WALL_ROAD_SIGN_MINI_18));
                stacks.add(new ItemStack(GREEN_WALL_ROAD_SIGN_MINI_19));

                // 迷你标志 - 黄色
                stacks.add(new ItemStack(YELLOW_WALL_ROAD_SIGN_MINI_01));
                stacks.add(new ItemStack(YELLOW_WALL_ROAD_SIGN_MINI_02));
                stacks.add(new ItemStack(YELLOW_WALL_ROAD_SIGN_MINI_03));
                stacks.add(new ItemStack(YELLOW_WALL_ROAD_SIGN_MINI_04));
                stacks.add(new ItemStack(YELLOW_WALL_ROAD_SIGN_MINI_05));
                stacks.add(new ItemStack(YELLOW_WALL_ROAD_SIGN_MINI_06));
                stacks.add(new ItemStack(YELLOW_WALL_ROAD_SIGN_MINI_07));
                stacks.add(new ItemStack(YELLOW_WALL_ROAD_SIGN_MINI_08));
                stacks.add(new ItemStack(YELLOW_WALL_ROAD_SIGN_MINI_09));
                stacks.add(new ItemStack(YELLOW_WALL_ROAD_SIGN_MINI_10));
                stacks.add(new ItemStack(YELLOW_WALL_ROAD_SIGN_MINI_11));
                stacks.add(new ItemStack(YELLOW_WALL_ROAD_SIGN_MINI_12));
                stacks.add(new ItemStack(YELLOW_WALL_ROAD_SIGN_MINI_13));
                stacks.add(new ItemStack(YELLOW_WALL_ROAD_SIGN_MINI_14));
                stacks.add(new ItemStack(YELLOW_WALL_ROAD_SIGN_MINI_15));
                stacks.add(new ItemStack(YELLOW_WALL_ROAD_SIGN_MINI_16));
                stacks.add(new ItemStack(YELLOW_WALL_ROAD_SIGN_MINI_17));
                stacks.add(new ItemStack(YELLOW_WALL_ROAD_SIGN_MINI_18));
                stacks.add(new ItemStack(YELLOW_WALL_ROAD_SIGN_MINI_19));

                // 迷你标志 - 白色
                stacks.add(new ItemStack(WHITE_WALL_ROAD_SIGN_MINI_01));
                stacks.add(new ItemStack(WHITE_WALL_ROAD_SIGN_MINI_02));
                stacks.add(new ItemStack(WHITE_WALL_ROAD_SIGN_MINI_03));
                stacks.add(new ItemStack(WHITE_WALL_ROAD_SIGN_MINI_04));
                stacks.add(new ItemStack(WHITE_WALL_ROAD_SIGN_MINI_05));
                stacks.add(new ItemStack(WHITE_WALL_ROAD_SIGN_MINI_06));
                stacks.add(new ItemStack(WHITE_WALL_ROAD_SIGN_MINI_07));
                stacks.add(new ItemStack(WHITE_WALL_ROAD_SIGN_MINI_08));
                stacks.add(new ItemStack(WHITE_WALL_ROAD_SIGN_MINI_09));
                stacks.add(new ItemStack(WHITE_WALL_ROAD_SIGN_MINI_10));
                stacks.add(new ItemStack(WHITE_WALL_ROAD_SIGN_MINI_11));
                stacks.add(new ItemStack(WHITE_WALL_ROAD_SIGN_MINI_12));
                stacks.add(new ItemStack(WHITE_WALL_ROAD_SIGN_MINI_13));
                stacks.add(new ItemStack(WHITE_WALL_ROAD_SIGN_MINI_14));
                stacks.add(new ItemStack(WHITE_WALL_ROAD_SIGN_MINI_15));
                stacks.add(new ItemStack(WHITE_WALL_ROAD_SIGN_MINI_16));
                stacks.add(new ItemStack(WHITE_WALL_ROAD_SIGN_MINI_17));
                stacks.add(new ItemStack(WHITE_WALL_ROAD_SIGN_MINI_18));
                stacks.add(new ItemStack(WHITE_WALL_ROAD_SIGN_MINI_19));

                // 迷你标志 - 棕色
                stacks.add(new ItemStack(BROWN_WALL_ROAD_SIGN_MINI_01));
                stacks.add(new ItemStack(BROWN_WALL_ROAD_SIGN_MINI_02));
                stacks.add(new ItemStack(BROWN_WALL_ROAD_SIGN_MINI_03));
                stacks.add(new ItemStack(BROWN_WALL_ROAD_SIGN_MINI_04));
                stacks.add(new ItemStack(BROWN_WALL_ROAD_SIGN_MINI_05));
                stacks.add(new ItemStack(BROWN_WALL_ROAD_SIGN_MINI_06));
                stacks.add(new ItemStack(BROWN_WALL_ROAD_SIGN_MINI_07));
                stacks.add(new ItemStack(BROWN_WALL_ROAD_SIGN_MINI_08));
                stacks.add(new ItemStack(BROWN_WALL_ROAD_SIGN_MINI_09));
                stacks.add(new ItemStack(BROWN_WALL_ROAD_SIGN_MINI_10));
                stacks.add(new ItemStack(BROWN_WALL_ROAD_SIGN_MINI_11));
                stacks.add(new ItemStack(BROWN_WALL_ROAD_SIGN_MINI_12));
                stacks.add(new ItemStack(BROWN_WALL_ROAD_SIGN_MINI_13));
                stacks.add(new ItemStack(BROWN_WALL_ROAD_SIGN_MINI_14));
                stacks.add(new ItemStack(BROWN_WALL_ROAD_SIGN_MINI_15));
                stacks.add(new ItemStack(BROWN_WALL_ROAD_SIGN_MINI_16));
                stacks.add(new ItemStack(BROWN_WALL_ROAD_SIGN_MINI_17));
                stacks.add(new ItemStack(BROWN_WALL_ROAD_SIGN_MINI_18));
                stacks.add(new ItemStack(BROWN_WALL_ROAD_SIGN_MINI_19));

                // 迷你标志 - 橙色
                stacks.add(new ItemStack(ORANGE_WALL_ROAD_SIGN_MINI_01));
                stacks.add(new ItemStack(ORANGE_WALL_ROAD_SIGN_MINI_02));
                stacks.add(new ItemStack(ORANGE_WALL_ROAD_SIGN_MINI_03));
                stacks.add(new ItemStack(ORANGE_WALL_ROAD_SIGN_MINI_04));
                stacks.add(new ItemStack(ORANGE_WALL_ROAD_SIGN_MINI_05));
                stacks.add(new ItemStack(ORANGE_WALL_ROAD_SIGN_MINI_06));
                stacks.add(new ItemStack(ORANGE_WALL_ROAD_SIGN_MINI_07));
                stacks.add(new ItemStack(ORANGE_WALL_ROAD_SIGN_MINI_08));
                stacks.add(new ItemStack(ORANGE_WALL_ROAD_SIGN_MINI_09));
                stacks.add(new ItemStack(ORANGE_WALL_ROAD_SIGN_MINI_10));
                stacks.add(new ItemStack(ORANGE_WALL_ROAD_SIGN_MINI_11));
                stacks.add(new ItemStack(ORANGE_WALL_ROAD_SIGN_MINI_12));
                stacks.add(new ItemStack(ORANGE_WALL_ROAD_SIGN_MINI_13));
                stacks.add(new ItemStack(ORANGE_WALL_ROAD_SIGN_MINI_14));
                stacks.add(new ItemStack(ORANGE_WALL_ROAD_SIGN_MINI_15));
                stacks.add(new ItemStack(ORANGE_WALL_ROAD_SIGN_MINI_16));
                stacks.add(new ItemStack(ORANGE_WALL_ROAD_SIGN_MINI_17));
                stacks.add(new ItemStack(ORANGE_WALL_ROAD_SIGN_MINI_18));
                stacks.add(new ItemStack(ORANGE_WALL_ROAD_SIGN_MINI_19));

                stacks.add(new ItemStack(OVERHEAD_ROAD_EDGE_MARKER));
            })
            .build();

    /**
     * 支柱分类
     */
    public static ItemGroup getPillars() {
        return PILLARS;
    }

    private static final ItemGroup PILLARS = FabricItemGroupBuilder.create(
                    new Identifier(OcelotSignMod.MOD_ID, "pillars"))
            .icon(() -> new ItemStack(ModItems.PILLARS_ICON))
            .appendItems(stacks -> {
                stacks.add(new ItemStack(ROAD_PILLAR_FOUR_SIDES_A));
                stacks.add(new ItemStack(ROAD_PILLAR_FOUR_SIDES_A_TOP));
                stacks.add(new ItemStack(ROAD_PILLAR_THREE_SIDES_A_LEFT));
                stacks.add(new ItemStack(ROAD_PILLAR_THREE_SIDES_A_RIGHT));
                stacks.add(new ItemStack(ROAD_PILLAR_THREE_SIDES_A_TOP_LEFT));
                stacks.add(new ItemStack(ROAD_PILLAR_THREE_SIDES_A_TOP_RIGHT));
                stacks.add(new ItemStack(ROAD_PILLAR_HORIZONTAL_A));
                stacks.add(new ItemStack(ROAD_PILLAR_UPRIGHT_A));
                stacks.add(new ItemStack(ROAD_PILLAR_UPRIGHT_B_DOUBLE));
                stacks.add(new ItemStack(ROAD_PILLAR_UPRIGHT_B_LEFT));
                stacks.add(new ItemStack(ROAD_PILLAR_UPRIGHT_B_RIGHT));
                stacks.add(new ItemStack(ROAD_PILLAR_UPRIGHT_C));
                stacks.add(new ItemStack(ROAD_PILLAR_UPRIGHT_C_HALF));
                stacks.add(new ItemStack(ROAD_PILLAR_UPRIGHT_C_INCLINED_LEFT));
                stacks.add(new ItemStack(ROAD_PILLAR_UPRIGHT_C_INCLINED_RIGHT));
                stacks.add(new ItemStack(ROAD_PILLAR_FOUR_SIDES_D));
                stacks.add(new ItemStack(ROAD_PILLAR_FOUR_SIDES_D_LEFT));
                stacks.add(new ItemStack(ROAD_PILLAR_FOUR_SIDES_D_RIGHT));
                stacks.add(new ItemStack(ROAD_PILLAR_HORIZONTAL_D));
                stacks.add(new ItemStack(ROAD_PILLAR_HORIZONTAL_D_DOWN));
                stacks.add(new ItemStack(ROAD_PILLAR_HORIZONTAL_D_UP));

                stacks.add(new ItemStack(OcelotSignMod.CUSTOM_MODEL_BLOCK));
                stacks.add(new ItemStack(OcelotSignMod.MODEL_WAND));

                addMishangucTextCopyTool(stacks);
            })
            .build();

    /**
     * 向分类中添加 mishanguc 的文本复制工具（如果可用）。
     */
    private static void addMishangucTextCopyTool(List<ItemStack> stacks) {
        if (!MishangAccess.isAvailable()) {
            LOGGER.debug("Mishanguc 不可用；跳过 TEXT_COPY_TOOL");
            return;
        }

        Object tool = MishangAccess.getTextCopyTool();
        if (tool instanceof net.minecraft.item.Item item) {
            stacks.add(new ItemStack(item));
        }
    }

    public static void registerItemGroups() {
    }
}