package bklmc.ocelotsign.blockentity;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 墙面道路指示牌方块实体
 *
 * @see RoadSignBlockEntity
 * @see ModBlockEntities#WALL_ROAD_SIGN_BLOCK_ENTITY
 */
public class WallRoadSignBlockEntity extends pers.solid.mishang.uc.blockentity.WallSignBlockEntity {
    private static final Logger LOGGER = LoggerFactory.getLogger(WallRoadSignBlockEntity.class);

    public WallRoadSignBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.WALL_ROAD_SIGN_BLOCK_ENTITY, pos, state);
        if (!bklmc.ocelotsign.integration.mishanguc.MishangAccess.isAvailable()) {
            LOGGER.warn("未检测到 Mishanguc；墙上道路指示牌功能可能受限");
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
