package bklmc.ocelotsign.client.render;

import bklmc.ocelotsign.OcelotSignMod;
import bklmc.ocelotsign.blockentity.CustomModelBlockEntity;
import bklmc.ocelotsign.client.model.ModelRegistryManager;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.math.Vector4f;

/**
 * 自定义模型方块实体渲染器 (1.19.2 兼容版) - 炫彩轮廓线
 */
public class CustomModelBER implements BlockEntityRenderer<CustomModelBlockEntity> {

    public CustomModelBER(BlockEntityRendererFactory.Context ctx) {}

    @Override
    public void render(CustomModelBlockEntity entity, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light, int overlay) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null) return;

        // ===== 渲染炫彩轮廓线 =====
        if (shouldRenderOutline(client)) {
            renderRainbowOutline(entity, matrices, vertexConsumers, tickDelta);
        }

        // ===== 原有渲染逻辑 =====
        matrices.push();

        String modelId = entity.getModelId();
        BlockState state = entity.getCachedState();
        Direction facing = state.contains(Properties.HORIZONTAL_FACING)
                ? state.get(Properties.HORIZONTAL_FACING) : Direction.NORTH;

        boolean hasModelId = modelId != null && !modelId.isEmpty();
        boolean hasValidModel = false;

        if (hasModelId) {
            Identifier modelIdentifier = ModelRegistryManager.getModelIdentifier(modelId);
            BakedModel bakedModel = getBakedModel(client, modelIdentifier);

            if (bakedModel != null && bakedModel != client.getBakedModelManager().getMissingModel()) {
                matrices.translate(0.5, 0.5, 0.5);
                matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-facing.asRotation()));
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

    private boolean shouldRenderOutline(MinecraftClient client) {
        if (client.player == null) return false;
        ItemStack mainHand = client.player.getMainHandStack();
        ItemStack offHand = client.player.getOffHandStack();
        return mainHand.getItem() == OcelotSignMod.MODEL_WAND
                || offHand.getItem() == OcelotSignMod.MODEL_WAND
                || mainHand.getItem() == OcelotSignMod.CUSTOM_MODEL_BLOCK.asItem()
                || offHand.getItem() == OcelotSignMod.CUSTOM_MODEL_BLOCK.asItem();
    }

    /**
     * 渲染炫彩（彩虹渐变）轮廓线。
     */
    private void renderRainbowOutline(CustomModelBlockEntity entity, MatrixStack matrices,
                                      VertexConsumerProvider vertexConsumers, float tickDelta) {
        // 相对于世界时间的色相偏移
        float hueOffset = (System.currentTimeMillis() % 5000) / 5000.0f;

        // 绘制 12 条边框线，每条颜色不同
        Box box = new Box(0.001, 0.001, 0.001, 0.999, 0.999, 0.999);

        matrices.push();
        // 不需要额外偏移，方块已经在世界坐标中

        // 12条边：底部4条 + 顶部4条 + 垂直4条
        float[][] edges = {
                // 底部 (y=0)
                {0, 0, 0, 1, 0, 0},  // 前
                {1, 0, 0, 1, 0, 1},  // 右
                {1, 0, 1, 0, 0, 1},  // 后
                {0, 0, 1, 0, 0, 0},  // 左
                // 顶部 (y=1)
                {0, 1, 0, 1, 1, 0},
                {1, 1, 0, 1, 1, 1},
                {1, 1, 1, 0, 1, 1},
                {0, 1, 1, 0, 1, 0},
                // 垂直
                {0, 0, 0, 0, 1, 0},
                {1, 0, 0, 1, 1, 0},
                {1, 0, 1, 1, 1, 1},
                {0, 0, 1, 0, 1, 1},
        };

        VertexConsumer buffer = vertexConsumers.getBuffer(RenderLayer.getLines());
        Matrix4f positionMatrix = matrices.peek().getPositionMatrix();

        for (int i = 0; i < edges.length; i++) {
            // 每条边不同色相
            float hue = (i / (float) edges.length + hueOffset) % 1.0f;
            float[] rgb = hsvToRgb(hue, 1.0f, 1.0f);

            float x1 = edges[i][0], y1 = edges[i][1], z1 = edges[i][2];
            float x2 = edges[i][3], y2 = edges[i][4], z2 = edges[i][5];

            buffer.vertex(positionMatrix, x1, y1, z1).color(rgb[0], rgb[1], rgb[2], 1.0f).normal(0, 1, 0).next();
            buffer.vertex(positionMatrix, x2, y2, z2).color(rgb[0], rgb[1], rgb[2], 1.0f).normal(0, 1, 0).next();
        }

        matrices.pop();
    }

    /**
     * HSV 转 RGB。
     */
    private float[] hsvToRgb(float h, float s, float v) {
        int i = (int) (h * 6);
        float f = h * 6 - i;
        float p = v * (1 - s);
        float q = v * (1 - f * s);
        float t = v * (1 - (1 - f) * s);
        return switch (i % 6) {
            case 0 -> new float[]{v, t, p};
            case 1 -> new float[]{q, v, p};
            case 2 -> new float[]{p, v, t};
            case 3 -> new float[]{p, q, v};
            case 4 -> new float[]{t, p, v};
            default -> new float[]{v, p, q};
        };
    }

    /** 渲染烘焙模型。 */
    private void renderModel(CustomModelBlockEntity entity, BlockState state, MatrixStack matrices,
                             VertexConsumerProvider vertexConsumers, BakedModel model, int light, int overlay) {
        MinecraftClient client = MinecraftClient.getInstance();
        VertexConsumer buffer = vertexConsumers.getBuffer(RenderLayer.getCutout());
        client.getBlockRenderManager().getModelRenderer().render(
                entity.getWorld(), model, state, entity.getPos(),
                matrices, buffer, false, entity.getWorld().random,
                state.getRenderingSeed(entity.getPos()), overlay);
    }

    /** 渲染旋转的 fallback 模型。 */
    private void renderRotatingFallback(CustomModelBlockEntity entity, BlockState state, MatrixStack matrices,
                                        VertexConsumerProvider vertexConsumers, float tickDelta, int light, int overlay) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null) return;

        matrices.push();
        float time = client.world.getTime() + tickDelta;
        matrices.translate(0.5, 0.5, 0.5);
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(time * 5.0f));
        matrices.translate(-0.5, -0.5, -0.5);

        Identifier fallbackId = ModelRegistryManager.getFallbackModelIdentifier();
        BakedModel fallbackModel = getBakedModel(client, fallbackId);

        if (fallbackModel != null && fallbackModel != client.getBakedModelManager().getMissingModel()) {
            renderModel(entity, state, matrices, vertexConsumers, fallbackModel, light, overlay);
        }
        matrices.pop();
    }

    private BakedModel getBakedModel(MinecraftClient client, Identifier id) {
        if (id instanceof net.minecraft.client.util.ModelIdentifier modelId) {
            return client.getBakedModelManager().getModel(modelId);
        }
        return client.getBakedModelManager().getModel(new net.minecraft.client.util.ModelIdentifier(id, ""));
    }
}