package bklmc.ocelotsign.block;

import bklmc.ocelotsign.OcelotSignMod;
import bklmc.ocelotsign.block.custom.PillarBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

/**
 * 立柱方块注册中心
 *
 * @see bklmc.ocelotsign.block.custom.PillarBlock
 */
public class PillarBlocks {
    private static RegistryKey<Block> getBlockKey(String id) {
        return RegistryKey.of(Registries.BLOCK.getKey(), Identifier.of(OcelotSignMod.MOD_ID, id));
    }

    private static RegistryKey<Item> getItemKey(String id) {
        return RegistryKey.of(Registries.ITEM.getKey(), Identifier.of(OcelotSignMod.MOD_ID, id));
    }

    private static AbstractBlock.Settings createSettings(String id) {
        return AbstractBlock.Settings.create()
                .strength(0.2f, 0.2f)
                .nonOpaque()
                .registryKey(getBlockKey(id));
    }

    public static final Block ROAD_PILLAR_FOUR_SIDES_A = registerWithItem("pillar/four_sides_a", PillarBlock.PillarType.FOUR_SIDES_A, "pillar/four_sides_a");
    public static final Block ROAD_PILLAR_FOUR_SIDES_A_TOP = registerWithItem("pillar/four_sides_a_top", PillarBlock.PillarType.FOUR_SIDES_A_TOP, "pillar/four_sides_a_top");
    public static final Block ROAD_PILLAR_FOUR_SIDES_D = registerWithItem("pillar/four_sides_d", PillarBlock.PillarType.FOUR_SIDES_D, "pillar/four_sides_d");
    public static final Block ROAD_PILLAR_FOUR_SIDES_D_LEFT = registerWithItem("pillar/four_sides_d_left", PillarBlock.PillarType.FOUR_SIDES_D_LEFT, "pillar/four_sides_d_left");
    public static final Block ROAD_PILLAR_FOUR_SIDES_D_RIGHT = registerWithItem("pillar/four_sides_d_right", PillarBlock.PillarType.FOUR_SIDES_D_RIGHT, "pillar/four_sides_d_right");
    public static final Block ROAD_PILLAR_THREE_SIDES_A_LEFT = registerWithItem("pillar/three_sides_a_left", PillarBlock.PillarType.THREE_SIDES_A_LEFT, "pillar/three_sides_a_left");
    public static final Block ROAD_PILLAR_THREE_SIDES_A_RIGHT = registerWithItem("pillar/three_sides_a_right", PillarBlock.PillarType.THREE_SIDES_A_RIGHT, "pillar/three_sides_a_right");
    public static final Block ROAD_PILLAR_THREE_SIDES_A_TOP_LEFT = registerWithItem("pillar/three_sides_a_top_left", PillarBlock.PillarType.THREE_SIDES_A_TOP_LEFT, "pillar/three_sides_a_top_left");
    public static final Block ROAD_PILLAR_THREE_SIDES_A_TOP_RIGHT = registerWithItem("pillar/three_sides_a_top_right", PillarBlock.PillarType.THREE_SIDES_A_TOP_RIGHT, "pillar/three_sides_a_top_right");
    public static final Block ROAD_PILLAR_HORIZONTAL_A = registerWithItem("pillar/horizontal_a", PillarBlock.PillarType.HORIZONTAL_A, "pillar/horizontal_a");
    public static final Block ROAD_PILLAR_HORIZONTAL_D = registerWithItem("pillar/horizontal_d", PillarBlock.PillarType.HORIZONTAL_D, "pillar/horizontal_d");
    public static final Block ROAD_PILLAR_HORIZONTAL_D_UP = registerWithItem("pillar/horizontal_d_up", PillarBlock.PillarType.HORIZONTAL_D_UP, "pillar/horizontal_d_up");
    public static final Block ROAD_PILLAR_HORIZONTAL_D_DOWN = registerWithItem("pillar/horizontal_d_down", PillarBlock.PillarType.HORIZONTAL_D_DOWN, "pillar/horizontal_d_down");
    public static final Block ROAD_PILLAR_UPRIGHT_A = registerWithItem("pillar/upright_a", PillarBlock.PillarType.UPRIGHT_A, "pillar/upright_a");
    public static final Block ROAD_PILLAR_UPRIGHT_B_LEFT = registerWithItem("pillar/upright_b_left", PillarBlock.PillarType.UPRIGHT_B_LEFT, "pillar/upright_b_left");
    public static final Block ROAD_PILLAR_UPRIGHT_B_RIGHT = registerWithItem("pillar/upright_b_right", PillarBlock.PillarType.UPRIGHT_B_RIGHT, "pillar/upright_b_right");
    public static final Block ROAD_PILLAR_UPRIGHT_B_DOUBLE = registerWithItem("pillar/upright_b_double", PillarBlock.PillarType.UPRIGHT_B_DOUBLE, "pillar/upright_b_double");
    public static final Block ROAD_PILLAR_UPRIGHT_C = registerWithItem("pillar/upright_c", PillarBlock.PillarType.UPRIGHT_C, "pillar/upright_c");
    public static final Block ROAD_PILLAR_UPRIGHT_C_HALF = registerWithItem("pillar/upright_c_half", PillarBlock.PillarType.UPRIGHT_C_HALF, "pillar/upright_c_half");
    public static final Block ROAD_PILLAR_UPRIGHT_C_INCLINED_LEFT = registerWithItem("pillar/upright_c_inclined_left", PillarBlock.PillarType.UPRIGHT_C_INCLINED_LEFT, "pillar/upright_c_inclined_left");
    public static final Block ROAD_PILLAR_UPRIGHT_C_INCLINED_RIGHT = registerWithItem("pillar/upright_c_inclined_right", PillarBlock.PillarType.UPRIGHT_C_INCLINED_RIGHT, "pillar/upright_c_inclined_right");

    /**
     * 注册方块并同时注册对应物品。
     *
     * @param id    注册 ID
     * @param type  立柱类型
     * @param settingsId 用于设置 registryKey 的 ID
     * @return 已注册的方块
     */
    private static Block registerWithItem(String id, PillarBlock.PillarType type, String settingsId) {
        PillarBlock block = new PillarBlock(type, createSettings(settingsId));
        Block registeredBlock = Registry.register(Registries.BLOCK, getBlockKey(id), block);
        Registry.register(Registries.ITEM, getItemKey(id),
                new BlockItem(block, new Item.Settings().registryKey(getItemKey(id))));
        return registeredBlock;
    }

    /**
     * 初始化立柱方块注册。
     */
    public static void registerPillarBlocks() {
        OcelotSignMod.LOGGER.info("Registering Pillar Blocks for " + OcelotSignMod.MOD_ID);
    }
}
