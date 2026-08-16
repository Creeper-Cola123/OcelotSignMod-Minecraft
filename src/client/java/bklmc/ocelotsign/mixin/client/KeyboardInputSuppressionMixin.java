package bklmc.ocelotsign.mixin.client;

import bklmc.ocelotsign.client.gui.ModelSelectionOverlay;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 当 {@link ModelSelectionOverlay} 激活时,禁用全部键盘输入。
 *
 * <p>{@code ModelSelectionOverlay} 是一个 {@code Overlay}，没有 Screen 自带的输入管线，
 * 因此 vanilla 的快捷键(KeyBinding)、聊天、物品栏切换、调试菜单等功能键都仍会被
 * {@link Keyboard#onKey} 直接处理。我们在这里拦截所有键盘事件,使按键在 overlay
 * 打开期间完全失效,以避免玩家误触导致游戏行为变化(例如 F3 调试、切物品栏、暂停、
 * 视角切换等)。
 *
 * <p>关闭 overlay 的 ESC 键由 {@link ModelSelectionOverlay#tickInput()} 自己直接轮询
 * GLFW 处理,不需要依赖本 mixin 之外的输入通道。
 */
@Mixin(Keyboard.class)
public class KeyboardInputSuppressionMixin {

    @Inject(method = "onKey", at = @At("HEAD"), cancellable = true)
    private void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc == null) return;
        if (mc.getOverlay() instanceof ModelSelectionOverlay) {
            ci.cancel();
        }
    }
}