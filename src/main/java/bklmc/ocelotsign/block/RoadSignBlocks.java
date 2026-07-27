package bklmc.ocelotsign.block;

import bklmc.ocelotsign.OcelotSignMod;
import bklmc.ocelotsign.block.custom.RoadSignBlock;
import bklmc.ocelotsign.item.RoadSignBlockItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

/**
 * 立式道路指示牌方块注册中心
 *
 * @see bklmc.ocelotsign.block.custom.RoadSignBlock
 */
public class RoadSignBlocks {
    public static final Block BLUE_ROAD_SIGN_LEFT_TOP = registerWithItem("roadsign/blue/left_top", new RoadSignBlock(Blocks.BLUE_CONCRETE, signSettings(Blocks.BLUE_CONCRETE, "roadsign/blue/left_top")));
    public static final Block BLUE_ROAD_SIGN_TOP = registerWithItem("roadsign/blue/top", new RoadSignBlock(Blocks.BLUE_CONCRETE, signSettings(Blocks.BLUE_CONCRETE, "roadsign/blue/top")));
    public static final Block BLUE_ROAD_SIGN_RIGHT_TOP = registerWithItem("roadsign/blue/right_top", new RoadSignBlock(Blocks.BLUE_CONCRETE, signSettings(Blocks.BLUE_CONCRETE, "roadsign/blue/right_top")));
    public static final Block BLUE_ROAD_SIGN_LEFT = registerWithItem("roadsign/blue/left", new RoadSignBlock(Blocks.BLUE_CONCRETE, signSettings(Blocks.BLUE_CONCRETE, "roadsign/blue/left")));
    public static final Block BLUE_ROAD_SIGN_MIDDLE = registerWithItem("roadsign/blue/middle", new RoadSignBlock(Blocks.BLUE_CONCRETE, signSettings(Blocks.BLUE_CONCRETE, "roadsign/blue/middle")));
    public static final Block BLUE_ROAD_SIGN_RIGHT = registerWithItem("roadsign/blue/right", new RoadSignBlock(Blocks.BLUE_CONCRETE, signSettings(Blocks.BLUE_CONCRETE, "roadsign/blue/right")));
    public static final Block BLUE_ROAD_SIGN_LEFT_BOTTOM = registerWithItem("roadsign/blue/left_bottom", new RoadSignBlock(Blocks.BLUE_CONCRETE, signSettings(Blocks.BLUE_CONCRETE, "roadsign/blue/left_bottom")));
    public static final Block BLUE_ROAD_SIGN_BOTTOM = registerWithItem("roadsign/blue/bottom", new RoadSignBlock(Blocks.BLUE_CONCRETE, signSettings(Blocks.BLUE_CONCRETE, "roadsign/blue/bottom")));
    public static final Block BLUE_ROAD_SIGN_RIGHT_BOTTOM = registerWithItem("roadsign/blue/right_bottom", new RoadSignBlock(Blocks.BLUE_CONCRETE, signSettings(Blocks.BLUE_CONCRETE, "roadsign/blue/right_bottom")));
    public static final Block BLUE_ROAD_SIGN_HORIZONTAL_LEFT = registerWithItem("roadsign/blue/horizontal_left", new RoadSignBlock(Blocks.BLUE_CONCRETE, signSettings(Blocks.BLUE_CONCRETE, "roadsign/blue/horizontal_left")));
    public static final Block BLUE_ROAD_SIGN_HORIZONTAL_MIDDLE = registerWithItem("roadsign/blue/horizontal_middle", new RoadSignBlock(Blocks.BLUE_CONCRETE, signSettings(Blocks.BLUE_CONCRETE, "roadsign/blue/horizontal_middle")));
    public static final Block BLUE_ROAD_SIGN_HORIZONTAL_RIGHT = registerWithItem("roadsign/blue/horizontal_right", new RoadSignBlock(Blocks.BLUE_CONCRETE, signSettings(Blocks.BLUE_CONCRETE, "roadsign/blue/horizontal_right")));
    public static final Block BLUE_ROAD_SIGN_VERTICAL_TOP = registerWithItem("roadsign/blue/vertical_top", new RoadSignBlock(Blocks.BLUE_CONCRETE, signSettings(Blocks.BLUE_CONCRETE, "roadsign/blue/vertical_top")));
    public static final Block BLUE_ROAD_SIGN_VERTICAL_MIDDLE = registerWithItem("roadsign/blue/vertical_middle", new RoadSignBlock(Blocks.BLUE_CONCRETE, signSettings(Blocks.BLUE_CONCRETE, "roadsign/blue/vertical_middle")));
    public static final Block BLUE_ROAD_SIGN_VERTICAL_BOTTOM = registerWithItem("roadsign/blue/vertical_bottom", new RoadSignBlock(Blocks.BLUE_CONCRETE, signSettings(Blocks.BLUE_CONCRETE, "roadsign/blue/vertical_bottom")));
    public static final Block BLUE_ROAD_SIGN_SMALL_A = registerWithItem("roadsign/blue/small_a", new RoadSignBlock(Blocks.BLUE_CONCRETE, signSettings(Blocks.BLUE_CONCRETE, "roadsign/blue/small_a")));
    public static final Block BLUE_ROAD_SIGN_SMALL_B = registerWithItem("roadsign/blue/small_b", new RoadSignBlock(Blocks.BLUE_CONCRETE, signSettings(Blocks.BLUE_CONCRETE, "roadsign/blue/small_b")));


