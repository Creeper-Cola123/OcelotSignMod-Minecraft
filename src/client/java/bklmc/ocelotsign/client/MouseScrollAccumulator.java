package bklmc.ocelotsign.client;

/**
 * 鼠标滚轮增量值的累加器（公开 API）。
 *
 * <p>由 {@link bklmc.ocelotsign.mixin.client.MouseMixin#ocelotsignmod$onScroll} 在
 * {@code Mouse.onMouseScroll} 调用时通过 {@link #accumulate(double)} 累加垂直滚动值，
 * 再由 {@link #consumeDeltaY()} 读取后清零，供 {@code ModelSelectionOverlay.tickInput} 查询。
 *
 * <p>本类被设计为普通类，<strong>不是</strong> Mixin 类 —— 因此可以从 Mod 主代码（含
 * {@code ModelSelectionOverlay}）直接引用，不会触发 Mixin 的
 * {@code IllegalClassLoadError}。
 */
public final class MouseScrollAccumulator {

    private static double pendingDeltaY = 0;

    private MouseScrollAccumulator() {
    }

    /**
     * 累加一次滚轮事件的垂直增量。
     *
     * @param vertical 来自 {@code Mouse.onMouseScroll} 的垂直滚动增量
     */
    public static void accumulate(double vertical) {
        pendingDeltaY += vertical;
    }

    /**
     * 读取自上次调用以来累积的鼠标垂直滚动增量，并清零累积值。
     *
     * @return 累积滚动值
     */
    public static double consumeDeltaY() {
        double value = pendingDeltaY;
        pendingDeltaY = 0;
        return value;
    }
}