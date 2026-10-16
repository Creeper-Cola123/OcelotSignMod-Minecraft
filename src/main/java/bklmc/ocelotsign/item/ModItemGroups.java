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
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registry;
import net.minecraft.registry.Registries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static bklmc.ocelotsign.block.RoadSignBlocks.*;
import static bklmc.ocelotsign.block.WallRoadSignBlocks.*;
import static bklmc.ocelotsign.block.PillarBlocks.*;

/**
 * 创建路标物品分类
 *
 * @see ModItems
 */
public class ModItemGroups {
    private static final Logger LOGGER = LoggerFactory.getLogger(ModItemGroups.class);

    /**
     * 创建路标分类
     */
    public static ItemGroup getRoadSigns() {
        return ROAD_SIGNS;
    }

    private static final ItemGroup ROAD_SIGNS = FabricItemGroup.builder(
                    new Identifier(OcelotSignMod.MOD_ID, "road_signs"))
            .icon(() -> new ItemStack(ModItems.ROAD_SIGNS_ICON))
            .entries((displayContext, stacks) -> {
                // 蓝色路标
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

                // 绿色路标
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

                // 黄色路标
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

                // 白色路标
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

                // 棕色路标
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

                // 橙色路标
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
     * 墙上路标分类
     */
    public static ItemGroup getWallRoadSigns() {
        return WALL_ROAD_SIGNS;
    }

    private static final ItemGroup WALL_ROAD_SIGNS = FabricItemGroup.builder(
                    new Identifier(OcelotSignMod.MOD_ID, "wall_road_signs"))
            .icon(() -> new ItemStack(ModItems.WALL_ROAD_SIGNS_ICON))
            .entries((displayContext, stacks) -> {
                // 蓝色墙上路标类型
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

                // 绿色墙上路标类型
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

                // 黄色墙上路标类型
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

                // 白色墙上路标类型
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

                // 棕色墙上路标类型
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

                // 橙色墙上路标类型
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

                // 禁止标志
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

                // 蓝色箭头指示
                stacks.add(new ItemStack(BLUE_CHEVRON_ALIGNMENT_A_LEFT));
                stacks.add(new ItemStack(BLUE_CHEVRON_ALIGNMENT_A_RIGHT));
                stacks.add(new ItemStack(BLUE_CHEVRON_ALIGNMENT_B_LEFT));
                stacks.add(new ItemStack(BLUE_CHEVRON_ALIGNMENT_B_RIGHT));
                stacks.add(new ItemStack(BLUE_CHEVRON_ALIGNMENT_C_LEFT));
                stacks.add(new ItemStack(BLUE_CHEVRON_ALIGNMENT_C_RIGHT));
                stacks.add(new ItemStack(BLUE_CHEVRON_ALIGNMENT_C_UP));

                // 绿色箭头指示
                stacks.add(new ItemStack(GREEN_CHEVRON_ALIGNMENT_A_LEFT));
                stacks.add(new ItemStack(GREEN_CHEVRON_ALIGNMENT_A_RIGHT));
                stacks.add(new ItemStack(GREEN_CHEVRON_ALIGNMENT_B_LEFT));
                stacks.add(new ItemStack(GREEN_CHEVRON_ALIGNMENT_B_RIGHT));
                stacks.add(new ItemStack(GREEN_CHEVRON_ALIGNMENT_C_LEFT));
                stacks.add(new ItemStack(GREEN_CHEVRON_ALIGNMENT_C_RIGHT));
                stacks.add(new ItemStack(GREEN_CHEVRON_ALIGNMENT_C_UP));

                // 红色箭头指示
                stacks.add(new ItemStack(RED_CHEVRON_ALIGNMENT_A_LEFT));
                stacks.add(new ItemStack(RED_CHEVRON_ALIGNMENT_A_RIGHT));
                stacks.add(new ItemStack(RED_CHEVRON_ALIGNMENT_B_LEFT));
                stacks.add(new ItemStack(RED_CHEVRON_ALIGNMENT_B_RIGHT));
                stacks.add(new ItemStack(RED_CHEVRON_ALIGNMENT_C_LEFT));
                stacks.add(new ItemStack(RED_CHEVRON_ALIGNMENT_C_RIGHT));
                stacks.add(new ItemStack(RED_CHEVRON_ALIGNMENT_C_UP));

                // 黄色箭头指示
                stacks.add(new ItemStack(YELLOW_CHEVRON_ALIGNMENT_A_LEFT));
                stacks.add(new ItemStack(YELLOW_CHEVRON_ALIGNMENT_A_RIGHT));
                stacks.add(new ItemStack(YELLOW_CHEVRON_ALIGNMENT_B_LEFT));
                stacks.add(new ItemStack(YELLOW_CHEVRON_ALIGNMENT_B_RIGHT));
                stacks.add(new ItemStack(YELLOW_CHEVRON_ALIGNMENT_C_LEFT));
                stacks.add(new ItemStack(YELLOW_CHEVRON_ALIGNMENT_C_RIGHT));
                stacks.add(new ItemStack(YELLOW_CHEVRON_ALIGNMENT_C_UP));

                // 迷你墙上路标 - 蓝色
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

                // 迷你墙上路标 - 绿色
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

                // 迷你墙上路标 - 黄色
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

                // 迷你墙上路标 - 白色
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

                // 迷你墙上路标 - 棕色
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

                // 迷你墙上路标 - 橙色
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
     * 获取物品分组
     */
    public static ItemGroup getPillars() {
        return PILLARS;
    }

    private static final ItemGroup PILLARS = FabricItemGroup.builder(
                    new Identifier(OcelotSignMod.MOD_ID, "pillars"))
            .icon(() -> new ItemStack(ModItems.PILLARS_ICON))
            .entries((displayContext, stacks) -> {
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
     * 道路标线分类
     */
    public static ItemGroup getRoadMarks() {
        return ROAD_MARKS;
    }

    private static final ItemGroup ROAD_MARKS = FabricItemGroup.builder(
                    new Identifier(OcelotSignMod.MOD_ID, "road_marks"))
            .icon(() -> new ItemStack(ArrowBlocks.ADVANCE_ARROW_STRAIGHT))
            .entries((displayContext, stacks) -> {
                // mishanguc 减速标记
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

                // 预警箭头直连
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

                // 橙色箭头直连
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

                // 禁止箭头直连
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

                // 减速标记
                stacks.add(new ItemStack(ArrowBlocks.SPEED_BUMP));
                stacks.add(new ItemStack(ArrowBlocks.YIELD));
                stacks.add(new ItemStack(ArrowBlocks.DISTANCE_CONFIRM));

                // 预警箭头直连来自ArrowBlocksLarge
                // 预警箭头直连
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

                // 路面箭头
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

                // 禁止箭头直连
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

                // 橙色箭头直连
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

                // 减速标记
                stacks.add(new ItemStack(ArrowBlocksLarge.DECELERATION_CROSSROADS));
                stacks.add(new ItemStack(ArrowBlocksLarge.SPEED_BUMP));
                stacks.add(new ItemStack(ArrowBlocksLarge.YIELD));
                stacks.add(new ItemStack(ArrowBlocksLarge.DISTANCE_CONFIRM));

                // 箭头变体样式来自ArrowBlocksStyle2
                // 预警箭头直连
                stacks.add(new ItemStack(ArrowBlocksStyle2.ADVANCE_ARROW_LEFT));
                stacks.add(new ItemStack(ArrowBlocksStyle2.ADVANCE_ARROW_LEFT_RIGHT));
                stacks.add(new ItemStack(ArrowBlocksStyle2.ADVANCE_ARROW_RIGHT));
                stacks.add(new ItemStack(ArrowBlocksStyle2.ADVANCE_ARROW_STRAIGHT));
                stacks.add(new ItemStack(ArrowBlocksStyle2.ADVANCE_ARROW_STRAIGHT_LEFT));
                stacks.add(new ItemStack(ArrowBlocksStyle2.ADVANCE_ARROW_STRAIGHT_LEFT_RIGHT));
                stacks.add(new ItemStack(ArrowBlocksStyle2.ADVANCE_ARROW_STRAIGHT_RIGHT));
                stacks.add(new ItemStack(ArrowBlocksStyle2.ADVANCE_ARROW_UTURN_LEFT));
                stacks.add(new ItemStack(ArrowBlocksStyle2.ADVANCE_ARROW_UTURN_RIGHT));

                // 路面箭头
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

                // 禁止箭头直连
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

                // 橙色箭头直连
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

                // 箭头变体样式来自ArrowBlocksStyle3
                // 路面箭头
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

                // 禁止箭头直连
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

                // 橙色箭头直连
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
     * mishanguc的文本复制工具类，用于在创意模式物品栏中添加mishanguc的文本复制工具物品
     */
    private static void addMishangucTextCopyTool(net.minecraft.item.ItemGroup.Entries stacks) {
        if (!MishangAccess.isAvailable()) {
            LOGGER.debug("Mishanguc 文本复制工具不可用");
            return;
        }

        Object tool = MishangAccess.getTextCopyTool();
        if (tool instanceof net.minecraft.item.Item item) {
            stacks.add(new ItemStack(item));
        }
    }

    /**
     * 根据命名空间和ID获取物品，并添加到物品栏中
     *
     * @param stacks 物品栏条目
     * @param namespace 命名空间
     * @param id 物品ID
     */
    private static void addItem(net.minecraft.item.ItemGroup.Entries stacks, String namespace, String id) {
        Item item = Registries.ITEM.get(new Identifier(namespace, id));
        if (item != null) {
            stacks.add(new ItemStack(item));
        } else {
            LOGGER.debug("创建物品失败：{}:{}", namespace, id);
        }
    }

    public static void registerItemGroups() {
        // 使用BlockItem在Registry.ITEM中注册ArrowBlocks的条目
        ArrowBlocks.registerBlockItems();
        ArrowBlocksLarge.registerBlockItems();
        ArrowBlocksStyle2.registerBlockItems();
        ArrowBlocksStyle3.registerBlockItems();
    }
}
