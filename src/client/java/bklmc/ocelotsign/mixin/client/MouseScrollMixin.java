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
 * 我们 mixin 到 {@link Mouse#onMouseScroll} 上以捕获原始滚轮事件。
 *
 * <p>主要通过 Fabric ScreenEvents 处理滚动（见 {@code OcelotSignModClient.registerModelSelectionScrollHandler}），
 * 这个 Mixin 作为备用方案，在 Fabric handler 不可用时也能捕获滚轮事件。
 */
@Mixin(Mouse.class)
public class MouseScrollMixin {

    @Inject(method = "onMouseScroll", at = @At("HEAD"), cancellable = true)
    private void onMouseScroll(long handle, double horizontal, double vertical, CallbackInfo ci) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc == null) return;

        // 优先通过 Fabric ScreenEvents 处理（更好的屏幕坐标转换）
        // Mixin 主要用于确保在所有情况下都能捕获滚轮事件
        if (mc.getOverlay() instanceof ModelSelectionOverlay overlay) {
            // 转发给 overlay 处理
            overlay.handleMouseScrolledFromMixin(horizontal, vertical);
            // 取消事件，阻止 Minecraft 自身的物品栏切换等处理
            ci.cancel();
        }
    }
}
