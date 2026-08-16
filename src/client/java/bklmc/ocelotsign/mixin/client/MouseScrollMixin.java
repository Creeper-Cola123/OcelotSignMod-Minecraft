package bklmc.ocelotsign.mixin.client;

import bklmc.ocelotsign.client.gui.ModelSelectionOverlay;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 全局拦截鼠标滚轮事件，将其转发给当前激活的 {@link ModelSelectionOverlay}（如果有）。
 *
 * <p>由于 {@link net.minecraft.client.gui.screen.Overlay} 没有 Screen 提供的输入管线，
 * 我们直接 mixin 到 {@link Mouse#onMouseScroll} 上以捕获滚动事件。
 *
 * <p>注意：本 mixin 不会吞掉事件，vanilla 仍会按通常方式处理滚动（用于切换手持物品栏等）。
 */
@Mixin(Mouse.class)
public class MouseScrollMixin {

    @Inject(method = "onMouseScroll", at = @At("HEAD"))
    private void onMouseScroll(long handle, double horizontal, double vertical, CallbackInfo ci) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc == null) return;
        if (mc.getOverlay() instanceof ModelSelectionOverlay overlay) {
            overlay.handleMouseScrolledFromMixin(horizontal, vertical);
        }
    }
}