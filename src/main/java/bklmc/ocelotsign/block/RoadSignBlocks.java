package bklmc.ocelotsign.block;

import bklmc.ocelotsign.OcelotSignMod;
import bklmc.ocelotsign.block.custom.RoadSignBlock;
import bklmc.ocelotsign.item.RoadSignBlockItem;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

/**
 * 立式道路指示牌方块注册中心
 *
 * @see bklmc.ocelotsign.block.custom.RoadSignBlock
 */
public class RoadSignBlocks {
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

    public static final Block BLUE_ROAD_SIGN_LEFT_TOP = registerWithItem("roadsign/blue/left_top", Blocks.BLUE_CONCRETE);
    public static final Block BLUE_ROAD_SIGN_TOP = registerWithItem("roadsign/blue/top", Blocks.BLUE_CONCRETE);
    public static final Block BLUE_ROAD_SIGN_RIGHT_TOP = registerWithItem("roadsign/blue/right_top", Blocks.BLUE_CONCRETE);
    public static final Block BLUE_ROAD_SIGN_LEFT = registerWithItem("roadsign/blue/left", Blocks.BLUE_CONCRETE);
    public static final Block BLUE_ROAD_SIGN_MIDDLE = registerWithItem("roadsign/blue/middle", Blocks.BLUE_CONCRETE);
    public static final Block BLUE_ROAD_SIGN_RIGHT = registerWithItem("roadsign/blue/right", Blocks.BLUE_CONCRETE);
    public static final Block BLUE_ROAD_SIGN_LEFT_BOTTOM = registerWithItem("roadsign/blue/left_bottom", Blocks.BLUE_CONCRETE);
    public static final Block BLUE_ROAD_SIGN_BOTTOM = registerWithItem("roadsign/blue/bottom", Blocks.BLUE_CONCRETE);
    public static final Block BLUE_ROAD_SIGN_RIGHT_BOTTOM = registerWithItem("roadsign/blue/right_bottom", Blocks.BLUE_CONCRETE);
    public static final Block BLUE_ROAD_SIGN_HORIZONTAL_LEFT = registerWithItem("roadsign/blue/horizontal_left", Blocks.BLUE_CONCRETE);
    public static final Block BLUE_ROAD_SIGN_HORIZONTAL_MIDDLE = registerWithItem("roadsign/blue/horizontal_middle", Blocks.BLUE_CONCRETE);
    public static final Block BLUE_ROAD_SIGN_HORIZONTAL_RIGHT = registerWithItem("roadsign/blue/horizontal_right", Blocks.BLUE_CONCRETE);
    public static final Block BLUE_ROAD_SIGN_VERTICAL_TOP = registerWithItem("roadsign/blue/vertical_top", Blocks.BLUE_CONCRETE);
    public static final Block BLUE_ROAD_SIGN_VERTICAL_MIDDLE = registerWithItem("roadsign/blue/vertical_middle", Blocks.BLUE_CONCRETE);
    public static final Block BLUE_ROAD_SIGN_VERTICAL_BOTTOM = registerWithItem("roadsign/blue/vertical_bottom", Blocks.BLUE_CONCRETE);
    public static final Block BLUE_ROAD_SIGN_SMALL_A = registerWithItem("roadsign/blue/small_a", Blocks.BLUE_CONCRETE);
    public static final Block BLUE_ROAD_SIGN_SMALL_B = registerWithItem("roadsign/blue/small_b", Blocks.BLUE_CONCRETE);

