package bklmc.ocelotsign.block.custom;

import bklmc.ocelotsign.integration.mishanguc.MishangAccess;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.WallMountLocation;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ModelIds;
import net.minecraft.data.client.TextureMap;
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
import net.minecraft.util.Identifier;
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
 * 立式道路指示牌方块
 *
 * @see WallRoadSignBlock
 * @see bklmc.ocelotsign.blockentity.RoadSignBlockEntity
 */
public class RoadSignBlock extends pers.solid.mishang.uc.block.FullWallSignBlock {

    public static final List<RoadSignBlock> ROAD_SIGNS = new ArrayList<>();

    public static final VoxelShape SHAPE_N = Block.createCuboidShape(0, 0, 0, 16, 16, 3);
    public static final VoxelShape SHAPE_S = Block.createCuboidShape(0, 0, 13, 16, 16, 16);
    public static final VoxelShape SHAPE_E = Block.createCuboidShape(13, 0, 0, 16, 16, 16);
    public static final VoxelShape SHAPE_W = Block.createCuboidShape(0, 0, 0, 3, 16, 16);

    public RoadSignBlock(net.minecraft.block.Block baseBlock, net.minecraft.block.AbstractBlock.Settings settings) {
        super(baseBlock, settings);
        ROAD_SIGNS.add(this);
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
        tooltip.add(MishangAccess.translatable("block.ocelotsignmod.roadsign.tooltip.1")
                .formatted(net.minecraft.util.Formatting.GRAY));
        tooltip.add(MishangAccess.translatable("block.ocelotsignmod.roadsign.tooltip.2")
                .formatted(net.minecraft.util.Formatting.GRAY));
    }

    @Override
    public bklmc.ocelotsign.blockentity.RoadSignBlockEntity createBlockEntity(net.minecraft.util.math.BlockPos pos,
                                                                              net.minecraft.block.BlockState state) {
        return new bklmc.ocelotsign.blockentity.RoadSignBlockEntity(pos, state);
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
        Direction facing = ctx.getPlayerFacing().getOpposite();
        if (facing.getAxis().isHorizontal()) {
            return this.getDefaultState()
                    .with(FACING, facing)
                    .with(FACE, WallMountLocation.WALL)
                    .with(Properties.WATERLOGGED, false);
        }
        return this.getDefaultState()
                .with(FACING, Direction.NORTH)
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
                               BlockStateModelGenerator blockStateModelGenerator) {
        final TextureMap textures = TextureMap.all(this);
        final Identifier modelId = ModelIds.getBlockModelId(this);

        blockStateModelGenerator.blockStateCollector.accept(
                BlockStateModelGenerator.createSingletonBlockState(this, modelId)
        );

        blockStateModelGenerator.registerParentedItemModel(this, modelId);
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }
}
