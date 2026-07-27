package bklmc.ocelotsign.block.custom;

import bklmc.ocelotsign.integration.mishanguc.MishangAccess;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.WallMountLocation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.text.MutableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.ArrayList;
import java.util.List;

/**
 * 墙上道路指示牌方块
 *
 * @see RoadSignBlock
 * @see bklmc.ocelotsign.blockentity.WallRoadSignBlockEntity
 */
public class WallRoadSignBlock extends pers.solid.mishang.uc.block.FullWallSignBlock {

    public static final List<WallRoadSignBlock> WALL_ROAD_SIGNS = new ArrayList<>();

    public static final VoxelShape SHAPE_N = Block.createCuboidShape(0, 0, 15, 16, 16, 18);
    public static final VoxelShape SHAPE_S = Block.createCuboidShape(0, 0, -2, 16, 16, 1);
    public static final VoxelShape SHAPE_E = Block.createCuboidShape(-2, 0, 0, 1, 16, 16);
    public static final VoxelShape SHAPE_W = Block.createCuboidShape(15, 0, 0, 18, 16, 16);

    public WallRoadSignBlock(net.minecraft.block.Block baseBlock, net.minecraft.block.AbstractBlock.Settings settings) {
        super(baseBlock, settings);
        WALL_ROAD_SIGNS.add(this);
        this.setDefaultState(this.getStateManager().getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(FACE, WallMountLocation.WALL)
                .with(Properties.WATERLOGGED, false));
    }

    @Override
    public MutableText getName() {
        return net.minecraft.text.Text.translatable(this.getTranslationKey());
    }

    @Override
    public void appendTooltip(net.minecraft.item.ItemStack stack, net.minecraft.world.BlockView world,
                             java.util.List<net.minecraft.text.Text> tooltip,
                             net.minecraft.client.item.TooltipContext options) {
        tooltip.add(MishangAccess.translatable("block.ocelotsignmod.wallroadsign.tooltip.1")
                .formatted(net.minecraft.util.Formatting.GRAY));
        tooltip.add(MishangAccess.translatable("block.ocelotsignmod.wallroadsign.tooltip.2")
                .formatted(net.minecraft.util.Formatting.GRAY));
    }

    @Override
    public bklmc.ocelotsign.blockentity.WallRoadSignBlockEntity createBlockEntity(net.minecraft.util.math.BlockPos pos,
                                                                               net.minecraft.block.BlockState state) {
        return new bklmc.ocelotsign.blockentity.WallRoadSignBlockEntity(pos, state);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACE, FACING, Properties.WATERLOGGED);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return Fluids.EMPTY.getDefaultState();
    }

    @Override
    public BlockState getStateForNeighborUpdate(
            BlockState state,
            Direction direction,
            BlockState neighborState,
            WorldAccess world,
            BlockPos pos,
            BlockPos neighborPos) {
        super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
        return state;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction horizontalFacing = ctx.getPlayerFacing().getOpposite();
        if (horizontalFacing.getAxis().isHorizontal()) {
            return this.getDefaultState()
                    .with(FACING, horizontalFacing)
                    .with(FACE, WallMountLocation.WALL)
                    .with(Properties.WATERLOGGED, false);
        }
        return this.getDefaultState()
                .with(FACE, WallMountLocation.WALL)
                .with(Properties.WATERLOGGED, false);
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

    @SuppressWarnings("deprecation")
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos,
                             PlayerEntity player, Hand hand, BlockHitResult hit) {
        final Direction side = hit.getSide();
        if (side != state.get(FACING)) {
            return ActionResult.PASS;
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }

    public void registerModels(net.minecraft.data.client.ModelProvider modelProvider,
                               net.minecraft.data.client.BlockStateModelGenerator blockStateModelGenerator) {
        final net.minecraft.data.client.TextureMap textures = net.minecraft.data.client.TextureMap.all(this);
        final net.minecraft.util.Identifier modelId = net.minecraft.data.client.ModelIds.getBlockModelId(this);

        blockStateModelGenerator.blockStateCollector.accept(
                net.minecraft.data.client.BlockStateModelGenerator.createSingletonBlockState(this, modelId)
        );

        blockStateModelGenerator.registerParentedItemModel(this, modelId);
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }
}
