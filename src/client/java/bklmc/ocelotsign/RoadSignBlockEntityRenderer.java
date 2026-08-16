package bklmc.ocelotsign;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import pers.solid.mishang.uc.block.WallSignBlock;
import pers.solid.mishang.uc.blockentity.WallSignBlockEntity;
import pers.solid.mishang.uc.text.TextContext;

/**
 * 立柱式道路指示牌方块实体渲染器
 *
 * @param <T> 渲染的方块实体类型
 * @see BlockEntityRenderer
 * @see WallSignBlockEntity
 */
@Environment(EnvType.CLIENT)
public class RoadSignBlockEntityRenderer<T extends WallSignBlockEntity>
        implements BlockEntityRenderer<T> {

    private final BlockEntityRendererFactory.Context ctx;

    /**
     * 构造渲染器。
     *
     * @param ctx 方块实体渲染器工厂上下文
     */
    public RoadSignBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.ctx = ctx;
    }

    /**
     * 渲染方块实体。
     *
     * @param entity 方块实体
     * @param tickDelta 帧插值时间
     * @param matrices 矩阵栈
     * @param vertexConsumers 顶点消费者提供者
     * @param light 光照值
     * @param overlay 覆盖贴图
     */
    @Override
    public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        final BlockState state = entity.getCachedState();
        final Direction facing = state.get(WallSignBlock.FACING);

        // 发光时使用最大光照
        if (entity.glowing) {
            light = 15728880;
        }

        // 对齐方块中心
        matrices.translate(0.5, 0.5, 0.5);
        // 按朝向旋转
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-facing.getPositiveHorizontalDegrees()));

        // 缩放并偏移至方块表面
        matrices.scale(1 / 16f, -1 / 16f, 1 / 16f);
        float zOffset = 8.0f + 0.0125f;
        matrices.translate(0, 0, zOffset);

        // 渲染所有文本
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
