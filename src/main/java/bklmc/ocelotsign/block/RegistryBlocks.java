package bklmc.ocelotsign.block;

import bklmc.ocelotsign.OcelotSignMod;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registry;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

/**
 * 注册表方块工具类
 *
 * <p>提供基础方块注册功能，支持快捷注册方块及其对应的物品。
 */
public class RegistryBlocks {
    /**
     * 注册方块并创建对应的物品
     *
     * @param id    方块ID
     * @param block 方块实例
     * @return 已注册的方块实例
     */
    private static Block registerWithItem(String id, Block block) {
        Block registeredBlock = Registry.register(Registries.BLOCK, new Identifier(OcelotSignMod.MOD_ID, id), block);
        registerBlockItem(id, registeredBlock);
        return registeredBlock;
    }

    /**
     * 注册方块物品
     *
     * @param id    方块ID
     * @param block 方块实例
     */
    private static void registerBlockItem(String id, Block block) {
        Registry.register(Registries.ITEM, new Identifier(OcelotSignMod.MOD_ID, id),
                new BlockItem(block, new Item.Settings()));
    }

    /**
     * 注册所有注册表方块
     */
    public static void registerRegistryBlocks() {
        OcelotSignMod.LOGGER.info("Registering Registry Blocks for " + OcelotSignMod.MOD_ID);
    }
}
