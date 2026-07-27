package bklmc.ocelotsign.client.render;

import bklmc.ocelotsign.blockentity.CustomModelBlockEntity;
import bklmc.ocelotsign.client.model.ModelRegistryManager;
import com.mojang.blaze3d.vertex.PoseStack;
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
 * @see ModelRegistryManager
 */
public class CustomModelBER
        implements BlockEntityRenderer<CustomModelBlockEntity, CustomModelBER.State> {

    /** 无染色层，自定义模型不使用方块染色 */
    private static final int[] NO_TINTS = BlockModelRenderState.EMPTY_TINTS;

    /**
     * 自定义模型方块的渲染状态。
     */
    public static class State extends BlockEntityRenderState {
        /** 方块实体中记录的模型 ID */
        public String modelId = "";
        /** 方块朝向 */
        public Direction facing = Direction.NORTH;
        /** 用于 fallback 模型旋转动画的时间（tick，含帧插值） */
        public float animationTime;
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

        Minecraft client = Minecraft.getInstance();
        state.animationTime = client.level == null ? 0f : client.level.getGameTime() + tickDelta;
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

            if (model != null) {
                matrices.translate(0.5, 0.5, 0.5);
                matrices.mulPose(Axis.YP.rotationDegrees(-state.facing.toYRot()));
                matrices.translate(-0.5, -0.5, -0.5);
                submitModel(matrices, collector, model, state.lightCoords);
                rendered = true;
            }
        }

        if (!rendered) {
            submitRotatingFallback(state, matrices, collector);
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

    /** 提交旋转的 fallback 模型。 */
    private void submitRotatingFallback(State state, PoseStack matrices, SubmitNodeCollector collector) {
        BlockStateModel fallbackModel =
                ModelRegistryManager.getBakedModel(ModelRegistryManager.getFallbackModelIdentifier());
        if (fallbackModel == null) {
            return;
        }

        matrices.pushPose();
        matrices.translate(0.5, 0.5, 0.5);
        // 绕Y轴旋转
        matrices.mulPose(Axis.YP.rotationDegrees(state.animationTime * 5.0f));
        matrices.translate(-0.5, -0.5, -0.5);

        submitModel(matrices, collector, fallbackModel, state.lightCoords);

        matrices.popPose();
    }
}
