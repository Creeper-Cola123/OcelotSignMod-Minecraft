package bklmc.ocelotsign.client.render;

import bklmc.ocelotsign.OcelotSignMod;
import bklmc.ocelotsign.blockentity.CustomModelBlockEntity;
import bklmc.ocelotsign.client.model.ModelRegistryManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.model.BlockStateModel;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

/**
 * 自定义模型方块实体渲染器。
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
        implements net.minecraft.client.render.block.entity.BlockEntityRenderer<CustomModelBlockEntity, CustomModelBlockEntityRenderState> {

    // 6 个方向边对应的颜色（ARGB）
    private static final int EDGE_COLOR_DOWN  = 0xFFFF4040; // -Y 红
    private static final int EDGE_COLOR_UP    = 0xFF40FF40; // +Y 绿
    private static final int EDGE_COLOR_NORTH = 0xFF4040FF; // -Z 蓝
    private static final int EDGE_COLOR_SOUTH = 0xFFFFFF40; // +Z 黄
    private static final int EDGE_COLOR_WEST  = 0xFF40FFFF; // -X 青
    private static final int EDGE_COLOR_EAST  = 0xFFFF40FF; // +X 紫

    public CustomModelBER(net.minecraft.client.render.block.entity.BlockEntityRendererFactory.Context ctx) {
    }

    @Override
    public CustomModelBlockEntityRenderState createRenderState() {
        return new CustomModelBlockEntityRenderState();
    }

    @Override
    public void updateRenderState(CustomModelBlockEntity entity, CustomModelBlockEntityRenderState state, float tickProgress, Vec3d cameraPos, @Nullable net.minecraft.client.render.command.ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlay) {
        net.minecraft.client.render.block.entity.state.BlockEntityRenderState.updateBlockEntityRenderState(entity, state, crumblingOverlay);

        state.blockState = entity.getCachedState();
        state.facing = state.blockState.contains(Properties.HORIZONTAL_FACING)
                ? state.blockState.get(Properties.HORIZONTAL_FACING)
                : Direction.NORTH;
        state.modelId = entity.getModelId();
        MinecraftClient client = MinecraftClient.getInstance();
        state.time = client.world == null ? 0L : client.world.getTime();
        state.tickProgress = tickProgress;
    }

    /**
     * 渲染方块实体。
     *
     * <ul>
     *   <li>有有效模型：按方块朝向提交该模型的渲染指令。</li>
     *   <li>无有效模型（fallback）：不渲染任何 3D 模型，但通过
     *       {@link OrderedRenderCommandQueue#submitCustom} 提交自定义绘制器，
     *       画一个 1px、6 条边不同颜色的描边方框，<strong>常态显示</strong>。</li>
     * </ul>
     */
    @Override
    public void render(CustomModelBlockEntityRenderState renderState, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {
        matrices.push();

        String modelId = renderState.modelId;
        MinecraftClient client = MinecraftClient.getInstance();

        boolean hasModelId = modelId != null && !modelId.isEmpty();
        boolean hasValidModel = false;

        if (hasModelId) {
            Identifier modelIdentifier = ModelRegistryManager.getModelIdentifier(modelId);
            BlockStateModel blockStateModel = ModelRegistryManager.getModel(modelIdentifier);

            if (blockStateModel != null && !isMissingModel(client, blockStateModel)) {
                Direction facing = renderState.facing;
                matrices.translate(0.5, 0.5, 0.5);
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-facing.getPositiveHorizontalDegrees()));
                matrices.translate(-0.5, -0.5, -0.5);
                submitModel(queue, matrices, blockStateModel, renderState.lightmapCoordinates, 0);
                hasValidModel = true;
            }
        }

        // fallback 状态：常态显示 6 条不同颜色的 1px 描边方框
        if (!hasValidModel) {
            OcelotSignMod.LOGGER.debug("[CustomModelBER] fallback 状态（modelId={}），渲染多色描边", modelId);
            submitFallbackOutline(queue, matrices);
        }

        matrices.pop();
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
    private void submitFallbackOutline(OrderedRenderCommandQueue queue, MatrixStack matrices) {
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
            queue.submitCustom(matrices, RenderLayers.LINES, (MatrixStack.Entry entry, VertexConsumer consumer) -> {
                // 起点
                consumer.vertex(entry, ca[0], ca[1], ca[2])
                        .color(r, g, b, a)
                        .normal(entry, nx, ny, nz)
                        .lineWidth(1.0f);
                // 终点
                consumer.vertex(entry, cb[0], cb[1], cb[2])
                        .color(r, g, b, a)
                        .normal(entry, nx, ny, nz)
                        .lineWidth(1.0f);
            });
        }
    }

    /**
     * 判定模型是否为缺失模型（vanilla 提供的占位模型）。
     */
    private boolean isMissingModel(MinecraftClient client, BlockStateModel model) {
        try {
            return model.equals(client.getBakedModelManager().getMissingModel());
        } catch (Exception e) {
            return false;
        }
    }

    /** 提交烘焙模型的渲染指令。 */
    private void submitModel(OrderedRenderCommandQueue queue, MatrixStack matrices, BlockStateModel model, int light, int overlay) {
        queue.submitBlockStateModel(
                matrices,
                RenderLayers.cutout(),
                model,
                1.0f,
                1.0f,
                1.0f,
                light,
                overlay,
                0
        );
    }
}
