package bklmc.ocelotsign.client.gui;

import bklmc.ocelotsign.client.model.ModelRegistryManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.render.SpecialGuiElementRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.render.model.BlockStateModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.EmptyBlockRenderView;
import org.joml.Matrix4f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 模型卡片预览的 GUI 特殊元素渲染器。
 */
public class ModelPreviewGuiElementRenderer extends SpecialGuiElementRenderer<ModelPreviewRenderState> {

    private static final Logger LOGGER = LoggerFactory.getLogger("ModelPreviewGuiElementRenderer");

    /** GUI 平面光照（最大天光 + 最大方块光）。 */
    private static final int FULL_BRIGHT_LIGHT = 0xF000F0;

    /** 把 [0, 16] 的方块模型空间缩到 [-0.4375, 0.4375] 模型空间。 */
    private static final float MODEL_TO_BASE = 0.0546875F;

    /** 用于 GUI 预览的默认方块状态。 */
    private static final net.minecraft.block.BlockState DEFAULT_BLOCK_STATE = net.minecraft.block.Blocks.AIR.getDefaultState();

    public ModelPreviewGuiElementRenderer(VertexConsumerProvider.Immediate vertexConsumers) {
        super(vertexConsumers);
    }

    @Override
    public Class<ModelPreviewRenderState> getElementClass() {
        return ModelPreviewRenderState.class;
    }

    @Override
    protected float getYOffset(int heightPx, int windowScaleFactor) {
        return 0.0F;
    }

    @Override
    protected boolean shouldBypassScaling(ModelPreviewRenderState state) {
        return false;
    }

    @Override
    protected String getName() {
        return "ocelotsign:model_preview_element";
    }

    @Override
    protected void render(ModelPreviewRenderState state, MatrixStack poseStack) {
        MinecraftClient mc = MinecraftClient.getInstance();
        BlockStateModel model = ModelRegistryManager.getModel(state.modelIdentifier());

        if (model == null || model == mc.getBakedModelManager().getMissingModel()) {
            LOGGER.warn("[ModelPreviewGuiElementRenderer] 模型为空或为缺失模型: {}", state.modelIdentifier());
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
                LOGGER.warn("[ModelPreviewGuiElementRenderer] 没有 parts");
                return;
            }

            LOGGER.info("[ModelPreviewGuiElementRenderer] 渲染模型: {}, parts数量: {}", state.modelIdentifier(), parts.size());

            // 1. 先用纯白色渲染背景（solid层，避免alpha丢弃问题）
            renderWhiteBackground(poseStack);

            // 2. 然后渲染模型本体 - 使用 Cutout 层
            VertexConsumer buffer = vertexConsumers.getBuffer(RenderLayers.cutout());
            mc.getBlockRenderManager().getModelRenderer().render(
                    EmptyBlockRenderView.INSTANCE,
                    parts,
                    DEFAULT_BLOCK_STATE,
                    BlockPos.ORIGIN,
                    poseStack,
                    buffer,
                    false,
                    FULL_BRIGHT_LIGHT
            );

            LOGGER.info("[ModelPreviewGuiElementRenderer] 渲染完成");
        } catch (Throwable t) {
            LOGGER.warn("[ModelPreviewGuiElementRenderer] render failure", t);
        } finally {
            poseStack.pop();
        }
    }

    /**
     * 渲染白色背景。
     */
    private void renderWhiteBackground(MatrixStack poseStack) {
        VertexConsumer buffer = vertexConsumers.getBuffer(RenderLayers.solid());

        float size = 10.0F;
        Matrix4f matrix = poseStack.peek().getPositionMatrix();

        // 在 Z = -0.5（最远处）绘制一个大白色背景四边形
        // 注意：使用 Solid 层确保不透明
        buffer.vertex(matrix, -size, -size, -0.5F).color(255, 255, 255, 255);
        buffer.vertex(matrix, size, -size, -0.5F).color(255, 255, 255, 255);
        buffer.vertex(matrix, size, size, -0.5F).color(255, 255, 255, 255);
        buffer.vertex(matrix, -size, size, -0.5F).color(255, 255, 255, 255);
    }
}