package bklmc.ocelotsign.block;

import bklmc.ocelotsign.OcelotSignMod;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registry;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

/**
 * 动态方块注册工具类
 *
 * <p>提供动态生成和注册方块的便捷方法，支持运行时创建自定义方块。
 */
public class DynamicBlocks {
    /**
     * 注册所有动态方块
     */
    public static void registerDynamicBlocks() {
        OcelotSignMod.LOGGER.info("Registering Dynamic Blocks for " + OcelotSignMod.MOD_ID);
    }

    /**
     * 注册方块并创建对应的物品
     *
     * @param id    方块ID
     * @param block 方块实例
     * @return 已注册的方块实例
     */
    private static Block registerBlockWithItem(String id, Block block) {
        Block registeredBlock = Registry.register(Registries.BLOCK, new Identifier(OcelotSignMod.MOD_ID, id), block);
        Registry.register(Registries.ITEM, new Identifier(OcelotSignMod.MOD_ID, id), new BlockItem(block, new Item.Settings()));
        return registeredBlock;
    }
}
