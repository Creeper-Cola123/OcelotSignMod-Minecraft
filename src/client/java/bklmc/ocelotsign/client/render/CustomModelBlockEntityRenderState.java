package bklmc.ocelotsign.client.render;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;
import net.minecraft.util.math.Direction;

/**
 * 自定义模型方块实体的渲染状态。
 *
 * <p>1.21.11 起 BlockEntityRenderer 采用 render-state 模式，此处将原 1.21.8 中
 * {@code render(entity, ...)} 内部读取的字段抽离到 render-state，
 * 在 {@link CustomModelBER#updateRenderState} 中填充，
 * 再在 {@link CustomModelBER#render} 中消费。</p>
 *
 * @see CustomModelBER
 */
public class CustomModelBlockEntityRenderState extends BlockEntityRenderState {
    /** 模型 ID；若为空则使用 fallback 旋转模型。 */
    public String modelId;
    /** 缓存的方块状态，用于读取朝向等属性。 */
    public BlockState blockState;
    /** 选择的方向。 */
    public Direction facing;
    /** 客户端世界时间戳，用于 fallback 旋转动画。 */
    public long time;
    /** tickProgress 用于平滑动画的帧间差。 */
    public float tickProgress;
}
