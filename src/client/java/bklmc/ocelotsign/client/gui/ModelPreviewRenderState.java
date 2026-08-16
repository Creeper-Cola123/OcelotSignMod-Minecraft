package bklmc.ocelotsign.client.gui;

import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.render.state.special.SpecialGuiElementRenderState;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

/**
 * 模型卡片预览在 GUI 中渲染时使用的特殊渲染状态。
 *
 * <p>携带需要在 GUI 预览框中显示的模型资源标识符及预览区域坐标，
 * 由 {@link ModelPreviewGuiElementRenderer} 负责实际绘制。</p>
 */
public record ModelPreviewRenderState(
        Identifier modelIdentifier,
        int x1,
        int y1,
        int x2,
        int y2,
        @Nullable ScreenRect scissorArea,
        @Nullable ScreenRect bounds
) implements SpecialGuiElementRenderState {

    public ModelPreviewRenderState(Identifier modelIdentifier,
                                    int left,
                                    int top,
                                    int right,
                                    int bottom,
                                    @Nullable ScreenRect scissorArea) {
        this(
                modelIdentifier,
                left,
                top,
                right,
                bottom,
                scissorArea,
                SpecialGuiElementRenderState.createBounds(left, top, right, bottom, scissorArea)
        );
    }

    @Override
    public float scale() {
        // 离屏纹理尺寸因子（像素），与预览区域尺寸对齐以便获得清晰的 1:1 显示效果。
        return 64.0f;
    }
}
