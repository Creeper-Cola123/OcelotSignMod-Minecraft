package bklmc.ocelotsign.block;

import bklmc.ocelotsign.OcelotSignMod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

/**
 * 版本校验方块注册中心
 *
 * <p>用于强制客户端更新至特定版本，缺少对应方块时无法进入服务器。
 */
public class VersionBlocks {
    public static final Block VERSION_BLOCK_1_0_1 = registerWithItem("version_block_1_0_1", new Block(BlockBehaviour.Properties.of()
            .setId(OcelotSignMod.blockKey("version_block_1_0_1"))));

    /**
     * 注册并创建方块对应的物品。
     *
     * @param id    方块 ID
     * @param block 方块实例
     * @return 已注册的方块
     */
    private static Block registerWithItem(String id, Block block) {
        Block registeredBlock = Registry.register(BuiltInRegistries.BLOCK, Identifier.fromNamespaceAndPath(OcelotSignMod.MOD_ID, id), block);
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
        Registry.register(BuiltInRegistries.ITEM, OcelotSignMod.id(id),
                new BlockItem(block, new Item.Properties()
                        .setId(OcelotSignMod.itemKey(id))
                        .useBlockDescriptionPrefix()));
    }

    /**
     * 初始化版本方块注册。
     */
    public static void registerVersionBlocks() {
        OcelotSignMod.LOGGER.info("Registering Version Blocks for " + OcelotSignMod.MOD_ID);
    }
}
