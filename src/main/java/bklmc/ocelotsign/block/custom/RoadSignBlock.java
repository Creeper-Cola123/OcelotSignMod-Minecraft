package bklmc.ocelotsign.block.custom;

import bklmc.ocelotsign.integration.mishanguc.MishangAccess;
import com.mojang.serialization.MapCodec;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * 立式道路指示牌方块
 *
 * @see WallRoadSignBlock
 * @see bklmc.ocelotsign.blockentity.RoadSignBlockEntity
 */
public class RoadSignBlock extends pers.solid.mishang.uc.block.FullWallSignBlock {

    public static final List<RoadSignBlock> ROAD_SIGNS = new ArrayList<>();

    public static final VoxelShape SHAPE_N = Block.box(0, 0, 0, 16, 16, 3);
    public static final VoxelShape SHAPE_S = Block.box(0, 0, 13, 16, 16, 16);
    public static final VoxelShape SHAPE_E = Block.box(13, 0, 0, 16, 16, 16);
    public static final VoxelShape SHAPE_W = Block.box(0, 0, 0, 3, 16, 16);

    public RoadSignBlock(net.minecraft.world.level.block.Block baseBlock, net.minecraft.world.level.block.state.BlockBehaviour.Properties settings) {
        super(baseBlock, settings);
        ROAD_SIGNS.add(this);
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(FACING, Direction.NORTH)
                .setValue(FACE, AttachFace.WALL)
                .setValue(BlockStateProperties.WATERLOGGED, false));
    }

    @Override
    public MutableComponent getName() {
        return net.minecraft.network.chat.Component.translatable(this.getDescriptionId());
    }

    @Override
    public BlockEntity newBlockEntity(net.minecraft.core.BlockPos pos,
                                         net.minecraft.world.level.block.state.BlockState state) {
        return new bklmc.ocelotsign.blockentity.RoadSignBlockEntity(pos, state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACE, FACING, BlockStateProperties.WATERLOGGED);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return Fluids.EMPTY.defaultFluidState();
    }

    @Override
    public BlockState updateShape(
            BlockState state,
            LevelReader world,
            ScheduledTickAccess tickAccess,
            BlockPos pos,
            Direction direction,
            BlockPos neighborPos,
            BlockState neighborState,
            RandomSource random) {
        super.updateShape(state, world, tickAccess, pos, direction, neighborPos, neighborState, random);
        return state;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Direction horizontalFacing = ctx.getHorizontalDirection().getOpposite();
        if (horizontalFacing.getAxis().isHorizontal()) {
            return this.defaultBlockState()
                    .setValue(FACING, horizontalFacing)
                    .setValue(FACE, AttachFace.WALL)
                    .setValue(BlockStateProperties.WATERLOGGED, false);
        }
        return this.defaultBlockState()
                .setValue(FACE, AttachFace.WALL)
                .setValue(BlockStateProperties.WATERLOGGED, false);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return getShapeForFacing(state.getValue(FACING));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return getShapeForFacing(state.getValue(FACING));
    }

    private VoxelShape getShapeForFacing(Direction facing) {
        return switch (facing) {
            case SOUTH -> SHAPE_S;
            case EAST -> SHAPE_E;
            case WEST -> SHAPE_W;
            default -> SHAPE_N;
        };
    }

    @SuppressWarnings("deprecation")
    @Override
    public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos,
                              Player player, BlockHitResult hit) {
        final Direction side = hit.getDirection();
        if (side != state.getValue(FACING)) {
            return InteractionResult.PASS;
        }
        return super.useWithoutItem(state, world, pos, player, hit);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }
}