    public static final Block GREEN_ROAD_SIGN_LEFT_TOP = registerWithItem("roadsign/green/left_top", new RoadSignBlock(Blocks.GREEN_CONCRETE, signSettings(Blocks.GREEN_CONCRETE, "roadsign/green/left_top")));
    public static final Block GREEN_ROAD_SIGN_TOP = registerWithItem("roadsign/green/top", new RoadSignBlock(Blocks.GREEN_CONCRETE, signSettings(Blocks.GREEN_CONCRETE, "roadsign/green/top")));
    public static final Block GREEN_ROAD_SIGN_RIGHT_TOP = registerWithItem("roadsign/green/right_top", new RoadSignBlock(Blocks.GREEN_CONCRETE, signSettings(Blocks.GREEN_CONCRETE, "roadsign/green/right_top")));
    public static final Block GREEN_ROAD_SIGN_LEFT = registerWithItem("roadsign/green/left", new RoadSignBlock(Blocks.GREEN_CONCRETE, signSettings(Blocks.GREEN_CONCRETE, "roadsign/green/left")));
    public static final Block GREEN_ROAD_SIGN_MIDDLE = registerWithItem("roadsign/green/middle", new RoadSignBlock(Blocks.GREEN_CONCRETE, signSettings(Blocks.GREEN_CONCRETE, "roadsign/green/middle")));
    public static final Block GREEN_ROAD_SIGN_RIGHT = registerWithItem("roadsign/green/right", new RoadSignBlock(Blocks.GREEN_CONCRETE, signSettings(Blocks.GREEN_CONCRETE, "roadsign/green/right")));
    public static final Block GREEN_ROAD_SIGN_LEFT_BOTTOM = registerWithItem("roadsign/green/left_bottom", new RoadSignBlock(Blocks.GREEN_CONCRETE, signSettings(Blocks.GREEN_CONCRETE, "roadsign/green/left_bottom")));
    public static final Block GREEN_ROAD_SIGN_BOTTOM = registerWithItem("roadsign/green/bottom", new RoadSignBlock(Blocks.GREEN_CONCRETE, signSettings(Blocks.GREEN_CONCRETE, "roadsign/green/bottom")));
    public static final Block GREEN_ROAD_SIGN_RIGHT_BOTTOM = registerWithItem("roadsign/green/right_bottom", new RoadSignBlock(Blocks.GREEN_CONCRETE, signSettings(Blocks.GREEN_CONCRETE, "roadsign/green/right_bottom")));
    public static final Block GREEN_ROAD_SIGN_HORIZONTAL_LEFT = registerWithItem("roadsign/green/horizontal_left", new RoadSignBlock(Blocks.GREEN_CONCRETE, signSettings(Blocks.GREEN_CONCRETE, "roadsign/green/horizontal_left")));
    public static final Block GREEN_ROAD_SIGN_HORIZONTAL_MIDDLE = registerWithItem("roadsign/green/horizontal_middle", new RoadSignBlock(Blocks.GREEN_CONCRETE, signSettings(Blocks.GREEN_CONCRETE, "roadsign/green/horizontal_middle")));
    public static final Block GREEN_ROAD_SIGN_HORIZONTAL_RIGHT = registerWithItem("roadsign/green/horizontal_right", new RoadSignBlock(Blocks.GREEN_CONCRETE, signSettings(Blocks.GREEN_CONCRETE, "roadsign/green/horizontal_right")));
    public static final Block GREEN_ROAD_SIGN_VERTICAL_TOP = registerWithItem("roadsign/green/vertical_top", new RoadSignBlock(Blocks.GREEN_CONCRETE, signSettings(Blocks.GREEN_CONCRETE, "roadsign/green/vertical_top")));
    public static final Block GREEN_ROAD_SIGN_VERTICAL_MIDDLE = registerWithItem("roadsign/green/vertical_middle", new RoadSignBlock(Blocks.GREEN_CONCRETE, signSettings(Blocks.GREEN_CONCRETE, "roadsign/green/vertical_middle")));
    public static final Block GREEN_ROAD_SIGN_VERTICAL_BOTTOM = registerWithItem("roadsign/green/vertical_bottom", new RoadSignBlock(Blocks.GREEN_CONCRETE, signSettings(Blocks.GREEN_CONCRETE, "roadsign/green/vertical_bottom")));
    public static final Block GREEN_ROAD_SIGN_SMALL_A = registerWithItem("roadsign/green/small_a", new RoadSignBlock(Blocks.GREEN_CONCRETE, signSettings(Blocks.GREEN_CONCRETE, "roadsign/green/small_a")));
    public static final Block GREEN_ROAD_SIGN_SMALL_B = registerWithItem("roadsign/green/small_b", new RoadSignBlock(Blocks.GREEN_CONCRETE, signSettings(Blocks.GREEN_CONCRETE, "roadsign/green/small_b")));

