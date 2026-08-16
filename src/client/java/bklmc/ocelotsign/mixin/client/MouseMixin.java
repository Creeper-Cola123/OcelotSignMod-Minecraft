package bklmc.ocelotsign.mixin.client;

import bklmc.ocelotsign.client.MouseScrollAccumulator;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin 用于捕获 Mouse 类的滚轮事件，供 ModelSelectionOverlay 在 Overlay 模式下使用。
 *
 * <p>
 * 这里通过 {@code @Inject} 拦截方法调用，将滚动增量转发给
 * {@link MouseScrollAccumulator#accumulate(double)}。
 *
 * <p>由于 Mixin 类不能被 Mod 主代码直接引用（会出现
 * {@code IllegalClassLoadError}），本 mixin 不持有状态、不暴露 API，
 * 所有共享状态都放在 {@link MouseScrollAccumulator} 中。
 */
@Mixin(Mouse.class)
public class MouseMixin {

    @Inject(method = "onMouseScroll", at = @At("HEAD"))
    private static void ocelotsignmod$onScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        // 将滚动值累加到公开累加器，供 Overlay 查询使用
        MouseScrollAccumulator.accumulate(vertical);
    }
}