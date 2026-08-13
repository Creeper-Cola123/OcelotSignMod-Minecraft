package bklmc.ocelotsign.client.render;

import bklmc.ocelotsign.OcelotSignMod;
import bklmc.ocelotsign.blockentity.CustomModelBlockEntity;
import bklmc.ocelotsign.client.model.ModelRegistryManager;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;

/**
 * 自定义模型方块实体渲染器。
 *
 * <p>设计思路：
 * <ul>
 *   <li>当方块已设置有效模型 ID 时，通过 {@link BlockModelRenderer}
 *       将资源包中的自定义模型渲染到方块上（按朝向旋转）。</li>
 *   <li>当方块未设置模型（或模型无效）时（fallback 状态），
 *       <strong>不渲染任何 3D 几何体</strong>；改为在方块位置常态画一个 1px 描边方框，
 *       <strong>每条边用不同颜色</strong>（东南西北上下 6 种颜色），
 *       提示玩家方块存在但模型尚未加载/缺失。</li>
 * </ul>
 */
public class CustomModelBER implements BlockEntityRenderer<CustomModelBlockEntity> {

    // 6 个方向边对应的颜色（ARGB）
    private static final int EDGE_COLOR_DOWN  = 0xFFFF4040; // -Y 红
    private static final int EDGE_COLOR_UP    = 0xFF40FF40; // +Y 绿
    private static final int EDGE_COLOR_NORTH = 0xFF4040FF; // -Z 蓝
    private static final int EDGE_COLOR_SOUTH = 0xFFFFFF40; // +Z 黄
    private static final int EDGE_COLOR_WEST  = 0xFF40FFFF; // -X 青
    private static final int EDGE_COLOR_EAST  = 0xFFFF40FF; // +X 紫

    private final BlockRenderManager blockRenderManager;

    public CustomModelBER(BlockEntityRendererFactory.Context ctx) {
        this.blockRenderManager = ctx.getRenderManager();
    }

    /**
     * 渲染方块实体。
     *
     * <ul>
     *   <li>有有效模型：通过 BlockModelRenderer 渲染自定义模型，按方块朝向旋转。</li>
     *   <li>无有效模型（fallback）：绘制一个 1px、6 条边不同颜色的描边方框，
     *       <strong>常态显示</strong>。</li>
     * </ul>
     */
    @Override
    public void render(CustomModelBlockEntity entity, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light, int overlay) {
        BlockState blockState = entity.getCachedState();
        Direction facing = blockState.contains(Properties.HORIZONTAL_FACING)
                ? blockState.get(Properties.HORIZONTAL_FACING)
                : Direction.NORTH;
        String modelId = entity.getModelId();

        boolean hasModelId = modelId != null && !modelId.isEmpty();
        boolean hasValidModel = false;

        if (hasModelId) {
            Identifier modelIdentifier = ModelRegistryManager.getModelIdentifier(modelId);
            MinecraftClient client = MinecraftClient.getInstance();
            BakedModel bakedModel = client.getBakedModelManager().getModel(modelIdentifier);

            if (bakedModel != null && !isMissingModel(bakedModel)) {
                hasValidModel = true;
                matrices.push();
                // 按朝向旋转，使模型正面对准玩家视角
                matrices.translate(0.5, 0.5, 0.5);
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-facing.asRotation()));
                matrices.translate(-0.5, -0.5, -0.5);

                // 通过 BlockModelRenderer 渲染自定义模型
                BlockModelRenderer modelRenderer = blockRenderManager.getModelRenderer();
                VertexConsumer consumer = vertexConsumers.getBuffer(RenderLayer.getCutout());
                modelRenderer.render(
                        entity.getWorld(),
                        bakedModel,
                        blockState,
                        entity.getPos(),
                        matrices,
                        consumer,
                        false,
                        entity.getWorld().random,
                        blockState.getRenderingSeed(entity.getPos()),
                        overlay
                );
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

        VertexConsumer consumer = vertexConsumers.getBuffer(RenderLayer.getLines());
        MatrixStack.Entry entry = matrices.peek();
        org.joml.Matrix4f matrix = entry.getPositionMatrix();
        org.joml.Matrix3f normalMatrix = entry.getNormalMatrix();

        for (float[] line : lines) {
            float x1 = line[0], y1 = line[1], z1 = line[2];
            float x2 = line[3], y2 = line[4], z2 = line[5];
            float r = line[6], g = line[7], b = line[8], a = line[9];

            // 顶点1
            consumer.vertex(matrix, x1, y1, z1)
                    .color(r, g, b, a)
                    .overlay(0xFFFFFFFF)
                    .light(15728816)
                    .normal(normalMatrix, 0, 1, 0)
                    .next();
            // 顶点2
            consumer.vertex(matrix, x2, y2, z2)
                    .color(r, g, b, a)
                    .overlay(0xFFFFFFFF)
                    .light(15728816)
                    .normal(normalMatrix, 0, 1, 0)
                    .next();
        }
    }

    /**
     * 判定模型是否为缺失模型（vanilla 提供的占位模型）。
     */
    private boolean isMissingModel(BakedModel model) {
        try {
            MinecraftClient client = MinecraftClient.getInstance();
            return model.equals(client.getBakedModelManager().getMissingModel());
        } catch (Exception e) {
            return false;
        }
    }
}