    public static final Block YELLOW_ROAD_SIGN_LEFT_TOP = registerWithItem("roadsign/yellow/left_top", new RoadSignBlock(Blocks.YELLOW_CONCRETE, signSettings(Blocks.YELLOW_CONCRETE, "roadsign/yellow/left_top")));
    public static final Block YELLOW_ROAD_SIGN_TOP = registerWithItem("roadsign/yellow/top", new RoadSignBlock(Blocks.YELLOW_CONCRETE, signSettings(Blocks.YELLOW_CONCRETE, "roadsign/yellow/top")));
    public static final Block YELLOW_ROAD_SIGN_RIGHT_TOP = registerWithItem("roadsign/yellow/right_top", new RoadSignBlock(Blocks.YELLOW_CONCRETE, signSettings(Blocks.YELLOW_CONCRETE, "roadsign/yellow/right_top")));
    public static final Block YELLOW_ROAD_SIGN_LEFT = registerWithItem("roadsign/yellow/left", new RoadSignBlock(Blocks.YELLOW_CONCRETE, signSettings(Blocks.YELLOW_CONCRETE, "roadsign/yellow/left")));
    public static final Block YELLOW_ROAD_SIGN_MIDDLE = registerWithItem("roadsign/yellow/middle", new RoadSignBlock(Blocks.YELLOW_CONCRETE, signSettings(Blocks.YELLOW_CONCRETE, "roadsign/yellow/middle")));
    public static final Block YELLOW_ROAD_SIGN_RIGHT = registerWithItem("roadsign/yellow/right", new RoadSignBlock(Blocks.YELLOW_CONCRETE, signSettings(Blocks.YELLOW_CONCRETE, "roadsign/yellow/right")));
    public static final Block YELLOW_ROAD_SIGN_LEFT_BOTTOM = registerWithItem("roadsign/yellow/left_bottom", new RoadSignBlock(Blocks.YELLOW_CONCRETE, signSettings(Blocks.YELLOW_CONCRETE, "roadsign/yellow/left_bottom")));
    public static final Block YELLOW_ROAD_SIGN_BOTTOM = registerWithItem("roadsign/yellow/bottom", new RoadSignBlock(Blocks.YELLOW_CONCRETE, signSettings(Blocks.YELLOW_CONCRETE, "roadsign/yellow/bottom")));
    public static final Block YELLOW_ROAD_SIGN_RIGHT_BOTTOM = registerWithItem("roadsign/yellow/right_bottom", new RoadSignBlock(Blocks.YELLOW_CONCRETE, signSettings(Blocks.YELLOW_CONCRETE, "roadsign/yellow/right_bottom")));
    public static final Block YELLOW_ROAD_SIGN_HORIZONTAL_LEFT = registerWithItem("roadsign/yellow/horizontal_left", new RoadSignBlock(Blocks.YELLOW_CONCRETE, signSettings(Blocks.YELLOW_CONCRETE, "roadsign/yellow/horizontal_left")));
    public static final Block YELLOW_ROAD_SIGN_HORIZONTAL_MIDDLE = registerWithItem("roadsign/yellow/horizontal_middle", new RoadSignBlock(Blocks.YELLOW_CONCRETE, signSettings(Blocks.YELLOW_CONCRETE, "roadsign/yellow/horizontal_middle")));
    public static final Block YELLOW_ROAD_SIGN_HORIZONTAL_RIGHT = registerWithItem("roadsign/yellow/horizontal_right", new RoadSignBlock(Blocks.YELLOW_CONCRETE, signSettings(Blocks.YELLOW_CONCRETE, "roadsign/yellow/horizontal_right")));
    public static final Block YELLOW_ROAD_SIGN_VERTICAL_TOP = registerWithItem("roadsign/yellow/vertical_top", new RoadSignBlock(Blocks.YELLOW_CONCRETE, signSettings(Blocks.YELLOW_CONCRETE, "roadsign/yellow/vertical_top")));
    public static final Block YELLOW_ROAD_SIGN_VERTICAL_MIDDLE = registerWithItem("roadsign/yellow/vertical_middle", new RoadSignBlock(Blocks.YELLOW_CONCRETE, signSettings(Blocks.YELLOW_CONCRETE, "roadsign/yellow/vertical_middle")));
    public static final Block YELLOW_ROAD_SIGN_VERTICAL_BOTTOM = registerWithItem("roadsign/yellow/vertical_bottom", new RoadSignBlock(Blocks.YELLOW_CONCRETE, signSettings(Blocks.YELLOW_CONCRETE, "roadsign/yellow/vertical_bottom")));
    public static final Block YELLOW_ROAD_SIGN_SMALL_A = registerWithItem("roadsign/yellow/small_a", new RoadSignBlock(Blocks.YELLOW_CONCRETE, signSettings(Blocks.YELLOW_CONCRETE, "roadsign/yellow/small_a")));
    public static final Block YELLOW_ROAD_SIGN_SMALL_B = registerWithItem("roadsign/yellow/small_b", new RoadSignBlock(Blocks.YELLOW_CONCRETE, signSettings(Blocks.YELLOW_CONCRETE, "roadsign/yellow/small_b")));