    public static final Block GREEN_ROAD_SIGN_LEFT_TOP = registerWithItem("roadsign/green/left_top", Blocks.GREEN_CONCRETE);
    public static final Block GREEN_ROAD_SIGN_TOP = registerWithItem("roadsign/green/top", Blocks.GREEN_CONCRETE);
    public static final Block GREEN_ROAD_SIGN_RIGHT_TOP = registerWithItem("roadsign/green/right_top", Blocks.GREEN_CONCRETE);
    public static final Block GREEN_ROAD_SIGN_LEFT = registerWithItem("roadsign/green/left", Blocks.GREEN_CONCRETE);
    public static final Block GREEN_ROAD_SIGN_MIDDLE = registerWithItem("roadsign/green/middle", Blocks.GREEN_CONCRETE);
    public static final Block GREEN_ROAD_SIGN_RIGHT = registerWithItem("roadsign/green/right", Blocks.GREEN_CONCRETE);
    public static final Block GREEN_ROAD_SIGN_LEFT_BOTTOM = registerWithItem("roadsign/green/left_bottom", Blocks.GREEN_CONCRETE);
    public static final Block GREEN_ROAD_SIGN_BOTTOM = registerWithItem("roadsign/green/bottom", Blocks.GREEN_CONCRETE);
    public static final Block GREEN_ROAD_SIGN_RIGHT_BOTTOM = registerWithItem("roadsign/green/right_bottom", Blocks.GREEN_CONCRETE);
    public static final Block GREEN_ROAD_SIGN_HORIZONTAL_LEFT = registerWithItem("roadsign/green/horizontal_left", Blocks.GREEN_CONCRETE);
    public static final Block GREEN_ROAD_SIGN_HORIZONTAL_MIDDLE = registerWithItem("roadsign/green/horizontal_middle", Blocks.GREEN_CONCRETE);
    public static final Block GREEN_ROAD_SIGN_HORIZONTAL_RIGHT = registerWithItem("roadsign/green/horizontal_right", Blocks.GREEN_CONCRETE);
    public static final Block GREEN_ROAD_SIGN_VERTICAL_TOP = registerWithItem("roadsign/green/vertical_top", Blocks.GREEN_CONCRETE);
    public static final Block GREEN_ROAD_SIGN_VERTICAL_MIDDLE = registerWithItem("roadsign/green/vertical_middle", Blocks.GREEN_CONCRETE);
    public static final Block GREEN_ROAD_SIGN_VERTICAL_BOTTOM = registerWithItem("roadsign/green/vertical_bottom", Blocks.GREEN_CONCRETE);
    public static final Block GREEN_ROAD_SIGN_SMALL_A = registerWithItem("roadsign/green/small_a", Blocks.GREEN_CONCRETE);
    public static final Block GREEN_ROAD_SIGN_SMALL_B = registerWithItem("roadsign/green/small_b", Blocks.GREEN_CONCRETE);

    public static final Block YELLOW_ROAD_SIGN_LEFT_TOP = registerWithItem("roadsign/yellow/left_top", Blocks.YELLOW_CONCRETE);
    public static final Block YELLOW_ROAD_SIGN_TOP = registerWithItem("roadsign/yellow/top", Blocks.YELLOW_CONCRETE);
    public static final Block YELLOW_ROAD_SIGN_RIGHT_TOP = registerWithItem("roadsign/yellow/right_top", Blocks.YELLOW_CONCRETE);
    public static final Block YELLOW_ROAD_SIGN_LEFT = registerWithItem("roadsign/yellow/left", Blocks.YELLOW_CONCRETE);
    public static final Block YELLOW_ROAD_SIGN_MIDDLE = registerWithItem("roadsign/yellow/middle", Blocks.YELLOW_CONCRETE);
    public static final Block YELLOW_ROAD_SIGN_RIGHT = registerWithItem("roadsign/yellow/right", Blocks.YELLOW_CONCRETE);
    public static final Block YELLOW_ROAD_SIGN_LEFT_BOTTOM = registerWithItem("roadsign/yellow/left_bottom", Blocks.YELLOW_CONCRETE);
    public static final Block YELLOW_ROAD_SIGN_BOTTOM = registerWithItem("roadsign/yellow/bottom", Blocks.YELLOW_CONCRETE);
    public static final Block YELLOW_ROAD_SIGN_RIGHT_BOTTOM = registerWithItem("roadsign/yellow/right_bottom", Blocks.YELLOW_CONCRETE);
    public static final Block YELLOW_ROAD_SIGN_HORIZONTAL_LEFT = registerWithItem("roadsign/yellow/horizontal_left", Blocks.YELLOW_CONCRETE);
    public static final Block YELLOW_ROAD_SIGN_HORIZONTAL_MIDDLE = registerWithItem("roadsign/yellow/horizontal_middle", Blocks.YELLOW_CONCRETE);
    public static final Block YELLOW_ROAD_SIGN_HORIZONTAL_RIGHT = registerWithItem("roadsign/yellow/horizontal_right", Blocks.YELLOW_CONCRETE);
    public static final Block YELLOW_ROAD_SIGN_VERTICAL_TOP = registerWithItem("roadsign/yellow/vertical_top", Blocks.YELLOW_CONCRETE);
    public static final Block YELLOW_ROAD_SIGN_VERTICAL_MIDDLE = registerWithItem("roadsign/yellow/vertical_middle", Blocks.YELLOW_CONCRETE);
    public static final Block YELLOW_ROAD_SIGN_VERTICAL_BOTTOM = registerWithItem("roadsign/yellow/vertical_bottom", Blocks.YELLOW_CONCRETE);
    public static final Block YELLOW_ROAD_SIGN_SMALL_A = registerWithItem("roadsign/yellow/small_a", Blocks.YELLOW_CONCRETE);
    public static final Block YELLOW_ROAD_SIGN_SMALL_B = registerWithItem("roadsign/yellow/small_b", Blocks.YELLOW_CONCRETE);

