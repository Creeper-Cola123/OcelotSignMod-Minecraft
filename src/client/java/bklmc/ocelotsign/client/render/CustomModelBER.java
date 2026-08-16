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
import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.render.model.BlockStateModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义模型方块实体渲染器
 *
 * <p>设计思路：</p>
 * <ul>
 *   <li>当方块已设置有效模型 ID 时，按方块朝向渲染该模型。</li>
 *   <li>当方块未设置模型（或模型无效）时（fallback 状态），
 *       <strong>不渲染任何 3D 几何体</strong>；改为在方块位置常态画一个 1px 描边方框，
 *       <strong>每条边用不同颜色</strong>（东南西北上下 6 种颜色），
 *       提示玩家方块存在但模型尚未加载/缺失。</li>
 * </ul>
 *
 * @see ModelRegistryManager
 */
public class CustomModelBER implements BlockEntityRenderer<CustomModelBlockEntity> {

    // 6 个方向边对应的颜色（ARGB）
    private static final int EDGE_COLOR_DOWN  = 0xFFFF4040; // -Y 红
    private static final int EDGE_COLOR_UP    = 0xFF40FF40; // +Y 绿
    private static final int EDGE_COLOR_NORTH = 0xFF4040FF; // -Z 蓝
    private static final int EDGE_COLOR_SOUTH = 0xFFFFFF40; // +Z 黄
    private static final int EDGE_COLOR_WEST  = 0xFF40FFFF; // -X 青
    private static final int EDGE_COLOR_EAST  = 0xFFFF40FF; // +X 紫

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
     * @param cameraPos 摄像机位置
     */
    @Override
    public void render(CustomModelBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, Vec3d cameraPos) {
        matrices.push();

        String modelId = entity.getModelId();
        BlockState state = entity.getCachedState();
        Direction facing = state.contains(Properties.HORIZONTAL_FACING) ? state.get(Properties.HORIZONTAL_FACING) : Direction.NORTH;
        MinecraftClient client = MinecraftClient.getInstance();

        boolean hasModelId = modelId != null && !modelId.isEmpty();
        boolean hasValidModel = false;

        if (hasModelId) {
            Identifier modelIdentifier = ModelRegistryManager.getModelIdentifier(modelId);
            BlockStateModel blockStateModel = ModelRegistryManager.getModel(modelIdentifier);

            if (blockStateModel != null && !isMissingModel(client, blockStateModel)) {
                matrices.translate(0.5, 0.5, 0.5);
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-facing.getPositiveHorizontalDegrees()));
                matrices.translate(-0.5, -0.5, -0.5);
                renderModel(entity, state, matrices, vertexConsumers, blockStateModel, light, overlay);
                hasValidModel = true;
            }
        }

        // fallback 状态：常态显示 6 条不同颜色的 1px 描边方框
        if (!hasValidModel) {
            renderFallbackOutline(entity, matrices, vertexConsumers, overlay);
        }

        matrices.pop();
    }

    /**
     * 判定模型是否为缺失模型（vanilla 提供的占位模型）。
     */
    private boolean isMissingModel(MinecraftClient client, BlockStateModel model) {
        return model == client.getBakedModelManager().getMissingModel();
    }

    /** 渲染烘焙模型。 */
    private void renderModel(CustomModelBlockEntity entity, BlockState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, BlockStateModel model, int light, int overlay) {
        MinecraftClient client = MinecraftClient.getInstance();
        VertexConsumer buffer = vertexConsumers.getBuffer(RenderLayer.getCutout());
        List<BlockModelPart> parts = new ArrayList<>();
        model.addParts(entity.getWorld().random, parts);
        client.getBlockRenderManager().getModelRenderer().render(
                entity.getWorld(),
                parts,
                state,
                entity.getPos(),
                matrices,
                buffer,
                false,
                overlay
        );
    }

    /**
     * 渲染 fallback 描边方框。
     *
     * <p>画 12 条棱（方块 8 角点 + 12 棱），每条棱用一个对应方向的颜色：</p>
     * <ul>
     *   <li>4 条底面棱（Y=0）→ 下 红</li>
     *   <li>4 条顶面棱（Y=1）→ 上 绿</li>
     *   <li>2 条南北向棱（Z=0 / Z=1）→ 北 蓝 / 南 黄</li>
     *   <li>2 条东西向棱（X=0 / X=1）→ 西 青 / 东 紫</li>
     * </ul>
     */
    private void renderFallbackOutline(CustomModelBlockEntity entity, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int overlay) {
        // 8 个角点：底面 0~3，顶面 4~7
        float[][] corners = new float[][] {
                {0f, 0f, 0f}, {1f, 0f, 0f}, {1f, 0f, 1f}, {0f, 0f, 1f}, // 0~3 底面
                {0f, 1f, 0f}, {1f, 1f, 0f}, {1f, 1f, 1f}, {0f, 1f, 1f}, // 4~7 顶面
        };

        // 12 条棱 + 颜色 (按方向分组)
        // 每条：{ 角点A, 角点B, ARGB }
        int[][] edges = new int[][] {
                // 底面 4 条 (Y=0) → 下 红
                {0, 1, EDGE_COLOR_DOWN},
                {1, 2, EDGE_COLOR_DOWN},
                {2, 3, EDGE_COLOR_DOWN},
                {3, 0, EDGE_COLOR_DOWN},
                // 顶面 4 条 (Y=1) → 上 绿
                {4, 5, EDGE_COLOR_UP},
                {5, 6, EDGE_COLOR_UP},
                {6, 7, EDGE_COLOR_UP},
                {7, 4, EDGE_COLOR_UP},
                // 南北向立柱
                {0, 4, EDGE_COLOR_NORTH}, // Z=0 立柱 → 北 蓝
                {1, 5, EDGE_COLOR_SOUTH}, // Z=1 立柱 → 南 黄
                // 东西向立柱
                {3, 7, EDGE_COLOR_WEST},  // X=0 立柱 → 西 青
                {2, 6, EDGE_COLOR_EAST},   // X=1 立柱 → 东 紫
        };

        VertexConsumer consumer = vertexConsumers.getBuffer(RenderLayer.getLines());
        Matrix4f matrix = matrices.peek().getPositionMatrix();

        for (int[] edge : edges) {
            float[] ca = corners[edge[0]];
            float[] cb = corners[edge[1]];
            int argb = edge[2];
            int r = (argb >> 16) & 0xFF;
            int g = (argb >> 8) & 0xFF;
            int b = argb & 0xFF;
            int a = (argb >> 24) & 0xFF;

            // 计算法线方向（从 A 到 B）
            float nx = cb[0] - ca[0];
            float ny = cb[1] - ca[1];
            float nz = cb[2] - ca[2];

            // 绘制线段
            consumer.vertex(matrix, ca[0], ca[1], ca[2])
                    .color(r, g, b, a)
                    .normal(nx, ny, nz);
            consumer.vertex(matrix, cb[0], cb[1], cb[2])
                    .color(r, g, b, a)
                    .normal(nx, ny, nz);
        }
    }
}