    public static final Block WHITE_ROAD_SIGN_LEFT_TOP = registerWithItem("roadsign/white/left_top", new RoadSignBlock(Blocks.WHITE_CONCRETE, signSettings(Blocks.WHITE_CONCRETE, "roadsign/white/left_top")));
    public static final Block WHITE_ROAD_SIGN_TOP = registerWithItem("roadsign/white/top", new RoadSignBlock(Blocks.WHITE_CONCRETE, signSettings(Blocks.WHITE_CONCRETE, "roadsign/white/top")));
    public static final Block WHITE_ROAD_SIGN_RIGHT_TOP = registerWithItem("roadsign/white/right_top", new RoadSignBlock(Blocks.WHITE_CONCRETE, signSettings(Blocks.WHITE_CONCRETE, "roadsign/white/right_top")));
    public static final Block WHITE_ROAD_SIGN_LEFT = registerWithItem("roadsign/white/left", new RoadSignBlock(Blocks.WHITE_CONCRETE, signSettings(Blocks.WHITE_CONCRETE, "roadsign/white/left")));
    public static final Block WHITE_ROAD_SIGN_MIDDLE = registerWithItem("roadsign/white/middle", new RoadSignBlock(Blocks.WHITE_CONCRETE, signSettings(Blocks.WHITE_CONCRETE, "roadsign/white/middle")));
    public static final Block WHITE_ROAD_SIGN_RIGHT = registerWithItem("roadsign/white/right", new RoadSignBlock(Blocks.WHITE_CONCRETE, signSettings(Blocks.WHITE_CONCRETE, "roadsign/white/right")));
    public static final Block WHITE_ROAD_SIGN_LEFT_BOTTOM = registerWithItem("roadsign/white/left_bottom", new RoadSignBlock(Blocks.WHITE_CONCRETE, signSettings(Blocks.WHITE_CONCRETE, "roadsign/white/left_bottom")));
    public static final Block WHITE_ROAD_SIGN_BOTTOM = registerWithItem("roadsign/white/bottom", new RoadSignBlock(Blocks.WHITE_CONCRETE, signSettings(Blocks.WHITE_CONCRETE, "roadsign/white/bottom")));
    public static final Block WHITE_ROAD_SIGN_RIGHT_BOTTOM = registerWithItem("roadsign/white/right_bottom", new RoadSignBlock(Blocks.WHITE_CONCRETE, signSettings(Blocks.WHITE_CONCRETE, "roadsign/white/right_bottom")));
    public static final Block WHITE_ROAD_SIGN_HORIZONTAL_LEFT = registerWithItem("roadsign/white/horizontal_left", new RoadSignBlock(Blocks.WHITE_CONCRETE, signSettings(Blocks.WHITE_CONCRETE, "roadsign/white/horizontal_left")));
    public static final Block WHITE_ROAD_SIGN_HORIZONTAL_MIDDLE = registerWithItem("roadsign/white/horizontal_middle", new RoadSignBlock(Blocks.WHITE_CONCRETE, signSettings(Blocks.WHITE_CONCRETE, "roadsign/white/horizontal_middle")));
    public static final Block WHITE_ROAD_SIGN_HORIZONTAL_RIGHT = registerWithItem("roadsign/white/horizontal_right", new RoadSignBlock(Blocks.WHITE_CONCRETE, signSettings(Blocks.WHITE_CONCRETE, "roadsign/white/horizontal_right")));
    public static final Block WHITE_ROAD_SIGN_VERTICAL_TOP = registerWithItem("roadsign/white/vertical_top", new RoadSignBlock(Blocks.WHITE_CONCRETE, signSettings(Blocks.WHITE_CONCRETE, "roadsign/white/vertical_top")));
    public static final Block WHITE_ROAD_SIGN_VERTICAL_MIDDLE = registerWithItem("roadsign/white/vertical_middle", new RoadSignBlock(Blocks.WHITE_CONCRETE, signSettings(Blocks.WHITE_CONCRETE, "roadsign/white/vertical_middle")));
    public static final Block WHITE_ROAD_SIGN_VERTICAL_BOTTOM = registerWithItem("roadsign/white/vertical_bottom", new RoadSignBlock(Blocks.WHITE_CONCRETE, signSettings(Blocks.WHITE_CONCRETE, "roadsign/white/vertical_bottom")));
    public static final Block WHITE_ROAD_SIGN_SMALL_A = registerWithItem("roadsign/white/small_a", new RoadSignBlock(Blocks.WHITE_CONCRETE, signSettings(Blocks.WHITE_CONCRETE, "roadsign/white/small_a")));
    public static final Block WHITE_ROAD_SIGN_SMALL_B = registerWithItem("roadsign/white/small_b", new RoadSignBlock(Blocks.WHITE_CONCRETE, signSettings(Blocks.WHITE_CONCRETE, "roadsign/white/small_b")));

