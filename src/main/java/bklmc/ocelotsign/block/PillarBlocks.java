package bklmc.ocelotsign.block;

import bklmc.ocelotsign.OcelotSignMod;
import bklmc.ocelotsign.block.custom.PillarBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

/**
 * 立柱方块注册中心
 *
 * @see bklmc.ocelotsign.block.custom.PillarBlock
 */
public class PillarBlocks {
    public static final Block ROAD_PILLAR_FOUR_SIDES_A = registerWithItem("pillar/four_sides_a", new PillarBlock(PillarBlock.PillarType.FOUR_SIDES_A, AbstractBlock.Settings.create().strength(0.2f,0.2f).nonOpaque()));
    public static final Block ROAD_PILLAR_FOUR_SIDES_A_TOP = registerWithItem("pillar/four_sides_a_top", new PillarBlock(PillarBlock.PillarType.FOUR_SIDES_A_TOP, AbstractBlock.Settings.create().strength(0.2f,0.2f).nonOpaque()));
    public static final Block ROAD_PILLAR_FOUR_SIDES_D = registerWithItem("pillar/four_sides_d", new PillarBlock(PillarBlock.PillarType.FOUR_SIDES_D, AbstractBlock.Settings.create().strength(0.2f,0.2f).nonOpaque()));
    public static final Block ROAD_PILLAR_FOUR_SIDES_D_LEFT = registerWithItem("pillar/four_sides_d_left", new PillarBlock(PillarBlock.PillarType.FOUR_SIDES_D_LEFT, AbstractBlock.Settings.create().strength(0.2f,0.2f).nonOpaque()));
    public static final Block ROAD_PILLAR_FOUR_SIDES_D_RIGHT = registerWithItem("pillar/four_sides_d_right", new PillarBlock(PillarBlock.PillarType.FOUR_SIDES_D_RIGHT, AbstractBlock.Settings.create().strength(0.2f,0.2f).nonOpaque()));
    public static final Block ROAD_PILLAR_THREE_SIDES_A_LEFT = registerWithItem("pillar/three_sides_a_left", new PillarBlock(PillarBlock.PillarType.THREE_SIDES_A_LEFT, AbstractBlock.Settings.create().strength(0.2f,0.2f).nonOpaque()));
    public static final Block ROAD_PILLAR_THREE_SIDES_A_RIGHT = registerWithItem("pillar/three_sides_a_right", new PillarBlock(PillarBlock.PillarType.THREE_SIDES_A_RIGHT, AbstractBlock.Settings.create().strength(0.2f,0.2f).nonOpaque()));
    public static final Block ROAD_PILLAR_THREE_SIDES_A_TOP_LEFT = registerWithItem("pillar/three_sides_a_top_left", new PillarBlock(PillarBlock.PillarType.THREE_SIDES_A_TOP_LEFT, AbstractBlock.Settings.create().strength(0.2f,0.2f).nonOpaque()));
    public static final Block ROAD_PILLAR_THREE_SIDES_A_TOP_RIGHT = registerWithItem("pillar/three_sides_a_top_right", new PillarBlock(PillarBlock.PillarType.THREE_SIDES_A_TOP_RIGHT, AbstractBlock.Settings.create().strength(0.2f,0.2f).nonOpaque()));
    public static final Block ROAD_PILLAR_HORIZONTAL_A = registerWithItem("pillar/horizontal_a", new PillarBlock(PillarBlock.PillarType.HORIZONTAL_A, AbstractBlock.Settings.create().strength(0.2f,0.2f).nonOpaque()));
    public static final Block ROAD_PILLAR_HORIZONTAL_D = registerWithItem("pillar/horizontal_d", new PillarBlock(PillarBlock.PillarType.HORIZONTAL_D, AbstractBlock.Settings.create().strength(0.2f,0.2f).nonOpaque()));
    public static final Block ROAD_PILLAR_HORIZONTAL_D_UP = registerWithItem("pillar/horizontal_d_up", new PillarBlock(PillarBlock.PillarType.HORIZONTAL_D_UP, AbstractBlock.Settings.create().strength(0.2f,0.2f).nonOpaque()));
    public static final Block ROAD_PILLAR_HORIZONTAL_D_DOWN = registerWithItem("pillar/horizontal_d_down", new PillarBlock(PillarBlock.PillarType.HORIZONTAL_D_DOWN, AbstractBlock.Settings.create().strength(0.2f,0.2f).nonOpaque()));
    public static final Block ROAD_PILLAR_UPRIGHT_A = registerWithItem("pillar/upright_a", new PillarBlock(PillarBlock.PillarType.UPRIGHT_A, AbstractBlock.Settings.create().strength(0.2f,0.2f).nonOpaque()));
    public static final Block ROAD_PILLAR_UPRIGHT_B_LEFT = registerWithItem("pillar/upright_b_left", new PillarBlock(PillarBlock.PillarType.UPRIGHT_B_LEFT, AbstractBlock.Settings.create().strength(0.2f,0.2f).nonOpaque()));
    public static final Block ROAD_PILLAR_UPRIGHT_B_RIGHT = registerWithItem("pillar/upright_b_right", new PillarBlock(PillarBlock.PillarType.UPRIGHT_B_RIGHT, AbstractBlock.Settings.create().strength(0.2f,0.2f).nonOpaque()));
    public static final Block ROAD_PILLAR_UPRIGHT_B_DOUBLE = registerWithItem("pillar/upright_b_double", new PillarBlock(PillarBlock.PillarType.UPRIGHT_B_DOUBLE, AbstractBlock.Settings.create().strength(0.2f,0.2f).nonOpaque()));
    public static final Block ROAD_PILLAR_UPRIGHT_C = registerWithItem("pillar/upright_c", new PillarBlock(PillarBlock.PillarType.UPRIGHT_C, AbstractBlock.Settings.create().strength(0.2f,0.2f).nonOpaque()));
    public static final Block ROAD_PILLAR_UPRIGHT_C_HALF = registerWithItem("pillar/upright_c_half", new PillarBlock(PillarBlock.PillarType.UPRIGHT_C_HALF, AbstractBlock.Settings.create().strength(0.2f,0.2f).nonOpaque()));
    public static final Block ROAD_PILLAR_UPRIGHT_C_INCLINED_LEFT = registerWithItem("pillar/upright_c_inclined_left", new PillarBlock(PillarBlock.PillarType.UPRIGHT_C_INCLINED_LEFT, AbstractBlock.Settings.create().strength(0.2f,0.2f).nonOpaque()));
    public static final Block ROAD_PILLAR_UPRIGHT_C_INCLINED_RIGHT = registerWithItem("pillar/upright_c_inclined_right", new PillarBlock(PillarBlock.PillarType.UPRIGHT_C_INCLINED_RIGHT, AbstractBlock.Settings.create().strength(0.2f,0.2f).nonOpaque()));

    /**
     * 注册方块并同时注册对应物品。
     *
     * @param id    注册 ID
     * @param block 待注册的方块
     * @return 已注册的方块
     */
    private static Block registerWithItem(String id, Block block) {
        Block registeredBlock = Registry.register(Registries.BLOCK, new Identifier(OcelotSignMod.MOD_ID, id), block);
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
        Registry.register(Registries.ITEM, new Identifier(OcelotSignMod.MOD_ID, id),
                new BlockItem(block, new Item.Settings()));
    }

    /**
     * 初始化立柱方块注册。
     */
    public static void registerPillarBlocks() {
        OcelotSignMod.LOGGER.info("Registering Pillar Blocks for " + OcelotSignMod.MOD_ID);
    }
}
