package bklmc.ocelotsign.item;

import bklmc.ocelotsign.OcelotSignMod;
import bklmc.ocelotsign.block.ArrowBlocks;
import bklmc.ocelotsign.block.ArrowBlocksLarge;
import bklmc.ocelotsign.block.ArrowBlocksStyle2;
import bklmc.ocelotsign.block.ArrowBlocksStyle3;
import bklmc.ocelotsign.block.RoadSignBlocks;
import bklmc.ocelotsign.block.WallRoadSignBlocks;
import bklmc.ocelotsign.block.PillarBlocks;
import bklmc.ocelotsign.integration.mishanguc.MishangAccess;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
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

                // 蓝色线形诱导标志
                stacks.add(new ItemStack(BLUE_CHEVRON_ALIGNMENT_A_LEFT));
                stacks.add(new ItemStack(BLUE_CHEVRON_ALIGNMENT_A_RIGHT));
                stacks.add(new ItemStack(BLUE_CHEVRON_ALIGNMENT_B_LEFT));
                stacks.add(new ItemStack(BLUE_CHEVRON_ALIGNMENT_B_RIGHT));
                stacks.add(new ItemStack(BLUE_CHEVRON_ALIGNMENT_C_LEFT));
                stacks.add(new ItemStack(BLUE_CHEVRON_ALIGNMENT_C_RIGHT));
                stacks.add(new ItemStack(BLUE_CHEVRON_ALIGNMENT_C_UP));

                // 绿色线形诱导标志
                stacks.add(new ItemStack(GREEN_CHEVRON_ALIGNMENT_A_LEFT));
                stacks.add(new ItemStack(GREEN_CHEVRON_ALIGNMENT_A_RIGHT));
                stacks.add(new ItemStack(GREEN_CHEVRON_ALIGNMENT_B_LEFT));
                stacks.add(new ItemStack(GREEN_CHEVRON_ALIGNMENT_B_RIGHT));
                stacks.add(new ItemStack(GREEN_CHEVRON_ALIGNMENT_C_LEFT));
                stacks.add(new ItemStack(GREEN_CHEVRON_ALIGNMENT_C_RIGHT));
                stacks.add(new ItemStack(GREEN_CHEVRON_ALIGNMENT_C_UP));

                // 红色线形诱导标志
                stacks.add(new ItemStack(RED_CHEVRON_ALIGNMENT_A_LEFT));
                stacks.add(new ItemStack(RED_CHEVRON_ALIGNMENT_A_RIGHT));
                stacks.add(new ItemStack(RED_CHEVRON_ALIGNMENT_B_LEFT));
                stacks.add(new ItemStack(RED_CHEVRON_ALIGNMENT_B_RIGHT));
                stacks.add(new ItemStack(RED_CHEVRON_ALIGNMENT_C_LEFT));
                stacks.add(new ItemStack(RED_CHEVRON_ALIGNMENT_C_RIGHT));
                stacks.add(new ItemStack(RED_CHEVRON_ALIGNMENT_C_UP));

                // 黄色线形诱导标志
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
     * 道路标线分类（地面箭头类方块）
     */
    public static ItemGroup getRoadMarks() {
        return ROAD_MARKS;
    }

    private static final ItemGroup ROAD_MARKS = FabricItemGroupBuilder.create(
                    new Identifier(OcelotSignMod.MOD_ID, "road_marks"))
            .icon(() -> new ItemStack(ArrowBlocks.ADVANCE_ARROW_STRAIGHT))
            .appendItems(stacks -> {
                // mishanguc 自带的箭头标记方块
                addItem(stacks, "mishanguc", "arrow_left_mark");
                addItem(stacks, "mishanguc", "arrow_left_merge_mark");
                addItem(stacks, "mishanguc", "arrow_left_right_mark");
                addItem(stacks, "mishanguc", "arrow_left_uturn_mark");
                addItem(stacks, "mishanguc", "arrow_right_mark");
                addItem(stacks, "mishanguc", "arrow_right_merge_mark");
                addItem(stacks, "mishanguc", "arrow_right_uturn_mark");
                addItem(stacks, "mishanguc", "arrow_straight_mark");
                addItem(stacks, "mishanguc", "arrow_straight_left_mark");
                addItem(stacks, "mishanguc", "arrow_straight_left_right_mark");
                addItem(stacks, "mishanguc", "arrow_straight_right_mark");
                addItem(stacks, "mishanguc", "arrow_straight_uturn_left_mark");
                addItem(stacks, "mishanguc", "arrow_straight_uturn_right_mark");
                addItem(stacks, "mishanguc", "arrow_uturn_left_mark");
                addItem(stacks, "mishanguc", "arrow_uturn_right_mark");
                addItem(stacks, "mishanguc", "deceleration_double_line_mark");
                addItem(stacks, "mishanguc", "deceleration_triple_line_mark");
                addItem(stacks, "mishanguc", "deceleration_crossroads_mark");
                addItem(stacks, "mishanguc", "lane_disabled_mark");
                addItem(stacks, "mishanguc", "lane_non_vehicle_mark");

                // 普通箭头（ArrowBlocks）
                // 预告箭头
                stacks.add(new ItemStack(ArrowBlocks.ADVANCE_ARROW_LEFT));
                stacks.add(new ItemStack(ArrowBlocks.ADVANCE_ARROW_LEFT_RIGHT));
                stacks.add(new ItemStack(ArrowBlocks.ADVANCE_ARROW_LEFT_UTURN));
                stacks.add(new ItemStack(ArrowBlocks.ADVANCE_ARROW_RIGHT));
                stacks.add(new ItemStack(ArrowBlocks.ADVANCE_ARROW_RIGHT_UTURN));
                stacks.add(new ItemStack(ArrowBlocks.ADVANCE_ARROW_STRAIGHT));
                stacks.add(new ItemStack(ArrowBlocks.ADVANCE_ARROW_STRAIGHT_LEFT));
                stacks.add(new ItemStack(ArrowBlocks.ADVANCE_ARROW_STRAIGHT_LEFT_RIGHT));
                stacks.add(new ItemStack(ArrowBlocks.ADVANCE_ARROW_STRAIGHT_RIGHT));
                stacks.add(new ItemStack(ArrowBlocks.ADVANCE_ARROW_STRAIGHT_UTURN_LEFT));
                stacks.add(new ItemStack(ArrowBlocks.ADVANCE_ARROW_STRAIGHT_UTURN_RIGHT));
                stacks.add(new ItemStack(ArrowBlocks.ADVANCE_ARROW_UTURN_LEFT));
                stacks.add(new ItemStack(ArrowBlocks.ADVANCE_ARROW_UTURN_RIGHT));

                // 橙色箭头
                stacks.add(new ItemStack(ArrowBlocks.ORANGE_ARROW_LEFT));
                stacks.add(new ItemStack(ArrowBlocks.ORANGE_ARROW_LEFT_RIGHT));
                stacks.add(new ItemStack(ArrowBlocks.ORANGE_ARROW_LEFT_UTURN));
                stacks.add(new ItemStack(ArrowBlocks.ORANGE_ARROW_RIGHT));
                stacks.add(new ItemStack(ArrowBlocks.ORANGE_ARROW_RIGHT_UTURN));
                stacks.add(new ItemStack(ArrowBlocks.ORANGE_ARROW_STRAIGHT));
                stacks.add(new ItemStack(ArrowBlocks.ORANGE_ARROW_STRAIGHT_LEFT));
                stacks.add(new ItemStack(ArrowBlocks.ORANGE_ARROW_STRAIGHT_LEFT_RIGHT));
                stacks.add(new ItemStack(ArrowBlocks.ORANGE_ARROW_STRAIGHT_RIGHT));
                stacks.add(new ItemStack(ArrowBlocks.ORANGE_ARROW_STRAIGHT_UTURN_LEFT));
                stacks.add(new ItemStack(ArrowBlocks.ORANGE_ARROW_STRAIGHT_UTURN_RIGHT));
                stacks.add(new ItemStack(ArrowBlocks.ORANGE_ARROW_UTURN_LEFT));
                stacks.add(new ItemStack(ArrowBlocks.ORANGE_ARROW_UTURN_RIGHT));

                // 禁止箭头
                stacks.add(new ItemStack(ArrowBlocks.ARROW_PROHIBITED));
                stacks.add(new ItemStack(ArrowBlocks.PROHIBITED_ARROW_LEFT));
                stacks.add(new ItemStack(ArrowBlocks.PROHIBITED_ARROW_LEFT_RIGHT));
                stacks.add(new ItemStack(ArrowBlocks.PROHIBITED_ARROW_LEFT_UTURN));
                stacks.add(new ItemStack(ArrowBlocks.PROHIBITED_ARROW_RIGHT));
                stacks.add(new ItemStack(ArrowBlocks.PROHIBITED_ARROW_RIGHT_UTURN));
                stacks.add(new ItemStack(ArrowBlocks.PROHIBITED_ARROW_STRAIGHT));
                stacks.add(new ItemStack(ArrowBlocks.PROHIBITED_ARROW_STRAIGHT_LEFT));
                stacks.add(new ItemStack(ArrowBlocks.PROHIBITED_ARROW_STRAIGHT_LEFT_RIGHT));
                stacks.add(new ItemStack(ArrowBlocks.PROHIBITED_ARROW_STRAIGHT_RIGHT));
                stacks.add(new ItemStack(ArrowBlocks.PROHIBITED_ARROW_STRAIGHT_UTURN_LEFT));
                stacks.add(new ItemStack(ArrowBlocks.PROHIBITED_ARROW_STRAIGHT_UTURN_RIGHT));
                stacks.add(new ItemStack(ArrowBlocks.PROHIBITED_ARROW_UTURN_LEFT));
                stacks.add(new ItemStack(ArrowBlocks.PROHIBITED_ARROW_UTURN_RIGHT));

                // 其他标线
                stacks.add(new ItemStack(ArrowBlocks.SPEED_BUMP));
                stacks.add(new ItemStack(ArrowBlocks.YIELD));
                stacks.add(new ItemStack(ArrowBlocks.DISTANCE_CONFIRM));

                // 大型箭头（ArrowBlocksLarge）
                // 预告箭头
                stacks.add(new ItemStack(ArrowBlocksLarge.ADVANCE_ARROW_LEFT));
                stacks.add(new ItemStack(ArrowBlocksLarge.ADVANCE_ARROW_LEFT_RIGHT));
                stacks.add(new ItemStack(ArrowBlocksLarge.ADVANCE_ARROW_LEFT_UTURN));
                stacks.add(new ItemStack(ArrowBlocksLarge.ADVANCE_ARROW_RIGHT));
                stacks.add(new ItemStack(ArrowBlocksLarge.ADVANCE_ARROW_RIGHT_UTURN));
                stacks.add(new ItemStack(ArrowBlocksLarge.ADVANCE_ARROW_STRAIGHT));
                stacks.add(new ItemStack(ArrowBlocksLarge.ADVANCE_ARROW_STRAIGHT_LEFT));
                stacks.add(new ItemStack(ArrowBlocksLarge.ADVANCE_ARROW_STRAIGHT_LEFT_RIGHT));
                stacks.add(new ItemStack(ArrowBlocksLarge.ADVANCE_ARROW_STRAIGHT_RIGHT));
                stacks.add(new ItemStack(ArrowBlocksLarge.ADVANCE_ARROW_STRAIGHT_UTURN_LEFT));
                stacks.add(new ItemStack(ArrowBlocksLarge.ADVANCE_ARROW_STRAIGHT_UTURN_RIGHT));
                stacks.add(new ItemStack(ArrowBlocksLarge.ADVANCE_ARROW_UTURN_LEFT));
                stacks.add(new ItemStack(ArrowBlocksLarge.ADVANCE_ARROW_UTURN_RIGHT));

                // 普通箭头
                stacks.add(new ItemStack(ArrowBlocksLarge.ARROW_LEFT));
                stacks.add(new ItemStack(ArrowBlocksLarge.ARROW_LEFT_MERGE));
                stacks.add(new ItemStack(ArrowBlocksLarge.ARROW_LEFT_RIGHT));
                stacks.add(new ItemStack(ArrowBlocksLarge.ARROW_LEFT_UTURN));
                stacks.add(new ItemStack(ArrowBlocksLarge.ARROW_RIGHT));
                stacks.add(new ItemStack(ArrowBlocksLarge.ARROW_RIGHT_MERGE));
                stacks.add(new ItemStack(ArrowBlocksLarge.ARROW_RIGHT_UTURN));
                stacks.add(new ItemStack(ArrowBlocksLarge.ARROW_STRAIGHT));
                stacks.add(new ItemStack(ArrowBlocksLarge.ARROW_STRAIGHT_LEFT));
                stacks.add(new ItemStack(ArrowBlocksLarge.ARROW_STRAIGHT_LEFT_RIGHT));
                stacks.add(new ItemStack(ArrowBlocksLarge.ARROW_STRAIGHT_RIGHT));
                stacks.add(new ItemStack(ArrowBlocksLarge.ARROW_STRAIGHT_UTURN_LEFT));
                stacks.add(new ItemStack(ArrowBlocksLarge.ARROW_STRAIGHT_UTURN_RIGHT));
                stacks.add(new ItemStack(ArrowBlocksLarge.ARROW_UTURN_LEFT));
                stacks.add(new ItemStack(ArrowBlocksLarge.ARROW_UTURN_RIGHT));

                // 禁止箭头
                stacks.add(new ItemStack(ArrowBlocksLarge.ARROW_PROHIBITED));
                stacks.add(new ItemStack(ArrowBlocksLarge.PROHIBITED_ARROW_LEFT));
                stacks.add(new ItemStack(ArrowBlocksLarge.PROHIBITED_ARROW_LEFT_RIGHT));
                stacks.add(new ItemStack(ArrowBlocksLarge.PROHIBITED_ARROW_LEFT_UTURN));
                stacks.add(new ItemStack(ArrowBlocksLarge.PROHIBITED_ARROW_RIGHT));
                stacks.add(new ItemStack(ArrowBlocksLarge.PROHIBITED_ARROW_RIGHT_UTURN));
                stacks.add(new ItemStack(ArrowBlocksLarge.PROHIBITED_ARROW_STRAIGHT));
                stacks.add(new ItemStack(ArrowBlocksLarge.PROHIBITED_ARROW_STRAIGHT_LEFT));
                stacks.add(new ItemStack(ArrowBlocksLarge.PROHIBITED_ARROW_STRAIGHT_LEFT_RIGHT));
                stacks.add(new ItemStack(ArrowBlocksLarge.PROHIBITED_ARROW_STRAIGHT_RIGHT));
                stacks.add(new ItemStack(ArrowBlocksLarge.PROHIBITED_ARROW_STRAIGHT_UTURN_LEFT));
                stacks.add(new ItemStack(ArrowBlocksLarge.PROHIBITED_ARROW_STRAIGHT_UTURN_RIGHT));
                stacks.add(new ItemStack(ArrowBlocksLarge.PROHIBITED_ARROW_UTURN_LEFT));
                stacks.add(new ItemStack(ArrowBlocksLarge.PROHIBITED_ARROW_UTURN_RIGHT));

                // 橙色箭头
                stacks.add(new ItemStack(ArrowBlocksLarge.ORANGE_ARROW_LEFT));
                stacks.add(new ItemStack(ArrowBlocksLarge.ORANGE_ARROW_LEFT_RIGHT));
                stacks.add(new ItemStack(ArrowBlocksLarge.ORANGE_ARROW_LEFT_UTURN));
                stacks.add(new ItemStack(ArrowBlocksLarge.ORANGE_ARROW_RIGHT));
                stacks.add(new ItemStack(ArrowBlocksLarge.ORANGE_ARROW_RIGHT_UTURN));
                stacks.add(new ItemStack(ArrowBlocksLarge.ORANGE_ARROW_STRAIGHT));
                stacks.add(new ItemStack(ArrowBlocksLarge.ORANGE_ARROW_STRAIGHT_LEFT));
                stacks.add(new ItemStack(ArrowBlocksLarge.ORANGE_ARROW_STRAIGHT_LEFT_RIGHT));
                stacks.add(new ItemStack(ArrowBlocksLarge.ORANGE_ARROW_STRAIGHT_RIGHT));
                stacks.add(new ItemStack(ArrowBlocksLarge.ORANGE_ARROW_STRAIGHT_UTURN_LEFT));
                stacks.add(new ItemStack(ArrowBlocksLarge.ORANGE_ARROW_STRAIGHT_UTURN_RIGHT));
                stacks.add(new ItemStack(ArrowBlocksLarge.ORANGE_ARROW_UTURN_LEFT));
                stacks.add(new ItemStack(ArrowBlocksLarge.ORANGE_ARROW_UTURN_RIGHT));

                // 其他标线
                stacks.add(new ItemStack(ArrowBlocksLarge.DECELERATION_CROSSROADS));
                stacks.add(new ItemStack(ArrowBlocksLarge.SPEED_BUMP));
                stacks.add(new ItemStack(ArrowBlocksLarge.YIELD));
                stacks.add(new ItemStack(ArrowBlocksLarge.DISTANCE_CONFIRM));

                // 样式二箭头（ArrowBlocksStyle2）
                // 预告箭头
                stacks.add(new ItemStack(ArrowBlocksStyle2.ADVANCE_ARROW_LEFT));
                stacks.add(new ItemStack(ArrowBlocksStyle2.ADVANCE_ARROW_LEFT_RIGHT));
                stacks.add(new ItemStack(ArrowBlocksStyle2.ADVANCE_ARROW_RIGHT));
                stacks.add(new ItemStack(ArrowBlocksStyle2.ADVANCE_ARROW_STRAIGHT));
                stacks.add(new ItemStack(ArrowBlocksStyle2.ADVANCE_ARROW_STRAIGHT_LEFT));
                stacks.add(new ItemStack(ArrowBlocksStyle2.ADVANCE_ARROW_STRAIGHT_LEFT_RIGHT));
                stacks.add(new ItemStack(ArrowBlocksStyle2.ADVANCE_ARROW_STRAIGHT_RIGHT));
                stacks.add(new ItemStack(ArrowBlocksStyle2.ADVANCE_ARROW_UTURN_LEFT));
                stacks.add(new ItemStack(ArrowBlocksStyle2.ADVANCE_ARROW_UTURN_RIGHT));

                // 普通箭头
                stacks.add(new ItemStack(ArrowBlocksStyle2.ARROW_LEFT));
                stacks.add(new ItemStack(ArrowBlocksStyle2.ARROW_LEFT_RIGHT));
                stacks.add(new ItemStack(ArrowBlocksStyle2.ARROW_PROHIBITED));
                stacks.add(new ItemStack(ArrowBlocksStyle2.ARROW_RIGHT));
                stacks.add(new ItemStack(ArrowBlocksStyle2.ARROW_STRAIGHT));
                stacks.add(new ItemStack(ArrowBlocksStyle2.ARROW_STRAIGHT_LEFT));
                stacks.add(new ItemStack(ArrowBlocksStyle2.ARROW_STRAIGHT_LEFT_RIGHT));
                stacks.add(new ItemStack(ArrowBlocksStyle2.ARROW_STRAIGHT_RIGHT));
                stacks.add(new ItemStack(ArrowBlocksStyle2.ARROW_UTURN_LEFT));
                stacks.add(new ItemStack(ArrowBlocksStyle2.ARROW_UTURN_RIGHT));
                stacks.add(new ItemStack(ArrowBlocksStyle2.ARROW_DOUBLE_LEFT));
                stacks.add(new ItemStack(ArrowBlocksStyle2.ARROW_DOUBLE_RIGHT));

                // 禁止箭头
                stacks.add(new ItemStack(ArrowBlocksStyle2.PROHIBITED_ARROW_LEFT));
                stacks.add(new ItemStack(ArrowBlocksStyle2.PROHIBITED_ARROW_LEFT_RIGHT));
                stacks.add(new ItemStack(ArrowBlocksStyle2.PROHIBITED_ARROW_RIGHT));
                stacks.add(new ItemStack(ArrowBlocksStyle2.PROHIBITED_ARROW_STRAIGHT));
                stacks.add(new ItemStack(ArrowBlocksStyle2.PROHIBITED_ARROW_STRAIGHT_LEFT));
                stacks.add(new ItemStack(ArrowBlocksStyle2.PROHIBITED_ARROW_STRAIGHT_LEFT_RIGHT));
                stacks.add(new ItemStack(ArrowBlocksStyle2.PROHIBITED_ARROW_STRAIGHT_RIGHT));
                stacks.add(new ItemStack(ArrowBlocksStyle2.PROHIBITED_ARROW_UTURN_LEFT));
                stacks.add(new ItemStack(ArrowBlocksStyle2.PROHIBITED_ARROW_UTURN_RIGHT));
                stacks.add(new ItemStack(ArrowBlocksStyle2.PROHIBITED_ARROW_DOUBLE_LEFT));
                stacks.add(new ItemStack(ArrowBlocksStyle2.PROHIBITED_ARROW_DOUBLE_RIGHT));

                // 橙色箭头
                stacks.add(new ItemStack(ArrowBlocksStyle2.ORANGE_ARROW_LEFT));
                stacks.add(new ItemStack(ArrowBlocksStyle2.ORANGE_ARROW_LEFT_RIGHT));
                stacks.add(new ItemStack(ArrowBlocksStyle2.ORANGE_ARROW_RIGHT));
                stacks.add(new ItemStack(ArrowBlocksStyle2.ORANGE_ARROW_STRAIGHT));
                stacks.add(new ItemStack(ArrowBlocksStyle2.ORANGE_ARROW_STRAIGHT_LEFT));
                stacks.add(new ItemStack(ArrowBlocksStyle2.ORANGE_ARROW_STRAIGHT_LEFT_RIGHT));
                stacks.add(new ItemStack(ArrowBlocksStyle2.ORANGE_ARROW_STRAIGHT_RIGHT));
                stacks.add(new ItemStack(ArrowBlocksStyle2.ORANGE_ARROW_UTURN_LEFT));
                stacks.add(new ItemStack(ArrowBlocksStyle2.ORANGE_ARROW_UTURN_RIGHT));
                stacks.add(new ItemStack(ArrowBlocksStyle2.ORANGE_ARROW_DOUBLE_LEFT));
                stacks.add(new ItemStack(ArrowBlocksStyle2.ORANGE_ARROW_DOUBLE_RIGHT));

                // 样式三箭头（ArrowBlocksStyle3）
                // 普通箭头
                stacks.add(new ItemStack(ArrowBlocksStyle3.ARROW_LEFT));
                stacks.add(new ItemStack(ArrowBlocksStyle3.ARROW_RIGHT));
                stacks.add(new ItemStack(ArrowBlocksStyle3.ARROW_STRAIGHT));
                stacks.add(new ItemStack(ArrowBlocksStyle3.ARROW_UTURN_LEFT));
                stacks.add(new ItemStack(ArrowBlocksStyle3.ARROW_UTURN_RIGHT));
                stacks.add(new ItemStack(ArrowBlocksStyle3.ARROW_LEFT_MERGE));
                stacks.add(new ItemStack(ArrowBlocksStyle3.ARROW_RIGHT_MERGE));
                stacks.add(new ItemStack(ArrowBlocksStyle3.ARROW_LEFT_UTURN));
                stacks.add(new ItemStack(ArrowBlocksStyle3.ARROW_RIGHT_UTURN));
                stacks.add(new ItemStack(ArrowBlocksStyle3.ARROW_LEFT_RIGHT));
                stacks.add(new ItemStack(ArrowBlocksStyle3.ARROW_STRAIGHT_LEFT));
                stacks.add(new ItemStack(ArrowBlocksStyle3.ARROW_STRAIGHT_RIGHT));
                stacks.add(new ItemStack(ArrowBlocksStyle3.ARROW_STRAIGHT_LEFT_RIGHT));
                stacks.add(new ItemStack(ArrowBlocksStyle3.ARROW_PROHIBITED));

                // 禁止箭头
                stacks.add(new ItemStack(ArrowBlocksStyle3.PROHIBITED_ARROW_LEFT));
                stacks.add(new ItemStack(ArrowBlocksStyle3.PROHIBITED_ARROW_RIGHT));
                stacks.add(new ItemStack(ArrowBlocksStyle3.PROHIBITED_ARROW_STRAIGHT));
                stacks.add(new ItemStack(ArrowBlocksStyle3.PROHIBITED_ARROW_UTURN_LEFT));
                stacks.add(new ItemStack(ArrowBlocksStyle3.PROHIBITED_ARROW_UTURN_RIGHT));
                stacks.add(new ItemStack(ArrowBlocksStyle3.PROHIBITED_ARROW_LEFT_RIGHT));
                stacks.add(new ItemStack(ArrowBlocksStyle3.PROHIBITED_ARROW_STRAIGHT_LEFT));
                stacks.add(new ItemStack(ArrowBlocksStyle3.PROHIBITED_ARROW_STRAIGHT_RIGHT));
                stacks.add(new ItemStack(ArrowBlocksStyle3.PROHIBITED_ARROW_STRAIGHT_LEFT_RIGHT));
                stacks.add(new ItemStack(ArrowBlocksStyle3.PROHIBITED_ARROW_LEFT_UTURN));
                stacks.add(new ItemStack(ArrowBlocksStyle3.PROHIBITED_ARROW_RIGHT_UTURN));

                // 橙色箭头
                stacks.add(new ItemStack(ArrowBlocksStyle3.ORANGE_ARROW_LEFT));
                stacks.add(new ItemStack(ArrowBlocksStyle3.ORANGE_ARROW_RIGHT));
                stacks.add(new ItemStack(ArrowBlocksStyle3.ORANGE_ARROW_STRAIGHT));
                stacks.add(new ItemStack(ArrowBlocksStyle3.ORANGE_ARROW_UTURN_LEFT));
                stacks.add(new ItemStack(ArrowBlocksStyle3.ORANGE_ARROW_UTURN_RIGHT));
                stacks.add(new ItemStack(ArrowBlocksStyle3.ORANGE_ARROW_LEFT_MERGE));
                stacks.add(new ItemStack(ArrowBlocksStyle3.ORANGE_ARROW_RIGHT_MERGE));
                stacks.add(new ItemStack(ArrowBlocksStyle3.ORANGE_ARROW_LEFT_UTURN));
                stacks.add(new ItemStack(ArrowBlocksStyle3.ORANGE_ARROW_RIGHT_UTURN));
                stacks.add(new ItemStack(ArrowBlocksStyle3.ORANGE_ARROW_LEFT_RIGHT));
                stacks.add(new ItemStack(ArrowBlocksStyle3.ORANGE_ARROW_STRAIGHT_LEFT));
                stacks.add(new ItemStack(ArrowBlocksStyle3.ORANGE_ARROW_STRAIGHT_RIGHT));
                stacks.add(new ItemStack(ArrowBlocksStyle3.ORANGE_ARROW_STRAIGHT_LEFT_RIGHT));
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

    /**
     * 如果指定 ID 的物品已注册，则向分类中添加其默认 {@link ItemStack}。
     */
    private static void addItem(List<ItemStack> stacks, String namespace, String id) {
        Item item = Registry.ITEM.get(new Identifier(namespace, id));
        if (item != null) {
            stacks.add(new ItemStack(item));
        } else {
            LOGGER.debug("物品不可用，跳过: {}:{}", namespace, id);
        }
    }

    public static void registerItemGroups() {
        // 只把 BlockItem 注册到 Registry.ITEM；具体分类由 ROAD_MARKS 通过 appendItems 显式列举
        ArrowBlocks.registerBlockItems();
        ArrowBlocksLarge.registerBlockItems();
        ArrowBlocksStyle2.registerBlockItems();
        ArrowBlocksStyle3.registerBlockItems();
    }
}