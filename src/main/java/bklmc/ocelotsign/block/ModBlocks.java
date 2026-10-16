package bklmc.ocelotsign.block;

import bklmc.ocelotsign.OcelotSignMod;

/**
 * 模组方块注册入口
 *
 * @see VersionBlocks
 * @see RegistryBlocks
 * @see RoadSignBlocks
 * @see WallRoadSignBlocks
 * @see PillarBlocks
 * @see ArrowBlocks
 * @see ArrowBlocksLarge
 * @see ArrowBlocksStyle2
 * @see ArrowBlocksStyle3
 * @see DynamicBlocks
 */
public class ModBlocks {
    public static void registerModBlocks() {
        OcelotSignMod.LOGGER.info("Registering Mod Blocks for " + OcelotSignMod.MOD_ID);

        VersionBlocks.registerVersionBlocks();
        RegistryBlocks.registerRegistryBlocks();
        RoadSignBlocks.registerRoadSignBlocks();
        WallRoadSignBlocks.registerWallRoadSignBlocks();
        PillarBlocks.registerPillarBlocks();
        ArrowBlocks.registerArrowBlocks();
        ArrowBlocksLarge.register();
        ArrowBlocksStyle2.register();
        ArrowBlocksStyle3.register();
        DynamicBlocks.registerDynamicBlocks();
    }
}
