package bklmc.ocelotsign;

import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;
import net.minecraft.util.math.Direction;
import pers.solid.mishang.uc.text.TextContext;

import java.util.List;

/**
 * 壁挂式道路指示牌的渲染状态。
 *
 * @see RoadSignBlockEntityRenderState
 * @see WallRoadSignBlockEntityRenderer
 */
public class WallRoadSignBlockEntityRenderState extends BlockEntityRenderState {
    /** 方块实体的朝向。 */
    public Direction facing;
    /** 是否启用发光照明。 */
    public boolean glowing;
    /** 该方块实体的全部文本上下文。 */
    public List<TextContext> textContexts;
    /** 方块实体的高度（用于文本渲染缩放）。 */
    public float height;
    /** Z 轴偏移量，随朝向变化。 */
    public float zOffset;
}