    public static final Block BROWN_ROAD_SIGN_LEFT_TOP = registerWithItem("roadsign/brown/left_top", new RoadSignBlock(Blocks.BROWN_CONCRETE, signSettings(Blocks.BROWN_CONCRETE, "roadsign/brown/left_top")));
    public static final Block BROWN_ROAD_SIGN_TOP = registerWithItem("roadsign/brown/top", new RoadSignBlock(Blocks.BROWN_CONCRETE, signSettings(Blocks.BROWN_CONCRETE, "roadsign/brown/top")));
    public static final Block BROWN_ROAD_SIGN_RIGHT_TOP = registerWithItem("roadsign/brown/right_top", new RoadSignBlock(Blocks.BROWN_CONCRETE, signSettings(Blocks.BROWN_CONCRETE, "roadsign/brown/right_top")));
    public static final Block BROWN_ROAD_SIGN_LEFT = registerWithItem("roadsign/brown/left", new RoadSignBlock(Blocks.BROWN_CONCRETE, signSettings(Blocks.BROWN_CONCRETE, "roadsign/brown/left")));
    public static final Block BROWN_ROAD_SIGN_MIDDLE = registerWithItem("roadsign/brown/middle", new RoadSignBlock(Blocks.BROWN_CONCRETE, signSettings(Blocks.BROWN_CONCRETE, "roadsign/brown/middle")));
    public static final Block BROWN_ROAD_SIGN_RIGHT = registerWithItem("roadsign/brown/right", new RoadSignBlock(Blocks.BROWN_CONCRETE, signSettings(Blocks.BROWN_CONCRETE, "roadsign/brown/right")));
    public static final Block BROWN_ROAD_SIGN_LEFT_BOTTOM = registerWithItem("roadsign/brown/left_bottom", new RoadSignBlock(Blocks.BROWN_CONCRETE, signSettings(Blocks.BROWN_CONCRETE, "roadsign/brown/left_bottom")));
    public static final Block BROWN_ROAD_SIGN_BOTTOM = registerWithItem("roadsign/brown/bottom", new RoadSignBlock(Blocks.BROWN_CONCRETE, signSettings(Blocks.BROWN_CONCRETE, "roadsign/brown/bottom")));
    public static final Block BROWN_ROAD_SIGN_RIGHT_BOTTOM = registerWithItem("roadsign/brown/right_bottom", new RoadSignBlock(Blocks.BROWN_CONCRETE, signSettings(Blocks.BROWN_CONCRETE, "roadsign/brown/right_bottom")));
    public static final Block BROWN_ROAD_SIGN_HORIZONTAL_LEFT = registerWithItem("roadsign/brown/horizontal_left", new RoadSignBlock(Blocks.BROWN_CONCRETE, signSettings(Blocks.BROWN_CONCRETE, "roadsign/brown/horizontal_left")));
    public static final Block BROWN_ROAD_SIGN_HORIZONTAL_MIDDLE = registerWithItem("roadsign/brown/horizontal_middle", new RoadSignBlock(Blocks.BROWN_CONCRETE, signSettings(Blocks.BROWN_CONCRETE, "roadsign/brown/horizontal_middle")));
    public static final Block BROWN_ROAD_SIGN_HORIZONTAL_RIGHT = registerWithItem("roadsign/brown/horizontal_right", new RoadSignBlock(Blocks.BROWN_CONCRETE, signSettings(Blocks.BROWN_CONCRETE, "roadsign/brown/horizontal_right")));
    public static final Block BROWN_ROAD_SIGN_VERTICAL_TOP = registerWithItem("roadsign/brown/vertical_top", new RoadSignBlock(Blocks.BROWN_CONCRETE, signSettings(Blocks.BROWN_CONCRETE, "roadsign/brown/vertical_top")));
    public static final Block BROWN_ROAD_SIGN_VERTICAL_MIDDLE = registerWithItem("roadsign/brown/vertical_middle", new RoadSignBlock(Blocks.BROWN_CONCRETE, signSettings(Blocks.BROWN_CONCRETE, "roadsign/brown/vertical_middle")));
    public static final Block BROWN_ROAD_SIGN_VERTICAL_BOTTOM = registerWithItem("roadsign/brown/vertical_bottom", new RoadSignBlock(Blocks.BROWN_CONCRETE, signSettings(Blocks.BROWN_CONCRETE, "roadsign/brown/vertical_bottom")));
    public static final Block BROWN_ROAD_SIGN_SMALL_A = registerWithItem("roadsign/brown/small_a", new RoadSignBlock(Blocks.BROWN_CONCRETE, signSettings(Blocks.BROWN_CONCRETE, "roadsign/brown/small_a")));
    public static final Block BROWN_ROAD_SIGN_SMALL_B = registerWithItem("roadsign/brown/small_b", new RoadSignBlock(Blocks.BROWN_CONCRETE, signSettings(Blocks.BROWN_CONCRETE, "roadsign/brown/small_b")));

