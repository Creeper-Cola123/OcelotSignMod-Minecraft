package bklmc.ocelotsign.block;

import bklmc.ocelotsign.OcelotSignMod;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

/**
 * 动态方块注册中心
 *
 * <p>用于运行时动态创建的方块。
 */
public class DynamicBlocks {
    private static RegistryKey<Item> getItemKey(String id) {
        return RegistryKey.of(Registries.ITEM.getKey(), Identifier.of(OcelotSignMod.MOD_ID, id));
    }

    /**
     * 初始化动态方块注册。
     */
    public static void registerDynamicBlocks() {
        OcelotSignMod.LOGGER.info("Registering Dynamic Blocks for " + OcelotSignMod.MOD_ID);
    }

    /**
     * 注册方块并同时注册其物品形式。
     *
     * @param id    方块 ID
     * @param block 方块实例
     * @return 已注册的方块
     */
    private static Block registerBlockWithItem(String id, Block block) {
        Block registeredBlock = Registry.register(Registries.BLOCK, Identifier.of(OcelotSignMod.MOD_ID, id), block);
        Registry.register(Registries.ITEM, getItemKey(id), new BlockItem(block, new Item.Settings().registryKey(getItemKey(id))));
        return registeredBlock;
    }
}
