package bklmc.ocelotsign.client.gui;

import bklmc.ocelotsign.client.model.ModelRegistryManager;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.render.SpecialGuiElementRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.render.model.BlockStateModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.EmptyBlockRenderView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义模型方块 GUI 预览的渲染器。
 *
 * <p>1.21.8 GUI 中的 3D 渲染走 {@link SpecialGuiElementRenderer} 通道。
 * 父类会先准备离屏 RGBA8 + DEPTH32 纹理，然后用下列矩阵初始化 {@link MatrixStack} 后回调
 * {@link #render}。</p>
 */
public class BlockModelPIPRenderer extends SpecialGuiElementRenderer<BlockModelPIPRenderState> {

    private static final Logger LOGGER = LoggerFactory.getLogger("BlockModelPIPRenderer");

    /** GUI 平面光照（最大天光 + 最大方块光）。 */
    private static final int FULL_BRIGHT_LIGHT = 0xF000F0;

    /** 把 [0, 16] 的方块模型空间缩到 [-0.4375, 0.4375] 模型空间。 */
    private static final float MODEL_TO_BASE = 0.0546875F;

    /** 用于 GUI 预览的默认方块状态。 */
    private static final BlockState DEFAULT_BLOCK_STATE = net.minecraft.block.Blocks.AIR.getDefaultState();

    public BlockModelPIPRenderer(VertexConsumerProvider.Immediate vertexConsumers) {
        super(vertexConsumers);
    }

    @Override
    public Class<BlockModelPIPRenderState> getElementClass() {
        return BlockModelPIPRenderState.class;
    }

    @Override
    protected float getYOffset(int heightPx, int windowScaleFactor) {
        return 0.0F;
    }

    @Override
    protected boolean shouldBypassScaling(BlockModelPIPRenderState state) {
        return false;
    }

    @Override
    protected String getName() {
        return "ocelotsign:custom_model_block_preview";
    }

    @Override
    protected void render(BlockModelPIPRenderState state, MatrixStack poseStack) {
        MinecraftClient mc = MinecraftClient.getInstance();
        BlockStateModel model = ModelRegistryManager.getModel(state.modelIdentifier());
        if (model == null || model == mc.getBakedModelManager().getMissingModel()) {
            return;
        }

        poseStack.push();
        try {
            // 1) 在 [0, 16] 模型空间里斜角旋转
            poseStack.translate(8.0F, 8.0F, 8.0F);
            poseStack.multiply(net.minecraft.util.math.RotationAxis.POSITIVE_Y.rotationDegrees(+30.0F));
            poseStack.multiply(net.minecraft.util.math.RotationAxis.POSITIVE_X.rotationDegrees(-25.0F));
            poseStack.translate(-8.0F, -8.0F, -8.0F);

            // 2) 把 [0, 16] 缩放到 base 期望的 [-0.4375, 0.4375] 模型空间
            poseStack.scale(MODEL_TO_BASE, MODEL_TO_BASE, -MODEL_TO_BASE);
            poseStack.translate(-8.0F, 0.0F, -8.0F);

            // 获取模型的 parts
            Random random = Random.create(42L);
            List<BlockModelPart> parts = new ArrayList<>();
            model.addParts(random, parts);

            if (parts.isEmpty()) {
                return;
            }

            // 使用 BlockModelRenderer.render() 方法渲染
            VertexConsumer buffer = vertexConsumers.getBuffer(RenderLayer.getCutout());
            mc.getBlockRenderManager().getModelRenderer().render(
                    EmptyBlockRenderView.INSTANCE,
                    parts,
                    DEFAULT_BLOCK_STATE,
                    BlockPos.ORIGIN,
                    poseStack,
                    buffer,
                    false,  // 不使用背面剔除
                    FULL_BRIGHT_LIGHT
            );
        } catch (Throwable t) {
            LOGGER.warn("[BlockModelPIPRenderer] render failure", t);
        } finally {
            poseStack.pop();
        }
    }
}