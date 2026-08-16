package bklmc.ocelotsign.block.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

/**
 * 简易道路方块
 *
 * @see RoadSignBlock
 */
public class SimpleRoadBlock extends Block {
    public static final EnumProperty<Direction> FACING = EnumProperty.of("facing", Direction.class,
            Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST);

    public static final VoxelShape SHAPE_N = Block.createCuboidShape(0, 0, 0, 16, 16, 3);
    public static final VoxelShape SHAPE_S = Block.createCuboidShape(0, 0, 13, 16, 16, 16);
    public static final VoxelShape SHAPE_E = Block.createCuboidShape(13, 0, 0, 16, 16, 16);
    public static final VoxelShape SHAPE_W = Block.createCuboidShape(0, 0, 0, 3, 16, 16);

    public SimpleRoadBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getStateManager().getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction horizontalFacing = ctx.getHorizontalPlayerFacing().getOpposite();
        if (horizontalFacing.getAxis().isHorizontal()) {
            return this.getDefaultState().with(FACING, horizontalFacing);
        }
        return this.getDefaultState();
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return getShapeForFacing(state.get(FACING));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return getShapeForFacing(state.get(FACING));
    }

    private VoxelShape getShapeForFacing(Direction facing) {
        return switch (facing) {
            case SOUTH -> SHAPE_S;
            case EAST -> SHAPE_E;
            case WEST -> SHAPE_W;
            default -> SHAPE_N;
        };
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }
}