    public static final Block ORANGE_ROAD_SIGN_LEFT_TOP = registerWithItem("roadsign/orange/left_top", new RoadSignBlock(Blocks.ORANGE_CONCRETE, signSettings(Blocks.ORANGE_CONCRETE, "roadsign/orange/left_top")));
    public static final Block ORANGE_ROAD_SIGN_TOP = registerWithItem("roadsign/orange/top", new RoadSignBlock(Blocks.ORANGE_CONCRETE, signSettings(Blocks.ORANGE_CONCRETE, "roadsign/orange/top")));
    public static final Block ORANGE_ROAD_SIGN_RIGHT_TOP = registerWithItem("roadsign/orange/right_top", new RoadSignBlock(Blocks.ORANGE_CONCRETE, signSettings(Blocks.ORANGE_CONCRETE, "roadsign/orange/right_top")));
    public static final Block ORANGE_ROAD_SIGN_LEFT = registerWithItem("roadsign/orange/left", new RoadSignBlock(Blocks.ORANGE_CONCRETE, signSettings(Blocks.ORANGE_CONCRETE, "roadsign/orange/left")));
    public static final Block ORANGE_ROAD_SIGN_MIDDLE = registerWithItem("roadsign/orange/middle", new RoadSignBlock(Blocks.ORANGE_CONCRETE, signSettings(Blocks.ORANGE_CONCRETE, "roadsign/orange/middle")));
    public static final Block ORANGE_ROAD_SIGN_RIGHT = registerWithItem("roadsign/orange/right", new RoadSignBlock(Blocks.ORANGE_CONCRETE, signSettings(Blocks.ORANGE_CONCRETE, "roadsign/orange/right")));
    public static final Block ORANGE_ROAD_SIGN_LEFT_BOTTOM = registerWithItem("roadsign/orange/left_bottom", new RoadSignBlock(Blocks.ORANGE_CONCRETE, signSettings(Blocks.ORANGE_CONCRETE, "roadsign/orange/left_bottom")));
    public static final Block ORANGE_ROAD_SIGN_BOTTOM = registerWithItem("roadsign/orange/bottom", new RoadSignBlock(Blocks.ORANGE_CONCRETE, signSettings(Blocks.ORANGE_CONCRETE, "roadsign/orange/bottom")));
    public static final Block ORANGE_ROAD_SIGN_RIGHT_BOTTOM = registerWithItem("roadsign/orange/right_bottom", new RoadSignBlock(Blocks.ORANGE_CONCRETE, signSettings(Blocks.ORANGE_CONCRETE, "roadsign/orange/right_bottom")));
    public static final Block ORANGE_ROAD_SIGN_HORIZONTAL_LEFT = registerWithItem("roadsign/orange/horizontal_left", new RoadSignBlock(Blocks.ORANGE_CONCRETE, signSettings(Blocks.ORANGE_CONCRETE, "roadsign/orange/horizontal_left")));
    public static final Block ORANGE_ROAD_SIGN_HORIZONTAL_MIDDLE = registerWithItem("roadsign/orange/horizontal_middle", new RoadSignBlock(Blocks.ORANGE_CONCRETE, signSettings(Blocks.ORANGE_CONCRETE, "roadsign/orange/horizontal_middle")));
    public static final Block ORANGE_ROAD_SIGN_HORIZONTAL_RIGHT = registerWithItem("roadsign/orange/horizontal_right", new RoadSignBlock(Blocks.ORANGE_CONCRETE, signSettings(Blocks.ORANGE_CONCRETE, "roadsign/orange/horizontal_right")));
    public static final Block ORANGE_ROAD_SIGN_VERTICAL_TOP = registerWithItem("roadsign/orange/vertical_top", new RoadSignBlock(Blocks.ORANGE_CONCRETE, signSettings(Blocks.ORANGE_CONCRETE, "roadsign/orange/vertical_top")));
    public static final Block ORANGE_ROAD_SIGN_VERTICAL_MIDDLE = registerWithItem("roadsign/orange/vertical_middle", new RoadSignBlock(Blocks.ORANGE_CONCRETE, signSettings(Blocks.ORANGE_CONCRETE, "roadsign/orange/vertical_middle")));
    public static final Block ORANGE_ROAD_SIGN_VERTICAL_BOTTOM = registerWithItem("roadsign/orange/vertical_bottom", new RoadSignBlock(Blocks.ORANGE_CONCRETE, signSettings(Blocks.ORANGE_CONCRETE, "roadsign/orange/vertical_bottom")));
    public static final Block ORANGE_ROAD_SIGN_SMALL_A = registerWithItem("roadsign/orange/small_a", new RoadSignBlock(Blocks.ORANGE_CONCRETE, signSettings(Blocks.ORANGE_CONCRETE, "roadsign/orange/small_a")));
    public static final Block ORANGE_ROAD_SIGN_SMALL_B = registerWithItem("roadsign/orange/small_b", new RoadSignBlock(Blocks.ORANGE_CONCRETE, signSettings(Blocks.ORANGE_CONCRETE, "roadsign/orange/small_b")));