    public static final Block WHITE_ROAD_SIGN_LEFT_TOP = registerWithItem("roadsign/white/left_top", Blocks.WHITE_CONCRETE);
    public static final Block WHITE_ROAD_SIGN_TOP = registerWithItem("roadsign/white/top", Blocks.WHITE_CONCRETE);
    public static final Block WHITE_ROAD_SIGN_RIGHT_TOP = registerWithItem("roadsign/white/right_top", Blocks.WHITE_CONCRETE);
    public static final Block WHITE_ROAD_SIGN_LEFT = registerWithItem("roadsign/white/left", Blocks.WHITE_CONCRETE);
    public static final Block WHITE_ROAD_SIGN_MIDDLE = registerWithItem("roadsign/white/middle", Blocks.WHITE_CONCRETE);
    public static final Block WHITE_ROAD_SIGN_RIGHT = registerWithItem("roadsign/white/right", Blocks.WHITE_CONCRETE);
    public static final Block WHITE_ROAD_SIGN_LEFT_BOTTOM = registerWithItem("roadsign/white/left_bottom", Blocks.WHITE_CONCRETE);
    public static final Block WHITE_ROAD_SIGN_BOTTOM = registerWithItem("roadsign/white/bottom", Blocks.WHITE_CONCRETE);
    public static final Block WHITE_ROAD_SIGN_RIGHT_BOTTOM = registerWithItem("roadsign/white/right_bottom", Blocks.WHITE_CONCRETE);
    public static final Block WHITE_ROAD_SIGN_HORIZONTAL_LEFT = registerWithItem("roadsign/white/horizontal_left", Blocks.WHITE_CONCRETE);
    public static final Block WHITE_ROAD_SIGN_HORIZONTAL_MIDDLE = registerWithItem("roadsign/white/horizontal_middle", Blocks.WHITE_CONCRETE);
    public static final Block WHITE_ROAD_SIGN_HORIZONTAL_RIGHT = registerWithItem("roadsign/white/horizontal_right", Blocks.WHITE_CONCRETE);
    public static final Block WHITE_ROAD_SIGN_VERTICAL_TOP = registerWithItem("roadsign/white/vertical_top", Blocks.WHITE_CONCRETE);
    public static final Block WHITE_ROAD_SIGN_VERTICAL_MIDDLE = registerWithItem("roadsign/white/vertical_middle", Blocks.WHITE_CONCRETE);
    public static final Block WHITE_ROAD_SIGN_VERTICAL_BOTTOM = registerWithItem("roadsign/white/vertical_bottom", Blocks.WHITE_CONCRETE);
    public static final Block WHITE_ROAD_SIGN_SMALL_A = registerWithItem("roadsign/white/small_a", Blocks.WHITE_CONCRETE);
    public static final Block WHITE_ROAD_SIGN_SMALL_B = registerWithItem("roadsign/white/small_b", Blocks.WHITE_CONCRETE);

    public static final Block BROWN_ROAD_SIGN_LEFT_TOP = registerWithItem("roadsign/brown/left_top", Blocks.BROWN_CONCRETE);
    public static final Block BROWN_ROAD_SIGN_TOP = registerWithItem("roadsign/brown/top", Blocks.BROWN_CONCRETE);
    public static final Block BROWN_ROAD_SIGN_RIGHT_TOP = registerWithItem("roadsign/brown/right_top", Blocks.BROWN_CONCRETE);
    public static final Block BROWN_ROAD_SIGN_LEFT = registerWithItem("roadsign/brown/left", Blocks.BROWN_CONCRETE);
    public static final Block BROWN_ROAD_SIGN_MIDDLE = registerWithItem("roadsign/brown/middle", Blocks.BROWN_CONCRETE);
    public static final Block BROWN_ROAD_SIGN_RIGHT = registerWithItem("roadsign/brown/right", Blocks.BROWN_CONCRETE);
    public static final Block BROWN_ROAD_SIGN_LEFT_BOTTOM = registerWithItem("roadsign/brown/left_bottom", Blocks.BROWN_CONCRETE);
    public static final Block BROWN_ROAD_SIGN_BOTTOM = registerWithItem("roadsign/brown/bottom", Blocks.BROWN_CONCRETE);
    public static final Block BROWN_ROAD_SIGN_RIGHT_BOTTOM = registerWithItem("roadsign/brown/right_bottom", Blocks.BROWN_CONCRETE);
    public static final Block BROWN_ROAD_SIGN_HORIZONTAL_LEFT = registerWithItem("roadsign/brown/horizontal_left", Blocks.BROWN_CONCRETE);
    public static final Block BROWN_ROAD_SIGN_HORIZONTAL_MIDDLE = registerWithItem("roadsign/brown/horizontal_middle", Blocks.BROWN_CONCRETE);
    public static final Block BROWN_ROAD_SIGN_HORIZONTAL_RIGHT = registerWithItem("roadsign/brown/horizontal_right", Blocks.BROWN_CONCRETE);
    public static final Block BROWN_ROAD_SIGN_VERTICAL_TOP = registerWithItem("roadsign/brown/vertical_top", Blocks.BROWN_CONCRETE);
    public static final Block BROWN_ROAD_SIGN_VERTICAL_MIDDLE = registerWithItem("roadsign/brown/vertical_middle", Blocks.BROWN_CONCRETE);
    public static final Block BROWN_ROAD_SIGN_VERTICAL_BOTTOM = registerWithItem("roadsign/brown/vertical_bottom", Blocks.BROWN_CONCRETE);
    public static final Block BROWN_ROAD_SIGN_SMALL_A = registerWithItem("roadsign/brown/small_a", Blocks.BROWN_CONCRETE);
    public static final Block BROWN_ROAD_SIGN_SMALL_B = registerWithItem("roadsign/brown/small_b", Blocks.BROWN_CONCRETE);

