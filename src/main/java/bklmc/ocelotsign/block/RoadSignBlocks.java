package bklmc.ocelotsign.block;

import bklmc.ocelotsign.OcelotSignMod;
import bklmc.ocelotsign.block.custom.RoadSignBlock;
import bklmc.ocelotsign.item.RoadSignBlockItem;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;

/**
 * 立式道路指示牌方块注册中心
 *
 * @see bklmc.ocelotsign.block.custom.RoadSignBlock
 */
public class RoadSignBlocks {
    public static final Block BLUE_ROAD_SIGN_LEFT_TOP = registerWithItem("roadsign/blue/left_top", new RoadSignBlock(Blocks.BLUE_CONCRETE, FabricBlockSettings.copyOf(Blocks.BLUE_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block BLUE_ROAD_SIGN_TOP = registerWithItem("roadsign/blue/top", new RoadSignBlock(Blocks.BLUE_CONCRETE, FabricBlockSettings.copyOf(Blocks.BLUE_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block BLUE_ROAD_SIGN_RIGHT_TOP = registerWithItem("roadsign/blue/right_top", new RoadSignBlock(Blocks.BLUE_CONCRETE, FabricBlockSettings.copyOf(Blocks.BLUE_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block BLUE_ROAD_SIGN_LEFT = registerWithItem("roadsign/blue/left", new RoadSignBlock(Blocks.BLUE_CONCRETE, FabricBlockSettings.copyOf(Blocks.BLUE_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block BLUE_ROAD_SIGN_MIDDLE = registerWithItem("roadsign/blue/middle", new RoadSignBlock(Blocks.BLUE_CONCRETE, FabricBlockSettings.copyOf(Blocks.BLUE_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block BLUE_ROAD_SIGN_RIGHT = registerWithItem("roadsign/blue/right", new RoadSignBlock(Blocks.BLUE_CONCRETE, FabricBlockSettings.copyOf(Blocks.BLUE_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block BLUE_ROAD_SIGN_LEFT_BOTTOM = registerWithItem("roadsign/blue/left_bottom", new RoadSignBlock(Blocks.BLUE_CONCRETE, FabricBlockSettings.copyOf(Blocks.BLUE_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block BLUE_ROAD_SIGN_BOTTOM = registerWithItem("roadsign/blue/bottom", new RoadSignBlock(Blocks.BLUE_CONCRETE, FabricBlockSettings.copyOf(Blocks.BLUE_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block BLUE_ROAD_SIGN_RIGHT_BOTTOM = registerWithItem("roadsign/blue/right_bottom", new RoadSignBlock(Blocks.BLUE_CONCRETE, FabricBlockSettings.copyOf(Blocks.BLUE_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block BLUE_ROAD_SIGN_HORIZONTAL_LEFT = registerWithItem("roadsign/blue/horizontal_left", new RoadSignBlock(Blocks.BLUE_CONCRETE, FabricBlockSettings.copyOf(Blocks.BLUE_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block BLUE_ROAD_SIGN_HORIZONTAL_MIDDLE = registerWithItem("roadsign/blue/horizontal_middle", new RoadSignBlock(Blocks.BLUE_CONCRETE, FabricBlockSettings.copyOf(Blocks.BLUE_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block BLUE_ROAD_SIGN_HORIZONTAL_RIGHT = registerWithItem("roadsign/blue/horizontal_right", new RoadSignBlock(Blocks.BLUE_CONCRETE, FabricBlockSettings.copyOf(Blocks.BLUE_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block BLUE_ROAD_SIGN_VERTICAL_TOP = registerWithItem("roadsign/blue/vertical_top", new RoadSignBlock(Blocks.BLUE_CONCRETE, FabricBlockSettings.copyOf(Blocks.BLUE_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block BLUE_ROAD_SIGN_VERTICAL_MIDDLE = registerWithItem("roadsign/blue/vertical_middle", new RoadSignBlock(Blocks.BLUE_CONCRETE, FabricBlockSettings.copyOf(Blocks.BLUE_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block BLUE_ROAD_SIGN_VERTICAL_BOTTOM = registerWithItem("roadsign/blue/vertical_bottom", new RoadSignBlock(Blocks.BLUE_CONCRETE, FabricBlockSettings.copyOf(Blocks.BLUE_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block BLUE_ROAD_SIGN_SMALL_A = registerWithItem("roadsign/blue/small_a", new RoadSignBlock(Blocks.BLUE_CONCRETE, FabricBlockSettings.copyOf(Blocks.BLUE_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block BLUE_ROAD_SIGN_SMALL_B = registerWithItem("roadsign/blue/small_b", new RoadSignBlock(Blocks.BLUE_CONCRETE, FabricBlockSettings.copyOf(Blocks.BLUE_CONCRETE).strength(0.2f,0.2f).nonOpaque()));


    public static final Block GREEN_ROAD_SIGN_LEFT_TOP = registerWithItem("roadsign/green/left_top", new RoadSignBlock(Blocks.GREEN_CONCRETE, FabricBlockSettings.copyOf(Blocks.GREEN_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block GREEN_ROAD_SIGN_TOP = registerWithItem("roadsign/green/top", new RoadSignBlock(Blocks.GREEN_CONCRETE, FabricBlockSettings.copyOf(Blocks.GREEN_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block GREEN_ROAD_SIGN_RIGHT_TOP = registerWithItem("roadsign/green/right_top", new RoadSignBlock(Blocks.GREEN_CONCRETE, FabricBlockSettings.copyOf(Blocks.GREEN_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block GREEN_ROAD_SIGN_LEFT = registerWithItem("roadsign/green/left", new RoadSignBlock(Blocks.GREEN_CONCRETE, FabricBlockSettings.copyOf(Blocks.GREEN_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block GREEN_ROAD_SIGN_MIDDLE = registerWithItem("roadsign/green/middle", new RoadSignBlock(Blocks.GREEN_CONCRETE, FabricBlockSettings.copyOf(Blocks.GREEN_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block GREEN_ROAD_SIGN_RIGHT = registerWithItem("roadsign/green/right", new RoadSignBlock(Blocks.GREEN_CONCRETE, FabricBlockSettings.copyOf(Blocks.GREEN_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block GREEN_ROAD_SIGN_LEFT_BOTTOM = registerWithItem("roadsign/green/left_bottom", new RoadSignBlock(Blocks.GREEN_CONCRETE, FabricBlockSettings.copyOf(Blocks.GREEN_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block GREEN_ROAD_SIGN_BOTTOM = registerWithItem("roadsign/green/bottom", new RoadSignBlock(Blocks.GREEN_CONCRETE, FabricBlockSettings.copyOf(Blocks.GREEN_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block GREEN_ROAD_SIGN_RIGHT_BOTTOM = registerWithItem("roadsign/green/right_bottom", new RoadSignBlock(Blocks.GREEN_CONCRETE, FabricBlockSettings.copyOf(Blocks.GREEN_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block GREEN_ROAD_SIGN_HORIZONTAL_LEFT = registerWithItem("roadsign/green/horizontal_left", new RoadSignBlock(Blocks.GREEN_CONCRETE, FabricBlockSettings.copyOf(Blocks.GREEN_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block GREEN_ROAD_SIGN_HORIZONTAL_MIDDLE = registerWithItem("roadsign/green/horizontal_middle", new RoadSignBlock(Blocks.GREEN_CONCRETE, FabricBlockSettings.copyOf(Blocks.GREEN_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block GREEN_ROAD_SIGN_HORIZONTAL_RIGHT = registerWithItem("roadsign/green/horizontal_right", new RoadSignBlock(Blocks.GREEN_CONCRETE, FabricBlockSettings.copyOf(Blocks.GREEN_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block GREEN_ROAD_SIGN_VERTICAL_TOP = registerWithItem("roadsign/green/vertical_top", new RoadSignBlock(Blocks.GREEN_CONCRETE, FabricBlockSettings.copyOf(Blocks.GREEN_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block GREEN_ROAD_SIGN_VERTICAL_MIDDLE = registerWithItem("roadsign/green/vertical_middle", new RoadSignBlock(Blocks.GREEN_CONCRETE, FabricBlockSettings.copyOf(Blocks.GREEN_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block GREEN_ROAD_SIGN_VERTICAL_BOTTOM = registerWithItem("roadsign/green/vertical_bottom", new RoadSignBlock(Blocks.GREEN_CONCRETE, FabricBlockSettings.copyOf(Blocks.GREEN_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block GREEN_ROAD_SIGN_SMALL_A = registerWithItem("roadsign/green/small_a", new RoadSignBlock(Blocks.GREEN_CONCRETE, FabricBlockSettings.copyOf(Blocks.GREEN_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block GREEN_ROAD_SIGN_SMALL_B = registerWithItem("roadsign/green/small_b", new RoadSignBlock(Blocks.GREEN_CONCRETE, FabricBlockSettings.copyOf(Blocks.GREEN_CONCRETE).strength(0.2f,0.2f).nonOpaque()));

    public static final Block YELLOW_ROAD_SIGN_LEFT_TOP = registerWithItem("roadsign/yellow/left_top", new RoadSignBlock(Blocks.YELLOW_CONCRETE, FabricBlockSettings.copyOf(Blocks.YELLOW_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block YELLOW_ROAD_SIGN_TOP = registerWithItem("roadsign/yellow/top", new RoadSignBlock(Blocks.YELLOW_CONCRETE, FabricBlockSettings.copyOf(Blocks.YELLOW_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block YELLOW_ROAD_SIGN_RIGHT_TOP = registerWithItem("roadsign/yellow/right_top", new RoadSignBlock(Blocks.YELLOW_CONCRETE, FabricBlockSettings.copyOf(Blocks.YELLOW_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block YELLOW_ROAD_SIGN_LEFT = registerWithItem("roadsign/yellow/left", new RoadSignBlock(Blocks.YELLOW_CONCRETE, FabricBlockSettings.copyOf(Blocks.YELLOW_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block YELLOW_ROAD_SIGN_MIDDLE = registerWithItem("roadsign/yellow/middle", new RoadSignBlock(Blocks.YELLOW_CONCRETE, FabricBlockSettings.copyOf(Blocks.YELLOW_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block YELLOW_ROAD_SIGN_RIGHT = registerWithItem("roadsign/yellow/right", new RoadSignBlock(Blocks.YELLOW_CONCRETE, FabricBlockSettings.copyOf(Blocks.YELLOW_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block YELLOW_ROAD_SIGN_LEFT_BOTTOM = registerWithItem("roadsign/yellow/left_bottom", new RoadSignBlock(Blocks.YELLOW_CONCRETE, FabricBlockSettings.copyOf(Blocks.YELLOW_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block YELLOW_ROAD_SIGN_BOTTOM = registerWithItem("roadsign/yellow/bottom", new RoadSignBlock(Blocks.YELLOW_CONCRETE, FabricBlockSettings.copyOf(Blocks.YELLOW_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block YELLOW_ROAD_SIGN_RIGHT_BOTTOM = registerWithItem("roadsign/yellow/right_bottom", new RoadSignBlock(Blocks.YELLOW_CONCRETE, FabricBlockSettings.copyOf(Blocks.YELLOW_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block YELLOW_ROAD_SIGN_HORIZONTAL_LEFT = registerWithItem("roadsign/yellow/horizontal_left", new RoadSignBlock(Blocks.YELLOW_CONCRETE, FabricBlockSettings.copyOf(Blocks.YELLOW_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block YELLOW_ROAD_SIGN_HORIZONTAL_MIDDLE = registerWithItem("roadsign/yellow/horizontal_middle", new RoadSignBlock(Blocks.YELLOW_CONCRETE, FabricBlockSettings.copyOf(Blocks.YELLOW_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block YELLOW_ROAD_SIGN_HORIZONTAL_RIGHT = registerWithItem("roadsign/yellow/horizontal_right", new RoadSignBlock(Blocks.YELLOW_CONCRETE, FabricBlockSettings.copyOf(Blocks.YELLOW_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block YELLOW_ROAD_SIGN_VERTICAL_TOP = registerWithItem("roadsign/yellow/vertical_top", new RoadSignBlock(Blocks.YELLOW_CONCRETE, FabricBlockSettings.copyOf(Blocks.YELLOW_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block YELLOW_ROAD_SIGN_VERTICAL_MIDDLE = registerWithItem("roadsign/yellow/vertical_middle", new RoadSignBlock(Blocks.YELLOW_CONCRETE, FabricBlockSettings.copyOf(Blocks.YELLOW_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block YELLOW_ROAD_SIGN_VERTICAL_BOTTOM = registerWithItem("roadsign/yellow/vertical_bottom", new RoadSignBlock(Blocks.YELLOW_CONCRETE, FabricBlockSettings.copyOf(Blocks.YELLOW_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block YELLOW_ROAD_SIGN_SMALL_A = registerWithItem("roadsign/yellow/small_a", new RoadSignBlock(Blocks.YELLOW_CONCRETE, FabricBlockSettings.copyOf(Blocks.YELLOW_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block YELLOW_ROAD_SIGN_SMALL_B = registerWithItem("roadsign/yellow/small_b", new RoadSignBlock(Blocks.YELLOW_CONCRETE, FabricBlockSettings.copyOf(Blocks.YELLOW_CONCRETE).strength(0.2f,0.2f).nonOpaque()));

    public static final Block WHITE_ROAD_SIGN_LEFT_TOP = registerWithItem("roadsign/white/left_top", new RoadSignBlock(Blocks.WHITE_CONCRETE, FabricBlockSettings.copyOf(Blocks.WHITE_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block WHITE_ROAD_SIGN_TOP = registerWithItem("roadsign/white/top", new RoadSignBlock(Blocks.WHITE_CONCRETE, FabricBlockSettings.copyOf(Blocks.WHITE_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block WHITE_ROAD_SIGN_RIGHT_TOP = registerWithItem("roadsign/white/right_top", new RoadSignBlock(Blocks.WHITE_CONCRETE, FabricBlockSettings.copyOf(Blocks.WHITE_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block WHITE_ROAD_SIGN_LEFT = registerWithItem("roadsign/white/left", new RoadSignBlock(Blocks.WHITE_CONCRETE, FabricBlockSettings.copyOf(Blocks.WHITE_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block WHITE_ROAD_SIGN_MIDDLE = registerWithItem("roadsign/white/middle", new RoadSignBlock(Blocks.WHITE_CONCRETE, FabricBlockSettings.copyOf(Blocks.WHITE_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block WHITE_ROAD_SIGN_RIGHT = registerWithItem("roadsign/white/right", new RoadSignBlock(Blocks.WHITE_CONCRETE, FabricBlockSettings.copyOf(Blocks.WHITE_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block WHITE_ROAD_SIGN_LEFT_BOTTOM = registerWithItem("roadsign/white/left_bottom", new RoadSignBlock(Blocks.WHITE_CONCRETE, FabricBlockSettings.copyOf(Blocks.WHITE_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block WHITE_ROAD_SIGN_BOTTOM = registerWithItem("roadsign/white/bottom", new RoadSignBlock(Blocks.WHITE_CONCRETE, FabricBlockSettings.copyOf(Blocks.WHITE_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block WHITE_ROAD_SIGN_RIGHT_BOTTOM = registerWithItem("roadsign/white/right_bottom", new RoadSignBlock(Blocks.WHITE_CONCRETE, FabricBlockSettings.copyOf(Blocks.WHITE_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block WHITE_ROAD_SIGN_HORIZONTAL_LEFT = registerWithItem("roadsign/white/horizontal_left", new RoadSignBlock(Blocks.WHITE_CONCRETE, FabricBlockSettings.copyOf(Blocks.WHITE_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block WHITE_ROAD_SIGN_HORIZONTAL_MIDDLE = registerWithItem("roadsign/white/horizontal_middle", new RoadSignBlock(Blocks.WHITE_CONCRETE, FabricBlockSettings.copyOf(Blocks.WHITE_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block WHITE_ROAD_SIGN_HORIZONTAL_RIGHT = registerWithItem("roadsign/white/horizontal_right", new RoadSignBlock(Blocks.WHITE_CONCRETE, FabricBlockSettings.copyOf(Blocks.WHITE_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block WHITE_ROAD_SIGN_VERTICAL_TOP = registerWithItem("roadsign/white/vertical_top", new RoadSignBlock(Blocks.WHITE_CONCRETE, FabricBlockSettings.copyOf(Blocks.WHITE_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block WHITE_ROAD_SIGN_VERTICAL_MIDDLE = registerWithItem("roadsign/white/vertical_middle", new RoadSignBlock(Blocks.WHITE_CONCRETE, FabricBlockSettings.copyOf(Blocks.WHITE_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block WHITE_ROAD_SIGN_VERTICAL_BOTTOM = registerWithItem("roadsign/white/vertical_bottom", new RoadSignBlock(Blocks.WHITE_CONCRETE, FabricBlockSettings.copyOf(Blocks.WHITE_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block WHITE_ROAD_SIGN_SMALL_A = registerWithItem("roadsign/white/small_a", new RoadSignBlock(Blocks.WHITE_CONCRETE, FabricBlockSettings.copyOf(Blocks.WHITE_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block WHITE_ROAD_SIGN_SMALL_B = registerWithItem("roadsign/white/small_b", new RoadSignBlock(Blocks.WHITE_CONCRETE, FabricBlockSettings.copyOf(Blocks.WHITE_CONCRETE).strength(0.2f,0.2f).nonOpaque()));

    public static final Block BROWN_ROAD_SIGN_LEFT_TOP = registerWithItem("roadsign/brown/left_top", new RoadSignBlock(Blocks.BROWN_CONCRETE, FabricBlockSettings.copyOf(Blocks.BROWN_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block BROWN_ROAD_SIGN_TOP = registerWithItem("roadsign/brown/top", new RoadSignBlock(Blocks.BROWN_CONCRETE, FabricBlockSettings.copyOf(Blocks.BROWN_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block BROWN_ROAD_SIGN_RIGHT_TOP = registerWithItem("roadsign/brown/right_top", new RoadSignBlock(Blocks.BROWN_CONCRETE, FabricBlockSettings.copyOf(Blocks.BROWN_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block BROWN_ROAD_SIGN_LEFT = registerWithItem("roadsign/brown/left", new RoadSignBlock(Blocks.BROWN_CONCRETE, FabricBlockSettings.copyOf(Blocks.BROWN_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block BROWN_ROAD_SIGN_MIDDLE = registerWithItem("roadsign/brown/middle", new RoadSignBlock(Blocks.BROWN_CONCRETE, FabricBlockSettings.copyOf(Blocks.BROWN_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block BROWN_ROAD_SIGN_RIGHT = registerWithItem("roadsign/brown/right", new RoadSignBlock(Blocks.BROWN_CONCRETE, FabricBlockSettings.copyOf(Blocks.BROWN_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block BROWN_ROAD_SIGN_LEFT_BOTTOM = registerWithItem("roadsign/brown/left_bottom", new RoadSignBlock(Blocks.BROWN_CONCRETE, FabricBlockSettings.copyOf(Blocks.BROWN_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block BROWN_ROAD_SIGN_BOTTOM = registerWithItem("roadsign/brown/bottom", new RoadSignBlock(Blocks.BROWN_CONCRETE, FabricBlockSettings.copyOf(Blocks.BROWN_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block BROWN_ROAD_SIGN_RIGHT_BOTTOM = registerWithItem("roadsign/brown/right_bottom", new RoadSignBlock(Blocks.BROWN_CONCRETE, FabricBlockSettings.copyOf(Blocks.BROWN_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block BROWN_ROAD_SIGN_HORIZONTAL_LEFT = registerWithItem("roadsign/brown/horizontal_left", new RoadSignBlock(Blocks.BROWN_CONCRETE, FabricBlockSettings.copyOf(Blocks.BROWN_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block BROWN_ROAD_SIGN_HORIZONTAL_MIDDLE = registerWithItem("roadsign/brown/horizontal_middle", new RoadSignBlock(Blocks.BROWN_CONCRETE, FabricBlockSettings.copyOf(Blocks.BROWN_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block BROWN_ROAD_SIGN_HORIZONTAL_RIGHT = registerWithItem("roadsign/brown/horizontal_right", new RoadSignBlock(Blocks.BROWN_CONCRETE, FabricBlockSettings.copyOf(Blocks.BROWN_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block BROWN_ROAD_SIGN_VERTICAL_TOP = registerWithItem("roadsign/brown/vertical_top", new RoadSignBlock(Blocks.BROWN_CONCRETE, FabricBlockSettings.copyOf(Blocks.BROWN_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block BROWN_ROAD_SIGN_VERTICAL_MIDDLE = registerWithItem("roadsign/brown/vertical_middle", new RoadSignBlock(Blocks.BROWN_CONCRETE, FabricBlockSettings.copyOf(Blocks.BROWN_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block BROWN_ROAD_SIGN_VERTICAL_BOTTOM = registerWithItem("roadsign/brown/vertical_bottom", new RoadSignBlock(Blocks.BROWN_CONCRETE, FabricBlockSettings.copyOf(Blocks.BROWN_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block BROWN_ROAD_SIGN_SMALL_A = registerWithItem("roadsign/brown/small_a", new RoadSignBlock(Blocks.BROWN_CONCRETE, FabricBlockSettings.copyOf(Blocks.BROWN_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block BROWN_ROAD_SIGN_SMALL_B = registerWithItem("roadsign/brown/small_b", new RoadSignBlock(Blocks.BROWN_CONCRETE, FabricBlockSettings.copyOf(Blocks.BROWN_CONCRETE).strength(0.2f,0.2f).nonOpaque()));

    public static final Block ORANGE_ROAD_SIGN_LEFT_TOP = registerWithItem("roadsign/orange/left_top", new RoadSignBlock(Blocks.ORANGE_CONCRETE, FabricBlockSettings.copyOf(Blocks.ORANGE_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block ORANGE_ROAD_SIGN_TOP = registerWithItem("roadsign/orange/top", new RoadSignBlock(Blocks.ORANGE_CONCRETE, FabricBlockSettings.copyOf(Blocks.ORANGE_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block ORANGE_ROAD_SIGN_RIGHT_TOP = registerWithItem("roadsign/orange/right_top", new RoadSignBlock(Blocks.ORANGE_CONCRETE, FabricBlockSettings.copyOf(Blocks.ORANGE_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block ORANGE_ROAD_SIGN_LEFT = registerWithItem("roadsign/orange/left", new RoadSignBlock(Blocks.ORANGE_CONCRETE, FabricBlockSettings.copyOf(Blocks.ORANGE_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block ORANGE_ROAD_SIGN_MIDDLE = registerWithItem("roadsign/orange/middle", new RoadSignBlock(Blocks.ORANGE_CONCRETE, FabricBlockSettings.copyOf(Blocks.ORANGE_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block ORANGE_ROAD_SIGN_RIGHT = registerWithItem("roadsign/orange/right", new RoadSignBlock(Blocks.ORANGE_CONCRETE, FabricBlockSettings.copyOf(Blocks.ORANGE_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block ORANGE_ROAD_SIGN_LEFT_BOTTOM = registerWithItem("roadsign/orange/left_bottom", new RoadSignBlock(Blocks.ORANGE_CONCRETE, FabricBlockSettings.copyOf(Blocks.ORANGE_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block ORANGE_ROAD_SIGN_BOTTOM = registerWithItem("roadsign/orange/bottom", new RoadSignBlock(Blocks.ORANGE_CONCRETE, FabricBlockSettings.copyOf(Blocks.ORANGE_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block ORANGE_ROAD_SIGN_RIGHT_BOTTOM = registerWithItem("roadsign/orange/right_bottom", new RoadSignBlock(Blocks.ORANGE_CONCRETE, FabricBlockSettings.copyOf(Blocks.ORANGE_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block ORANGE_ROAD_SIGN_HORIZONTAL_LEFT = registerWithItem("roadsign/orange/horizontal_left", new RoadSignBlock(Blocks.ORANGE_CONCRETE, FabricBlockSettings.copyOf(Blocks.ORANGE_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block ORANGE_ROAD_SIGN_HORIZONTAL_MIDDLE = registerWithItem("roadsign/orange/horizontal_middle", new RoadSignBlock(Blocks.ORANGE_CONCRETE, FabricBlockSettings.copyOf(Blocks.ORANGE_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block ORANGE_ROAD_SIGN_HORIZONTAL_RIGHT = registerWithItem("roadsign/orange/horizontal_right", new RoadSignBlock(Blocks.ORANGE_CONCRETE, FabricBlockSettings.copyOf(Blocks.ORANGE_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block ORANGE_ROAD_SIGN_VERTICAL_TOP = registerWithItem("roadsign/orange/vertical_top", new RoadSignBlock(Blocks.ORANGE_CONCRETE, FabricBlockSettings.copyOf(Blocks.ORANGE_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block ORANGE_ROAD_SIGN_VERTICAL_MIDDLE = registerWithItem("roadsign/orange/vertical_middle", new RoadSignBlock(Blocks.ORANGE_CONCRETE, FabricBlockSettings.copyOf(Blocks.ORANGE_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block ORANGE_ROAD_SIGN_VERTICAL_BOTTOM = registerWithItem("roadsign/orange/vertical_bottom", new RoadSignBlock(Blocks.ORANGE_CONCRETE, FabricBlockSettings.copyOf(Blocks.ORANGE_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block ORANGE_ROAD_SIGN_SMALL_A = registerWithItem("roadsign/orange/small_a", new RoadSignBlock(Blocks.ORANGE_CONCRETE, FabricBlockSettings.copyOf(Blocks.ORANGE_CONCRETE).strength(0.2f,0.2f).nonOpaque()));
    public static final Block ORANGE_ROAD_SIGN_SMALL_B = registerWithItem("roadsign/orange/small_b", new RoadSignBlock(Blocks.ORANGE_CONCRETE, FabricBlockSettings.copyOf(Blocks.ORANGE_CONCRETE).strength(0.2f,0.2f).nonOpaque()));

    private static Block registerWithItem(String id, Block block) {
        Block registeredBlock = Registry.register(Registries.BLOCK, new Identifier(OcelotSignMod.MOD_ID, id), block);
        registerBlockItem(id, registeredBlock);
        return registeredBlock;
    }

    private static void registerBlockItem(String id, Block block) {
        Registry.register(Registries.ITEM, new Identifier(OcelotSignMod.MOD_ID, id),
                new RoadSignBlockItem(block, new Item.Settings()));
    }

    public static void registerRoadSignBlocks() {
        OcelotSignMod.LOGGER.info("Registering Road Sign Blocks for " + OcelotSignMod.MOD_ID);
    }
}
