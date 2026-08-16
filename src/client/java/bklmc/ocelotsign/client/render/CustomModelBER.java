package bklmc.ocelotsign.client.render;

import bklmc.ocelotsign.blockentity.CustomModelBlockEntity;
import bklmc.ocelotsign.client.model.ModelRegistryManager;
import com.mojang.blaze3d.systems.RenderSystem;
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
import org.joml.Matrix4f;

/**
 * 自定义模型方块实体渲染器
 *
 * <p>设计思路：
 * <ul>
 *   <li>当方块已设置有效模型 ID 时，按方块朝向渲染该模型。</li>
 *   <li>当方块未设置模型（或模型无效）时（fallback 状态），
 *       <strong>不渲染旋转的 3D 几何体</strong>；改为在方块位置画一个多色描边方框，
 *       <strong>常态显示</strong>，提示玩家方块存在但模型尚未加载/缺失。</li>
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

            if (bakedModel != null && bakedModel != client.getBakedModelManager().getMissingBlockModel()) {
                matrices.translate(0.5, 0.5, 0.5);
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-facing.getPositiveHorizontalDegrees()));
                matrices.translate(-0.5, -0.5, -0.5);
                renderModel(entity, state, matrices, vertexConsumers, bakedModel, light, overlay);
                hasValidModel = true;
            }
        }

        // fallback 状态：渲染多色描边方框（不旋转）
        if (!hasValidModel) {
            renderFallbackOutline(matrices, vertexConsumers);
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

    /**
     * 渲染 fallback 描边方框。
     *
     * <p>画 12 条棱（方块 8 角点 + 12 棱），每条棱用一个对应方向的颜色：
     * <ul>
     *   <li>4 条底面棱（Y=0）→ 下 红</li>
     *   <li>4 条顶面棱（Y=1）→ 上 绿</li>
     *   <li>2 条南北向棱（Z=0 / Z=1）→ 北 蓝 / 南 黄</li>
     *   <li>2 条东西向棱（X=0 / X=1）→ 西 青 / 东 紫</li>
     * </ul>
     *
     * @param matrices 矩阵栈
     * @param vertexConsumers 顶点消费者提供者
     */
    private void renderFallbackOutline(MatrixStack matrices, VertexConsumerProvider vertexConsumers) {
        // 8 个角点：底面 0~3，顶面 4~7
        float[][] corners = new float[][] {
                {0f, 0f, 0f}, {1f, 0f, 0f}, {1f, 0f, 1f}, {0f, 0f, 1f}, // 0~3 底面
                {0f, 1f, 0f}, {1f, 1f, 0f}, {1f, 1f, 1f}, {0f, 1f, 1f}, // 4~7 顶面
        };

        // 12 条棱 + 颜色 (按方向分组)
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
                {0, 3, EDGE_COLOR_NORTH}, // Z=0 立柱 → 北 蓝
                {1, 2, EDGE_COLOR_SOUTH}, // Z=1 立柱 → 南 黄
                // 东西向立柱
                {0, 4, EDGE_COLOR_WEST},  // X=0 立柱 → 西 青
                {1, 5, EDGE_COLOR_EAST},  // X=1 立柱 → 东 紫
        };

        // 使用 Lines 层渲染
        VertexConsumer buffer = vertexConsumers.getBuffer(RenderLayer.getLines());
        Matrix4f matrix = matrices.peek().getPositionMatrix();

        for (int[] edge : edges) {
            float[] ca = corners[edge[0]];
            float[] cb = corners[edge[1]];
            int argb = edge[2];
            float r = ((argb >> 16) & 0xFF) / 255f;
            float g = ((argb >> 8) & 0xFF) / 255f;
            float b = (argb & 0xFF) / 255f;
            float a = ((argb >>> 24) & 0xFF) / 255f;

            // 计算法线方向
            float nx = cb[0] - ca[0];
            float ny = cb[1] - ca[1];
            float nz = cb[2] - ca[2];
            // 归一化
            float len = (float) Math.sqrt(nx * nx + ny * ny + nz * nz);
            if (len > 0) {
                nx /= len;
                ny /= len;
                nz /= len;
            }

            buffer.vertex(matrix, ca[0], ca[1], ca[2])
                    .color(r, g, b, a)
                    .normal(matrices.peek(), nx, ny, nz);
            buffer.vertex(matrix, cb[0], cb[1], cb[2])
                    .color(r, g, b, a)
                    .normal(matrices.peek(), nx, ny, nz);
        }
    }
}
