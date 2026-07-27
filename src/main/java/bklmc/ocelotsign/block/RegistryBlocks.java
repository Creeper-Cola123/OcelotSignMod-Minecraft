package bklmc.ocelotsign.block;

import bklmc.ocelotsign.OcelotSignMod;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.Identifier;

/**
 * 注册表占位方块注册中心
 *
 * <p>预留的方块注册位，暂无实际功能。
 */
public class RegistryBlocks {
    /**
     * 注册并创建方块对应的物品。
     *
     * @param id    方块 ID
     * @param block 方块实例
     * @return 已注册的方块
     */
    private static Block registerWithItem(String id, Block block) {
        Block registeredBlock = Registry.register(Registry.BLOCK, new Identifier(OcelotSignMod.MOD_ID, id), block);
        registerBlockItem(id, registeredBlock);
        return registeredBlock;
    }

    /**
     * 注册方块对应的物品。
     *
     * @param id    物品 ID
     * @param block 对应的方块
     */
    private static void registerBlockItem(String id, Block block) {
        Registry.register(Registry.ITEM, new Identifier(OcelotSignMod.MOD_ID, id),
                new BlockItem(block, new Item.Settings()));
    }

    /**
     * 初始化注册占位方块注册。
     */
    public static void registerRegistryBlocks() {
        OcelotSignMod.LOGGER.info("Registering Registry Blocks for " + OcelotSignMod.MOD_ID);
    }
}
