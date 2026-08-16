package bklmc.ocelotsign.mixin.client;

import bklmc.ocelotsign.client.gui.ModelSelectionOverlay;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 阻止 {@link ModelSelectionOverlay} 打开时游戏在窗口失焦后自动暂停。
 *
 * <p>vanilla 行为：{@code MinecraftClient.onWindowFocusChanged(false)} 在没有焦点时
 * 会调用 {@code openGameMenu(true)}，从而打开游戏菜单 (即暂停界面) 并设置 paused
 * 状态。我们在这里拦截该回调，当玩家当前激活的 Overlay 是我们的
 * {@link ModelSelectionOverlay} 时直接取消，避免游戏暂停、保持世界持续渲染。
 *
 * <p>这样玩家在此叠加层打开时即使鼠标焦点离开窗口（Alt-Tab、转码、视频切屏等），
 * 世界和已放置的实体也会继续运行。
 */
@Mixin(MinecraftClient.class)
public class MinecraftClientWindowFocusMixin {

    @Inject(method = "onWindowFocusChanged", at = @At("HEAD"), cancellable = true)
    private void onWindowFocusChanged(boolean focused, CallbackInfo ci) {
        if (focused) {
            return;
        }
        MinecraftClient self = (MinecraftClient) (Object) this;
        if (self.getOverlay() instanceof ModelSelectionOverlay) {
            ci.cancel();
        }
    }
}
