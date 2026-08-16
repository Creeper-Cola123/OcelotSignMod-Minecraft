package bklmc.ocelotsign;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.block.WallSignBlock;
import pers.solid.mishang.uc.blockentity.WallSignBlockEntity;

/**
 * 立柱式道路指示牌方块实体渲染器。
 *
 * <p>1.21.11 起 BlockEntityRenderer 改为 render-state 模式：
 * {@link RoadSignBlockEntityRenderer#updateRenderState} 把方块实体数据写入
 * {@link RoadSignBlockEntityRenderState}，然后由
 * {@link RoadSignBlockEntityRenderer#render} 提交渲染指令到
 * {@link OrderedRenderCommandQueue}。</p>
 *
 * @param <T> 渲染的方块实体类型
 * @see net.minecraft.client.render.block.entity.BlockEntityRenderer
 * @see WallSignBlockEntity
 */
@Environment(EnvType.CLIENT)
public class RoadSignBlockEntityRenderer<T extends WallSignBlockEntity>
        implements net.minecraft.client.render.block.entity.BlockEntityRenderer<T, RoadSignBlockEntityRenderState> {

    private final BlockEntityRendererFactory.Context ctx;

    /**
     * 构造渲染器。
     *
     * @param ctx 方块实体渲染器工厂上下文
     */
    public RoadSignBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public RoadSignBlockEntityRenderState createRenderState() {
        return new RoadSignBlockEntityRenderState();
    }

    @Override
    public void updateRenderState(T entity, RoadSignBlockEntityRenderState state, float tickProgress, Vec3d cameraPos, @Nullable net.minecraft.client.render.command.ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlay) {
        // 使用 BlockEntityRenderState 提供的工具同步默认值（pos/blockState/type/lightmapCoordinates）
        net.minecraft.client.render.block.entity.state.BlockEntityRenderState.updateBlockEntityRenderState(entity, state, crumblingOverlay);

        final BlockState blockState = entity.getCachedState();
        state.facing = blockState.get(WallSignBlock.FACING);
        state.glowing = entity.glowing;
        state.textContexts = entity.textContexts;
        state.height = entity.getHeight();
    }

    /**
     * 渲染方块实体。
     *
     * @param state  渲染状态
     * @param matrices 矩阵栈
     * @param queue 渲染命令队列
     * @param cameraState 摄像机渲染状态
     */
    @Override
    public void render(RoadSignBlockEntityRenderState state, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {
        // 发光时使用最大光照
        int light = state.glowing ? 15728880 : state.lightmapCoordinates;

        // 对齐方块中心
        matrices.translate(0.5, 0.5, 0.5);
        // 按朝向旋转
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-state.facing.getPositiveHorizontalDegrees()));

        // 缩放并偏移至方块表面
        matrices.scale(1 / 16f, -1 / 16f, 1 / 16f);
        float zOffset = 8.0f + 0.0125f;
        matrices.translate(0, 0, zOffset);

        // 渲染所有文本
        final TextRenderer textRenderer = ctx.textRenderer();
        for (pers.solid.mishang.uc.text.TextContext textContext : state.textContexts) {
            textContext.draw(
                    textRenderer,
                    matrices,
                    queue,
                    light,
                    16,
                    state.height
            );
        }
    }
}
