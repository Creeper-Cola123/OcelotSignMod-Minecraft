package bklmc.ocelotsign.client.render;

import bklmc.ocelotsign.blockentity.CustomModelBlockEntity;
import bklmc.ocelotsign.client.model.ModelRegistryManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

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
public class CustomModelBER
        implements BlockEntityRenderer<CustomModelBlockEntity, CustomModelBER.State> {

    /** 无染色层，自定义模型不使用方块染色 */
    private static final int[] NO_TINTS = BlockModelRenderState.EMPTY_TINTS;

    // 6 个方向边对应的颜色（ARGB）
    private static final int EDGE_COLOR_DOWN  = 0xFFFF4040; // -Y 红
    private static final int EDGE_COLOR_UP    = 0xFF40FF40; // +Y 绿
    private static final int EDGE_COLOR_NORTH = 0xFF4040FF; // -Z 蓝
    private static final int EDGE_COLOR_SOUTH = 0xFFFFFF40; // +Z 黄
    private static final int EDGE_COLOR_WEST  = 0xFF40FFFF; // -X 青
    private static final int EDGE_COLOR_EAST  = 0xFFFF40FF; // +X 紫

    /**
     * 自定义模型方块的渲染状态。
     */
    public static class State extends BlockEntityRenderState {
        /** 方块实体中记录的模型 ID */
        public String modelId = "";
        /** 方块朝向 */
        public Direction facing = Direction.NORTH;
    }

    private final RandomSource random = RandomSource.create();

    /**
     * 构造渲染器。
     *
     * @param ctx 渲染器工厂上下文
     */
    public CustomModelBER(BlockEntityRendererProvider.Context ctx) {}

    @Override
    public State createRenderState() {
        return new State();
    }

    /**
     * 从方块实体提取渲染状态。
     *
     * @param entity        方块实体
     * @param state         待填充的渲染状态
     * @param tickDelta     帧间时间差
     * @param cameraPos     摄像机位置
     * @param breakProgress 破坏进度覆盖层
     */
    @Override
    public void extractRenderState(CustomModelBlockEntity entity, State state, float tickDelta,
                                   Vec3 cameraPos, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderState.extractBase(entity, state, breakProgress);

        state.modelId = entity.getModelId();
        state.facing = entity.getBlockState().hasProperty(BlockStateProperties.HORIZONTAL_FACING)
                ? entity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING)
                : Direction.NORTH;
    }

    /**
     * 提交渲染指令。
     *
     * @param state     渲染状态
     * @param matrices  矩阵栈
     * @param collector 渲染指令收集器
     * @param camera    摄像机状态
     */
    @Override
    public void submit(State state, PoseStack matrices, SubmitNodeCollector collector, CameraRenderState camera) {
        matrices.pushPose();

        boolean rendered = false;
        if (state.modelId != null && !state.modelId.isEmpty()) {
            Identifier modelIdentifier = ModelRegistryManager.getModelIdentifier(state.modelId);
            BlockStateModel model = ModelRegistryManager.getBakedModel(modelIdentifier);

            if (model != null && !isMissingModel(model)) {
                matrices.translate(0.5, 0.5, 0.5);
                matrices.mulPose(Axis.YP.rotationDegrees(-state.facing.toYRot()));
                matrices.translate(-0.5, -0.5, -0.5);
                submitModel(matrices, collector, model, state.lightCoords);
                rendered = true;
            }
        }

        // fallback 状态：常态显示 6 条不同颜色的 1px 描边方框
        if (!rendered) {
            submitFallbackOutline(matrices, collector);
        }

        matrices.popPose();
    }

    /** 提交烘焙模型。 */
    private void submitModel(PoseStack matrices, SubmitNodeCollector collector, BlockStateModel model, int light) {
        List<BlockStateModelPart> parts = new ArrayList<>();
        model.collectParts(random, parts);
        if (parts.isEmpty()) {
            return;
        }
        collector.submitBlockModel(
                matrices,
                RenderTypes.cutoutMovingBlock(),
                parts,
                NO_TINTS,
                light,
                OverlayTexture.NO_OVERLAY,
                0
        );
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
    private void submitFallbackOutline(PoseStack matrices, SubmitNodeCollector collector) {
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
                {0, 3, EDGE_COLOR_NORTH}, // Z=0 立柱 → 北 蓝
                {1, 2, EDGE_COLOR_SOUTH}, // Z=1 立柱 → 南 黄
                // 东西向立柱
                {0, 4, EDGE_COLOR_WEST},  // X=0 立柱 → 西 青
                {1, 5, EDGE_COLOR_EAST},  // X=1 立柱 → 东 紫
        };

        for (int[] edge : edges) {
            float[] ca = corners[edge[0]];
            float[] cb = corners[edge[1]];
            int argb = edge[2];
            final int r = (argb >> 16) & 0xFF;
            final int g = (argb >> 8) & 0xFF;
            final int b = argb & 0xFF;
            final int a = (argb >>> 24) & 0xFF;
            // 法线方向：从 ca 指向 cb
            final float nx = cb[0] - ca[0];
            final float ny = cb[1] - ca[1];
            final float nz = cb[2] - ca[2];
            collector.submitCustomGeometry(matrices, RenderTypes.LINES, (PoseStack.Pose entry, VertexConsumer consumer) -> {
                // 起点
                consumer.addVertex(entry, ca[0], ca[1], ca[2])
                        .setColor(r, g, b, a)
                        .setNormal(entry, nx, ny, nz)
                        .setLineWidth(1.0f);
                // 终点
                consumer.addVertex(entry, cb[0], cb[1], cb[2])
                        .setColor(r, g, b, a)
                        .setNormal(entry, nx, ny, nz)
                        .setLineWidth(1.0f);
            });
        }
    }

    /**
     * 判定模型是否为缺失模型（vanilla 提供的占位模型）。
     *
     * <p>在 26.1.x 中 ModelManager 没有公开的"获取缺失模型"接口，
     * 但如果给定的 modelId 解析到的 BlockStateModel 为 null，我们就视其为缺失模型，
     * 并直接走 fallback 描边逻辑。这里保留方法签名以保持与未来 Mojang 接口的兼容性。
     */
    private boolean isMissingModel(BlockStateModel model) {
        if (model == null) {
            return true;
        }
        try {
            Minecraft client = Minecraft.getInstance();
            // 26.1.x 中 ModelManager 没有公开的 getMissingBlockStateModel()，
            // 退而通过 BlockStateModelSet 探测：当解析到任何 BlockStateModelSet 都认为非缺失。
            return client.getModelManager().getBlockStateModelSet() == null;
        } catch (Exception e) {
            return false;
        }
    }
}