    /**
     * 指示牌方块的基础属性。
     *
     * <p>注册键必须在方块构造前设置，因此每个方块都需要独立的属性实例。</p>
     *
     * @param source 用于复制属性的原版方块
     * @param name   方块注册路径
     * @return 该方块专属的属性实例
     */
    private static BlockBehaviour.Properties signSettings(Block source, String name) {
        return BlockBehaviour.Properties.ofFullCopy(source)
                .setId(OcelotSignMod.blockKey(name))
                .strength(0.2f, 0.2f)
                .noOcclusion();
    }

    private static Block registerWithItem(String id, Block block) {
        Block registeredBlock = Registry.register(BuiltInRegistries.BLOCK, OcelotSignMod.id(id), block);
        registerBlockItem(id, registeredBlock);
        return registeredBlock;
    }

    private static void registerBlockItem(String id, Block block) {
        Registry.register(BuiltInRegistries.ITEM, OcelotSignMod.id(id),
                new RoadSignBlockItem(block, new Item.Properties()
                        .setId(OcelotSignMod.itemKey(id))
                        .useBlockDescriptionPrefix()));
    }

    public static void registerRoadSignBlocks() {
        OcelotSignMod.LOGGER.info("Registering Road Sign Blocks for " + OcelotSignMod.MOD_ID);
    }
}
