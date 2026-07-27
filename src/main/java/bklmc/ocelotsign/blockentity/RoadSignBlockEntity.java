package bklmc.ocelotsign.blockentity;

import bklmc.ocelotsign.integration.mishanguc.MishangAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 道路指示牌方块实体
 *
 * @see WallRoadSignBlockEntity
 * @see ModBlockEntities#ROAD_SIGN_BLOCK_ENTITY
 */
public class RoadSignBlockEntity extends pers.solid.mishang.uc.blockentity.WallSignBlockEntity {
    private static final Logger LOGGER = LoggerFactory.getLogger(RoadSignBlockEntity.class);

    public RoadSignBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ROAD_SIGN_BLOCK_ENTITY, pos, state);
        if (!MishangAccess.isAvailable()) {
            LOGGER.warn("未检测到 Mishanguc；道路指示牌功能可能受限");
        }
    }

    @Override
    public float getHeight() {
        return 16;
    }

    @Override
    public pers.solid.mishang.uc.text.TextContext createDefaultTextContext() {
        pers.solid.mishang.uc.text.TextContext context = new pers.solid.mishang.uc.text.TextContext();
        context.size = 8;
        return context;
    }
}