    public static final Block ORANGE_ROAD_SIGN_LEFT_TOP = registerWithItem("roadsign/orange/left_top", Blocks.ORANGE_CONCRETE);
    public static final Block ORANGE_ROAD_SIGN_TOP = registerWithItem("roadsign/orange/top", Blocks.ORANGE_CONCRETE);
    public static final Block ORANGE_ROAD_SIGN_RIGHT_TOP = registerWithItem("roadsign/orange/right_top", Blocks.ORANGE_CONCRETE);
    public static final Block ORANGE_ROAD_SIGN_LEFT = registerWithItem("roadsign/orange/left", Blocks.ORANGE_CONCRETE);
    public static final Block ORANGE_ROAD_SIGN_MIDDLE = registerWithItem("roadsign/orange/middle", Blocks.ORANGE_CONCRETE);
    public static final Block ORANGE_ROAD_SIGN_RIGHT = registerWithItem("roadsign/orange/right", Blocks.ORANGE_CONCRETE);
    public static final Block ORANGE_ROAD_SIGN_LEFT_BOTTOM = registerWithItem("roadsign/orange/left_bottom", Blocks.ORANGE_CONCRETE);
    public static final Block ORANGE_ROAD_SIGN_BOTTOM = registerWithItem("roadsign/orange/bottom", Blocks.ORANGE_CONCRETE);
    public static final Block ORANGE_ROAD_SIGN_RIGHT_BOTTOM = registerWithItem("roadsign/orange/right_bottom", Blocks.ORANGE_CONCRETE);
    public static final Block ORANGE_ROAD_SIGN_HORIZONTAL_LEFT = registerWithItem("roadsign/orange/horizontal_left", Blocks.ORANGE_CONCRETE);
    public static final Block ORANGE_ROAD_SIGN_HORIZONTAL_MIDDLE = registerWithItem("roadsign/orange/horizontal_middle", Blocks.ORANGE_CONCRETE);
    public static final Block ORANGE_ROAD_SIGN_HORIZONTAL_RIGHT = registerWithItem("roadsign/orange/horizontal_right", Blocks.ORANGE_CONCRETE);
    public static final Block ORANGE_ROAD_SIGN_VERTICAL_TOP = registerWithItem("roadsign/orange/vertical_top", Blocks.ORANGE_CONCRETE);
    public static final Block ORANGE_ROAD_SIGN_VERTICAL_MIDDLE = registerWithItem("roadsign/orange/vertical_middle", Blocks.ORANGE_CONCRETE);
    public static final Block ORANGE_ROAD_SIGN_VERTICAL_BOTTOM = registerWithItem("roadsign/orange/vertical_bottom", Blocks.ORANGE_CONCRETE);
    public static final Block ORANGE_ROAD_SIGN_SMALL_A = registerWithItem("roadsign/orange/small_a", Blocks.ORANGE_CONCRETE);
    public static final Block ORANGE_ROAD_SIGN_SMALL_B = registerWithItem("roadsign/orange/small_b", Blocks.ORANGE_CONCRETE);

    private static Block registerWithItem(String id, Block baseBlock) {
        RoadSignBlock block = new RoadSignBlock(baseBlock, createSettings(id, baseBlock));
        Block registeredBlock = Registry.register(Registries.BLOCK, getBlockKey(id), block);
        Registry.register(Registries.ITEM, getItemKey(id),
                new RoadSignBlockItem(block, new Item.Settings().registryKey(getItemKey(id))));
        return registeredBlock;
    }

    public static void registerRoadSignBlocks() {
        OcelotSignMod.LOGGER.info("Registering Road Sign Blocks for " + OcelotSignMod.MOD_ID);
    }
}
