package bklmc.ocelotsign.block;

import bklmc.ocelotsign.OcelotSignMod;
import bklmc.ocelotsign.block.custom.PillarBlock;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

/**
 * 立柱方块注册中心
 *
 * @see bklmc.ocelotsign.block.custom.PillarBlock
 */
public class PillarBlocks {
    public static final Block ROAD_PILLAR_FOUR_SIDES_A = registerWithItem("pillar/four_sides_a", new PillarBlock(PillarBlock.PillarType.FOUR_SIDES_A, pillarSettings("pillar/four_sides_a")));
    public static final Block ROAD_PILLAR_FOUR_SIDES_A_TOP = registerWithItem("pillar/four_sides_a_top", new PillarBlock(PillarBlock.PillarType.FOUR_SIDES_A_TOP, pillarSettings("pillar/four_sides_a_top")));
    public static final Block ROAD_PILLAR_FOUR_SIDES_D = registerWithItem("pillar/four_sides_d", new PillarBlock(PillarBlock.PillarType.FOUR_SIDES_D, pillarSettings("pillar/four_sides_d")));
    public static final Block ROAD_PILLAR_FOUR_SIDES_D_LEFT = registerWithItem("pillar/four_sides_d_left", new PillarBlock(PillarBlock.PillarType.FOUR_SIDES_D_LEFT, pillarSettings("pillar/four_sides_d_left")));
    public static final Block ROAD_PILLAR_FOUR_SIDES_D_RIGHT = registerWithItem("pillar/four_sides_d_right", new PillarBlock(PillarBlock.PillarType.FOUR_SIDES_D_RIGHT, pillarSettings("pillar/four_sides_d_right")));
    public static final Block ROAD_PILLAR_THREE_SIDES_A_LEFT = registerWithItem("pillar/three_sides_a_left", new PillarBlock(PillarBlock.PillarType.THREE_SIDES_A_LEFT, pillarSettings("pillar/three_sides_a_left")));
    public static final Block ROAD_PILLAR_THREE_SIDES_A_RIGHT = registerWithItem("pillar/three_sides_a_right", new PillarBlock(PillarBlock.PillarType.THREE_SIDES_A_RIGHT, pillarSettings("pillar/three_sides_a_right")));
    public static final Block ROAD_PILLAR_THREE_SIDES_A_TOP_LEFT = registerWithItem("pillar/three_sides_a_top_left", new PillarBlock(PillarBlock.PillarType.THREE_SIDES_A_TOP_LEFT, pillarSettings("pillar/three_sides_a_top_left")));
    public static final Block ROAD_PILLAR_THREE_SIDES_A_TOP_RIGHT = registerWithItem("pillar/three_sides_a_top_right", new PillarBlock(PillarBlock.PillarType.THREE_SIDES_A_TOP_RIGHT, pillarSettings("pillar/three_sides_a_top_right")));
    public static final Block ROAD_PILLAR_HORIZONTAL_A = registerWithItem("pillar/horizontal_a", new PillarBlock(PillarBlock.PillarType.HORIZONTAL_A, pillarSettings("pillar/horizontal_a")));
    public static final Block ROAD_PILLAR_HORIZONTAL_D = registerWithItem("pillar/horizontal_d", new PillarBlock(PillarBlock.PillarType.HORIZONTAL_D, pillarSettings("pillar/horizontal_d")));
    public static final Block ROAD_PILLAR_HORIZONTAL_D_UP = registerWithItem("pillar/horizontal_d_up", new PillarBlock(PillarBlock.PillarType.HORIZONTAL_D_UP, pillarSettings("pillar/horizontal_d_up")));
    public static final Block ROAD_PILLAR_HORIZONTAL_D_DOWN = registerWithItem("pillar/horizontal_d_down", new PillarBlock(PillarBlock.PillarType.HORIZONTAL_D_DOWN, pillarSettings("pillar/horizontal_d_down")));
    public static final Block ROAD_PILLAR_UPRIGHT_A = registerWithItem("pillar/upright_a", new PillarBlock(PillarBlock.PillarType.UPRIGHT_A, pillarSettings("pillar/upright_a")));
    public static final Block ROAD_PILLAR_UPRIGHT_B_LEFT = registerWithItem("pillar/upright_b_left", new PillarBlock(PillarBlock.PillarType.UPRIGHT_B_LEFT, pillarSettings("pillar/upright_b_left")));
    public static final Block ROAD_PILLAR_UPRIGHT_B_RIGHT = registerWithItem("pillar/upright_b_right", new PillarBlock(PillarBlock.PillarType.UPRIGHT_B_RIGHT, pillarSettings("pillar/upright_b_right")));
    public static final Block ROAD_PILLAR_UPRIGHT_B_DOUBLE = registerWithItem("pillar/upright_b_double", new PillarBlock(PillarBlock.PillarType.UPRIGHT_B_DOUBLE, pillarSettings("pillar/upright_b_double")));
    public static final Block ROAD_PILLAR_UPRIGHT_C = registerWithItem("pillar/upright_c", new PillarBlock(PillarBlock.PillarType.UPRIGHT_C, pillarSettings("pillar/upright_c")));
    public static final Block ROAD_PILLAR_UPRIGHT_C_HALF = registerWithItem("pillar/upright_c_half", new PillarBlock(PillarBlock.PillarType.UPRIGHT_C_HALF, pillarSettings("pillar/upright_c_half")));
    public static final Block ROAD_PILLAR_UPRIGHT_C_INCLINED_LEFT = registerWithItem("pillar/upright_c_inclined_left", new PillarBlock(PillarBlock.PillarType.UPRIGHT_C_INCLINED_LEFT, pillarSettings("pillar/upright_c_inclined_left")));
    public static final Block ROAD_PILLAR_UPRIGHT_C_INCLINED_RIGHT = registerWithItem("pillar/upright_c_inclined_right", new PillarBlock(PillarBlock.PillarType.UPRIGHT_C_INCLINED_RIGHT, pillarSettings("pillar/upright_c_inclined_right")));

    /**
     * 注册方块并同时注册对应物品。
     *
     * @param id    注册 ID
     * @param block 待注册的方块
     * @return 已注册的方块
     */
    /**
     * 立柱方块的基础属性。
     *
     * <p>注册键必须在方块构造前设置，因此每个方块都需要独立的属性实例。</p>
     *
     * @param name 方块注册路径
     * @return 该方块专属的属性实例
     */
    private static BlockBehaviour.Properties pillarSettings(String name) {
        return BlockBehaviour.Properties.of()
                .setId(OcelotSignMod.blockKey(name))
                .strength(0.2f, 0.2f)
                .noOcclusion();
    }

    private static Block registerWithItem(String id, Block block) {
        Block registeredBlock = Registry.register(BuiltInRegistries.BLOCK, Identifier.fromNamespaceAndPath(OcelotSignMod.MOD_ID, id), block);
        registerBlockItem(id, registeredBlock);
        return registeredBlock;
    }

    /**
     * 注册方块对应的物品。
     *
     * @param id    注册 ID
     * @param block 对应的方块
     */
    private static void registerBlockItem(String id, Block block) {
        Registry.register(BuiltInRegistries.ITEM, OcelotSignMod.id(id),
                new BlockItem(block, new Item.Properties()
                        .setId(OcelotSignMod.itemKey(id))
                        .useBlockDescriptionPrefix()));
    }

    /**
     * 初始化立柱方块注册。
     */
    public static void registerPillarBlocks() {
        OcelotSignMod.LOGGER.info("Registering Pillar Blocks for " + OcelotSignMod.MOD_ID);
    }
}
