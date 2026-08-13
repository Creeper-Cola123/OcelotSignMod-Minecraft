package bklmc.ocelotsign.mixin.client;

import bklmc.ocelotsign.client.gui.ModelSelectionOverlay;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 全局拦截鼠标滚轮事件，将其转发给当前激活的 {@link ModelSelectionOverlay}（如果有）。
 *
 * <p>由于 {@link net.minecraft.client.gui.screens.Overlay} 没有 Screen 提供的输入管线，
 * 我们 mixin 到 {@link MouseHandler#onScroll} 上以捕获原始滚轮事件。
 */
@Mixin(MouseHandler.class)
public class MouseScrollMixin {

    @Inject(method = "onScroll", at = @At("HEAD"), cancellable = true)
    private void onMouseScroll(long handle, double horizontal, double vertical, CallbackInfo ci) {
        if (net.minecraft.client.Minecraft.getInstance() == null) return;

        if (net.minecraft.client.Minecraft.getInstance().getOverlay() instanceof ModelSelectionOverlay overlay) {
            // 转发给 overlay 处理
            overlay.handleMouseScrolledFromMixin(horizontal, vertical);
            // 取消事件，阻止 Minecraft 自身的物品栏切换等处理
            ci.cancel();
        }
    }
}