package bklmc.ocelotsign;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;
import pers.solid.mishang.uc.block.WallSignBlock;
import pers.solid.mishang.uc.blockentity.WallSignBlockEntity;
import pers.solid.mishang.uc.text.TextContext;

/**
 * 立柱式道路指示牌方块实体渲染器 (1.19.2 兼容版)
 *
 * @param <T> 渲染的方块实体类型
 */
@Environment(EnvType.CLIENT)
public class RoadSignBlockEntityRenderer<T extends WallSignBlockEntity>
        implements BlockEntityRenderer<T> {

    private final BlockEntityRendererFactory.Context ctx;

    public RoadSignBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        final BlockState state = entity.getCachedState();
        final Direction facing = state.get(WallSignBlock.FACING);

        if (entity.glowing) {
            light = 15728880;
        }

        matrices.translate(0.5, 0.5, 0.5);
        // 1.19.2: Vec3f 替代 RotationAxis
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-facing.asRotation()));

        matrices.scale(1 / 16f, -1 / 16f, 1 / 16f);
        float zOffset = 8.0f + 0.0125f;
        matrices.translate(0, 0, zOffset);

        for (TextContext textContext : entity.textContexts) {
            textContext.draw(
                    ctx.getTextRenderer(),
                    matrices,
                    vertexConsumers,
                    light,
                    16,
                    entity.getHeight()
            );
        }
    }
}