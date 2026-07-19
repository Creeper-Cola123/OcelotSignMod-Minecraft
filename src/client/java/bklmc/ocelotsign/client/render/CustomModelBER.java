package bklmc.ocelotsign.client.render;

import bklmc.ocelotsign.blockentity.CustomModelBlockEntity;
import bklmc.ocelotsign.client.model.ModelRegistryManager;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;

/**
 * 自定义模型方块实体渲染器
 *
 * @see ModelRegistryManager
 */
public class CustomModelBER implements BlockEntityRenderer<CustomModelBlockEntity> {

    /**
     * 构造渲染器。
     *
     * @param ctx 渲染器工厂上下文
     */
    public CustomModelBER(BlockEntityRendererFactory.Context ctx) {}

    /**
     * 渲染方块实体。
     *
     * @param entity 方块实体
     * @param tickDelta 帧间时间差
     * @param matrices 矩阵栈
     * @param vertexConsumers 顶点消费者提供者
     * @param light 光照
     * @param overlay 覆盖层
     */
    @Override
    public void render(CustomModelBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();

        String modelId = entity.getModelId();
        BlockState state = entity.getCachedState();
        Direction facing = state.contains(Properties.HORIZONTAL_FACING) ? state.get(Properties.HORIZONTAL_FACING) : Direction.NORTH;
        MinecraftClient client = MinecraftClient.getInstance();

        boolean hasModelId = modelId != null && !modelId.isEmpty();
        boolean hasValidModel = false;

        if (hasModelId) {
            Identifier modelIdentifier = ModelRegistryManager.getModelIdentifier(modelId);
            BakedModel bakedModel = client.getBakedModelManager().getModel(modelIdentifier);

            if (bakedModel != null && bakedModel != client.getBakedModelManager().getMissingModel()) {
                matrices.translate(0.5, 0.5, 0.5);
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-facing.asRotation()));
                matrices.translate(-0.5, -0.5, -0.5);
                renderModel(entity, state, matrices, vertexConsumers, bakedModel, light, overlay);
                hasValidModel = true;
            }
        }

        if (!hasValidModel) {
            renderRotatingFallback(entity, state, matrices, vertexConsumers, tickDelta, light, overlay);
        }

        matrices.pop();
    }

    /** 渲染烘焙模型。 */
    private void renderModel(CustomModelBlockEntity entity, BlockState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, BakedModel model, int light, int overlay) {
        MinecraftClient client = MinecraftClient.getInstance();
        VertexConsumer buffer = vertexConsumers.getBuffer(RenderLayer.getCutout());
        client.getBlockRenderManager().getModelRenderer().render(
                entity.getWorld(),
                model,
                state,
                entity.getPos(),
                matrices,
                buffer,
                false,
                entity.getWorld().random,
                state.getRenderingSeed(entity.getPos()),
                overlay
        );
    }

    /** 渲染旋转的 fallback 模型。 */
    private void renderRotatingFallback(CustomModelBlockEntity entity, BlockState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, float tickDelta, int light, int overlay) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null) return;

        matrices.push();

        float time = client.world.getTime() + tickDelta;
        matrices.translate(0.5, 0.5, 0.5);
        // 绕Y轴旋转
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(time * 5.0f));
        matrices.translate(-0.5, -0.5, -0.5);

        Identifier fallbackId = ModelRegistryManager.getFallbackModelIdentifier();
        BakedModel fallbackModel = client.getBakedModelManager().getModel(fallbackId);

        if (fallbackModel != null && fallbackModel != client.getBakedModelManager().getMissingModel()) {
            renderModel(entity, state, matrices, vertexConsumers, fallbackModel, light, overlay);
        }

        matrices.pop();
    }
}
