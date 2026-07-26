package bklmc.ocelotsign.block;

import bklmc.ocelotsign.OcelotSignMod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

/**
 * 动态方块注册中心
 *
 * <p>用于运行时动态创建的方块。
 */
public class DynamicBlocks {
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
        Block registeredBlock = Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(OcelotSignMod.MOD_ID, id), block);
        Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(OcelotSignMod.MOD_ID, id), new BlockItem(block, new Item.Properties()));
        return registeredBlock;
    }
}
