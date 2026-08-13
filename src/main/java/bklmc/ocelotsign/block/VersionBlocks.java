package bklmc.ocelotsign.block;

import bklmc.ocelotsign.OcelotSignMod;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.Identifier;

/**
 * 版本校验方块注册中心
 *
 * <p>用于强制客户端更新至特定版本，缺少对应方块时无法进入服务器。
 */
public class VersionBlocks {
    public static final Block   VERSION_BLOCK_1_0_0 = registerWithItem("version_block_1_0_0", new Block(AbstractBlock.Settings.of(Material.STONE)));

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
     * 初始化版本方块注册。
     */
    public static void registerVersionBlocks() {
        OcelotSignMod.LOGGER.info("Registering Version Blocks for " + OcelotSignMod.MOD_ID);
    }
}
