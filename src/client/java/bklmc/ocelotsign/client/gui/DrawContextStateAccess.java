package bklmc.ocelotsign.client.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.render.state.GuiRenderState;
import net.minecraft.client.gui.render.state.special.SpecialGuiElementRenderState;

import java.lang.reflect.Field;

/**
 * 通过反射访问 {@link DrawContext} 内部的 {@code state} 字段，
 * 以便添加自定义的 GUI 特殊渲染元素。
 *
 * <p>在 1.21.8 中，{@code DrawContext#state} 是 private 字段，
 * 但我们需要通过它来调用 {@link GuiRenderState#addSpecialElement}。</p>
 */
public final class DrawContextStateAccess {

    /** {@link DrawContext#state} 字段的反射缓存。 */
    private static final Field STATE_FIELD = resolveStateField();

    private DrawContextStateAccess() {}

    private static Field resolveStateField() {
        try {
            Field field = DrawContext.class.getDeclaredField("state");
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            throw new IllegalStateException(
                    "无法找到 DrawContext#state 字段，1.21.8 GUI 渲染管线可能已变更", e);
        }
    }

    /**
     * 获取 DrawContext 对应的 GuiRenderState。
     *
     * @param ctx DrawContext 实例
     * @return GuiRenderState
     */
    public static GuiRenderState getGuiRenderState(DrawContext ctx) {
        try {
            return (GuiRenderState) STATE_FIELD.get(ctx);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("无法访问 DrawContext#state", e);
        }
    }

    /**
     * 添加特殊渲染元素到 DrawContext。
     *
     * @param ctx DrawContext 实例
     * @param element 特殊渲染元素状态
     */
    public static void addSpecialElement(DrawContext ctx, SpecialGuiElementRenderState element) {
        getGuiRenderState(ctx).addSpecialElement(element);
    }
}
