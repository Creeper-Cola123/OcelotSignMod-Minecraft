package bklmc.ocelotsign.blockentity;

import bklmc.ocelotsign.OcelotSignMod;
import bklmc.ocelotsign.block.custom.RoadSignBlock;
import bklmc.ocelotsign.block.custom.WallRoadSignBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;

/**
 * 方块实体类型注册中心
 *
 * @see RoadSignBlockEntity
 * @see WallRoadSignBlockEntity
 * @see CustomModelBlockEntity
 */
public class ModBlockEntities {
    public static BlockEntityType<RoadSignBlockEntity> ROAD_SIGN_BLOCK_ENTITY;
    public static BlockEntityType<WallRoadSignBlockEntity> WALL_ROAD_SIGN_BLOCK_ENTITY;

    public static void registerModBlockEntities() {
        ROAD_SIGN_BLOCK_ENTITY = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            OcelotSignMod.id("road_sign_block_entity"),
            FabricBlockEntityTypeBuilder.create(RoadSignBlockEntity::new,
                    RoadSignBlock.ROAD_SIGNS.toArray(new RoadSignBlock[0])).build()
        );

        WALL_ROAD_SIGN_BLOCK_ENTITY = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            OcelotSignMod.id("wall_road_sign_block_entity"),
            FabricBlockEntityTypeBuilder.create(WallRoadSignBlockEntity::new,
                    WallRoadSignBlock.WALL_ROAD_SIGNS.toArray(new WallRoadSignBlock[0])).build()
        );

        OcelotSignMod.LOGGER.info("Registered {} RoadSignBlock instances", RoadSignBlock.ROAD_SIGNS.size());
        OcelotSignMod.LOGGER.info("Registered {} WallRoadSignBlock instances", WallRoadSignBlock.WALL_ROAD_SIGNS.size());
    }
}
