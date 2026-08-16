package bklmc.ocelotsign;

import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;
import net.minecraft.util.math.Direction;
import pers.solid.mishang.uc.text.TextContext;

import java.util.List;

/**
 * 立柱式道路指示牌的渲染状态。
 *
 * <p>1.21.11 的 {@link net.minecraft.client.render.block.entity.BlockEntityRenderer}
 * 采用 render-state 模式：先在 {@link net.minecraft.client.render.block.entity.BlockEntityRenderer#updateRenderState}
 * 把方块实体数据提取到 render state，再在
 * {@link net.minecraft.client.render.block.entity.BlockEntityRenderer#render} 阶段使用。</p>
 */
public class RoadSignBlockEntityRenderState extends BlockEntityRenderState {
    /** 方块实体的朝向。 */
    public Direction facing;
    /** 是否启用发光照明。 */
    public boolean glowing;
    /** 该方块实体的全部文本上下文。 */
    public List<TextContext> textContexts;
    /** 方块实体的高度（用于文本渲染缩放）。 */
    public float height;
}
