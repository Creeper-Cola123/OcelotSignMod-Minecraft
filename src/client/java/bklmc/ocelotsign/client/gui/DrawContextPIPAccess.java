package bklmc.ocelotsign.client.gui;

import java.lang.reflect.Field;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.VertexConsumerProvider;

/**
 * 通过反射访问 {@link DrawContext} 内部的字段，
 * 以获取渲染所需的提供者。
 *
 * <p>1.21.8 中 {@code DrawContext#client} 为 package-private 字段，
 * 这里通过反射访问。同时，{@code MinecraftClient#getBufferBuilders()} 是公开的。</p>
 *
 * <p>3D 渲染需要在 GUI 中使用完整的 PoseStack 而不是 Matrix3x2fStack，
 * 因此需要通过 Tessellator 直接获取 BufferBuilder 来进行渲染。</p>
 */
final class DrawContextPIPAccess {

    /** {@link DrawContext#client} 字段的反射缓存。 */
    private static final Field CLIENT_FIELD = resolveClientField();

    private DrawContextPIPAccess() {}

    private static Field resolveClientField() {
        try {
            Field field = DrawContext.class.getDeclaredField("client");
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            throw new IllegalStateException(
                    "无法找到 DrawContext#client 字段，1.21.8 GUI 渲染管线可能已变更", e);
        }
    }

    /**
     * 返回 DrawContext 对应的 MinecraftClient。
     */
    static MinecraftClient getMinecraftClient(DrawContext ctx) {
        try {
            return (MinecraftClient) CLIENT_FIELD.get(ctx);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("无法访问 DrawContext#client", e);
        }
    }

    /**
     * 返回 DrawContext 对应的 MinecraftClient 的 entity VertexConsumerProvider。
     * 这与 {@link net.minecraft.client.render.block.entity.BlockEntityRenderer} 使用的
     * VertexConsumerProvider 一致，会在世界帧的 entity 渲染 pass 中被消费。
     */
    static VertexConsumerProvider.Immediate getEntityVertexConsumers(DrawContext ctx) {
        return getMinecraftClient(ctx).getBufferBuilders().getEntityVertexConsumers();
    }
}