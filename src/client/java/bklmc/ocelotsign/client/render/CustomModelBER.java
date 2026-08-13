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
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;

/**
 * 自定义模型方块实体渲染器（1.19.2 兼容版）。
 *
 * <p>设计思路：
 * <ul>
 *   <li>当方块已设置有效模型 ID 时，通过 {@link net.minecraft.client.render.block.BlockModelRenderer}
 *       将资源包中的自定义模型渲染到方块上（按朝向旋转）。</li>
 *   <li>当方块未设置模型（或模型无效）时（fallback 状态），
 *       <strong>不渲染任何 3D 几何体</strong>；改为在方块位置常态画一个 1px 描边方框，
 *       <strong>每条边用不同颜色</strong>（东南西北上下 6 种颜色），
 *       提示玩家方块存在但模型尚未加载/缺失。</li>
 * </ul>
 */
public class CustomModelBER implements BlockEntityRenderer<CustomModelBlockEntity> {

    public CustomModelBER(BlockEntityRendererFactory.Context ctx) {}

    @Override
    public void render(CustomModelBlockEntity entity, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light, int overlay) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null) return;

        BlockState blockState = entity.getCachedState();
        Direction facing = blockState.contains(Properties.HORIZONTAL_FACING)
                ? blockState.get(Properties.HORIZONTAL_FACING)
                : Direction.NORTH;
        String modelId = entity.getModelId();

        boolean hasModelId = modelId != null && !modelId.isEmpty();
        boolean hasValidModel = false;

        if (hasModelId) {
            Identifier modelIdentifier = ModelRegistryManager.getModelIdentifier(modelId);
            BakedModel bakedModel = getBakedModel(client, modelIdentifier);

            if (bakedModel != null && bakedModel != client.getBakedModelManager().getMissingModel()) {
                matrices.push();
                matrices.translate(0.5, 0.5, 0.5);
                matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-facing.asRotation()));
                matrices.translate(-0.5, -0.5, -0.5);
                renderModel(entity, blockState, matrices, vertexConsumers, bakedModel, light, overlay);
                hasValidModel = true;
                matrices.pop();
            }
        }

        // fallback 状态：常态显示 6 条不同颜色的 1px 描边方框
        if (!hasValidModel) {
            OcelotSignMod.LOGGER.debug("[CustomModelBER] fallback 状态（modelId={}），渲染多色描边", modelId);
            matrices.push();
            submitFallbackOutline(vertexConsumers, matrices);
            matrices.pop();
        }
    }

    /**
     * 提交 fallback 描边方框。
     *
     * <p>画 12 条棱（方块 8 角点 + 12 棱），每条棱用一个对应方向的颜色：</p>
     * <ul>
     *   <li>4 条底面棱（Y=0）→ 下 红</li>
     *   <li>4 条顶面棱（Y=1）→ 上 绿</li>
     *   <li>2 条南北向棱（Z=0 / Z=1）→ 南 黄 / 北 蓝</li>
     *   <li>2 条东西向棱（X=0 / X=1）→ 西 青 / 东 紫</li>
     * </ul>
     */
    private void submitFallbackOutline(VertexConsumerProvider vertexConsumers, MatrixStack matrices) {
        // 方块 12 条外边框
        // 格式: [x1, y1, z1, x2, y2, z2, r, g, b, a]
        float[][] lines = new float[][] {
                // 底面 4 条 (Y=0) → 红
                {0, 0, 0,  1, 0, 0,  1, 0.25f, 0.25f, 1},
                {1, 0, 0,  1, 0, 1,  1, 0.25f, 0.25f, 1},
                {1, 0, 1,  0, 0, 1,  1, 0.25f, 0.25f, 1},
                {0, 0, 1,  0, 0, 0,  1, 0.25f, 0.25f, 1},
                // 顶面 4 条 (Y=1) → 绿
                {0, 1, 0,  1, 1, 0,  0.25f, 1, 0.25f, 1},
                {1, 1, 0,  1, 1, 1,  0.25f, 1, 0.25f, 1},
                {1, 1, 1,  0, 1, 1,  0.25f, 1, 0.25f, 1},
                {0, 1, 1,  0, 1, 0,  0.25f, 1, 0.25f, 1},
                // 垂直边 4 条 → 青/紫
                {0, 0, 0,  0, 1, 0,  0.25f, 1, 1, 1},
                {1, 0, 0,  1, 1, 0,  1, 0.25f, 1, 1},
                {1, 0, 1,  1, 1, 1,  1, 0.25f, 1, 1},
                {0, 0, 1,  0, 1, 1,  0.25f, 1, 1, 1},
        };

        VertexConsumer buffer = vertexConsumers.getBuffer(RenderLayer.getLines());
        Matrix4f positionMatrix = matrices.peek().getPositionMatrix();

        for (float[] line : lines) {
            float x1 = line[0], y1 = line[1], z1 = line[2];
            float x2 = line[3], y2 = line[4], z2 = line[5];
            float r = line[6], g = line[7], b = line[8], a = line[9];

            // 顶点1
            buffer.vertex(positionMatrix, x1, y1, z1).color(r, g, b, a).normal(0, 1, 0).next();
            // 顶点2
            buffer.vertex(positionMatrix, x2, y2, z2).color(r, g, b, a).normal(0, 1, 0).next();
        }
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

    /**
     * 解析模型并获取 BakedModel。
     *
     * <p>核心修复：使用 Fabric API 提供的 BakedModelManagerHelper 直接通过纯 Identifier 获取模型，
     * 避免 vanilla ModelIdentifier 自动追加 "#" 导致匹配失败返回紫黑方块。</p>
     */
    private BakedModel getBakedModel(MinecraftClient client, Identifier id) {
        if (id instanceof net.minecraft.client.util.ModelIdentifier modelId) {
            return client.getBakedModelManager().getModel(modelId);
        }
        // 【核心修复】使用 Fabric API 提供的 Helper 直接通过纯 Identifier 获取模型
        return net.fabricmc.fabric.api.client.model.BakedModelManagerHelper.getModel(client.getBakedModelManager(), id);
    }
}
