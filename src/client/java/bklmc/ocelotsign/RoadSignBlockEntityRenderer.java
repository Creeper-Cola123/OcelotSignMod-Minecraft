package bklmc.ocelotsign;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.phys.Vec3;
import pers.solid.mishang.uc.blockentity.WallSignBlockEntity;
import pers.solid.mishang.uc.render.WallSignBlockEntityRenderState;
import pers.solid.mishang.uc.render.WallSignBlockEntityRenderer;
import pers.solid.mishang.uc.text.TextContext;

/**
 * 立柱式道路指示牌方块实体渲染器
 *
 * <p>渲染状态的提取委托给 mishanguc 的 {@link WallSignBlockEntityRenderer}，
 * 本类只负责按本模组的变换提交文本。</p>
 *
 * @param <T> 渲染的方块实体类型
 * @see BlockEntityRenderer
 * @see WallSignBlockEntity
 */
@Environment(EnvType.CLIENT)
public class RoadSignBlockEntityRenderer<T extends WallSignBlockEntity>
        implements BlockEntityRenderer<T, WallSignBlockEntityRenderState> {

    /** 发光时使用的最大光照值 */
    private static final int FULL_BRIGHT = 15728880;

    private final BlockEntityRendererProvider.Context ctx;
    private final WallSignBlockEntityRenderer<T> stateExtractor;

    /**
     * 构造渲染器。
     *
     * @param ctx 方块实体渲染器工厂上下文
     */
    public RoadSignBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        this.ctx = ctx;
        this.stateExtractor = new WallSignBlockEntityRenderer<>(ctx);
    }

    @Override
    public WallSignBlockEntityRenderState createRenderState() {
        return stateExtractor.createRenderState();
    }

    @Override
    public void extractRenderState(T entity, WallSignBlockEntityRenderState state, float tickDelta,
                                   Vec3 cameraPos, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        stateExtractor.extractRenderState(entity, state, tickDelta, cameraPos, breakProgress);
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
    public void submit(WallSignBlockEntityRenderState state, PoseStack matrices,
                       SubmitNodeCollector collector, CameraRenderState camera) {
        // 发光时使用最大光照
        final int light = state.glowing ? FULL_BRIGHT : state.lightCoords;

        matrices.pushPose();

        // 对齐方块中心
        matrices.translate(0.5, 0.5, 0.5);
        // 按朝向旋转
        matrices.mulPose(Axis.YP.rotationDegrees(-state.facing.toYRot()));

        // 缩放并偏移至方块表面
        matrices.scale(1 / 16f, -1 / 16f, 1 / 16f);
        float zOffset = 8.0f + 0.0125f;
        matrices.translate(0, 0, zOffset);

        // 渲染所有文本
        for (TextContext textContext : state.textContexts) {
            textContext.draw(
                    ctx.font(),
                    matrices,
                    collector,
                    light,
                    16,
                    state.height
            );
        }

        matrices.popPose();
    }
}
