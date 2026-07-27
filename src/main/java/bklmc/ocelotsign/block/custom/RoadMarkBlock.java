package bklmc.ocelotsign.block.custom;

import com.google.gson.JsonPrimitive;
import net.minecraft.block.*;
import net.minecraft.data.client.*;
import bklmc.ocelotsign.util.EightHorizontalDirection;
import bklmc.ocelotsign.util.FourHorizontalAxis;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * 道路标线方块
 */
public class RoadMarkBlock extends Block implements Waterloggable {
    public static final VoxelShape SHAPE = Block.createCuboidShape(0, 0, 0, 16, 1, 16);
    public static final VoxelShape SHAPE_X = Block.createCuboidShape(0, 0, 2, 16, 1, 14);
    public static final VoxelShape SHAPE_Z = Block.createCuboidShape(2, 0, 0, 14, 1, 16);
    public static final VoxelShape SHAPE_ON_SLAB = Block.createCuboidShape(0, -8, 0, 16, -7, 16);
    public static final VoxelShape SHAPE_ON_SLAB_X = Block.createCuboidShape(0, -8, 2, 16, -7, 14);
    public static final VoxelShape SHAPE_ON_SLAB_Z = Block.createCuboidShape(2, -8, 0, 14, -7, 16);

    public static final BooleanProperty ON_SLAB = BooleanProperty.of("on_slab");
    public static final net.minecraft.data.client.VariantSetting<Integer> Y_VARIANT = new net.minecraft.data.client.VariantSetting<>("y", JsonPrimitive::new);

    protected final Identifier texture;

    private static final VoxelShape SHAPE_TOP_MASK = Block.createCuboidShape(0, 15.5, 0, 16, 16, 16);
    private static final VoxelShape SHAPE_SLAB_TOP_MASK = Block.createCuboidShape(0, 7.5, 0, 16, 8, 16);

    public RoadMarkBlock(@NotNull Identifier texture, Settings settings) {
        super(settings);
        this.texture = texture;
        setDefaultState(getDefaultState()
            .with(Properties.WATERLOGGED, false)
            .with(ON_SLAB, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(Properties.WATERLOGGED, ON_SLAB);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        final BlockPos downPos = pos.down();
        final BlockState downState = world.getBlockState(downPos);
        final VoxelShape downShape = downState.getSidesShape(world, downPos);
        return !VoxelShapes.matchesAnywhere(downShape, SHAPE_TOP_MASK, BooleanBiFunction.ONLY_SECOND)
                || !VoxelShapes.matchesAnywhere(downShape, SHAPE_SLAB_TOP_MASK, BooleanBiFunction.ONLY_SECOND);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState state = super.getPlacementState(ctx);
        if (state != null) {
            final BlockPos blockPos = ctx.getBlockPos();
            final World world = ctx.getWorld();
            state = state.with(Properties.WATERLOGGED, world.getFluidState(blockPos).getFluid() == Fluids.WATER);
            final BlockPos downPos = blockPos.down();
            final BlockState downState = world.getBlockState(downPos);
            final VoxelShape downShape = downState.getSidesShape(world, downPos);
            if (VoxelShapes.matchesAnywhere(downShape, SHAPE_TOP_MASK, BooleanBiFunction.ONLY_SECOND)
                    && !VoxelShapes.matchesAnywhere(downShape, SHAPE_SLAB_TOP_MASK, BooleanBiFunction.ONLY_SECOND)) {
                state = state.with(ON_SLAB, true);
            }
        }
        return state;
    }

    @SuppressWarnings("deprecation")
    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(Properties.WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState,
                                                 WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(Properties.WATERLOGGED)) {
            world.createAndScheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        if (direction == Direction.DOWN) {
            if (!this.canPlaceAt(state, world, pos)) {
                return Blocks.AIR.getDefaultState();
            } else {
                return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos)
                    .with(ON_SLAB, VoxelShapes.matchesAnywhere(
                            world.getBlockState(neighborPos).getOutlineShape(world, neighborPos),
                            SHAPE_TOP_MASK, BooleanBiFunction.ONLY_SECOND));
            }
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return state.get(ON_SLAB) ? SHAPE_ON_SLAB : SHAPE;
    }

    @SuppressWarnings("deprecation")
    @Override
    public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
        List<ItemStack> drops = super.getDroppedStacks(state, builder);
        if (drops.isEmpty()) {
            drops.add(new ItemStack(this));
        }
        return drops;
    }

    public static RoadMarkBlock createAxisFacing(Identifier texture, Settings settings) {
        return new AxisFacing(texture, settings);
    }

    public static RoadMarkBlock createDirectionalFacing(Identifier texture, Settings settings) {
        return new DirectionalFacing(texture, settings);
    }

    public void registerModels(ModelProvider modelProvider, BlockStateModelGenerator blockStateModelGenerator) {
        final TextureMap textures = TextureMap.all(texture);
        final Identifier modelId = ModelIds.getBlockModelId(this);
        final Identifier onSlabModelId = new Identifier(modelId.getNamespace(), modelId.getPath() + "_on_slab");

        net.minecraft.data.client.Models.CUBE_ALL.upload(modelId, textures, blockStateModelGenerator.modelCollector);
        net.minecraft.data.client.Models.CUBE_ALL.upload(onSlabModelId, textures, blockStateModelGenerator.modelCollector);

        blockStateModelGenerator.blockStateCollector.accept(net.minecraft.data.client.VariantsBlockStateSupplier.create(this)
            .coordinate(net.minecraft.data.client.BlockStateVariantMap.create(ON_SLAB)
                .register(false, new net.minecraft.data.client.BlockStateVariant()
                    .put(net.minecraft.data.client.VariantSettings.MODEL, modelId))
                .register(true, new net.minecraft.data.client.BlockStateVariant()
                    .put(net.minecraft.data.client.VariantSettings.MODEL, onSlabModelId))));

        net.minecraft.data.client.Models.HANDHELD.upload(
                net.minecraft.data.client.ModelIds.getItemModelId(asItem()),
                TextureMap.layer0(texture),
                blockStateModelGenerator.modelCollector);
    }

    protected static class AxisFacing extends RoadMarkBlock {
        public static final EnumProperty<FourHorizontalAxis> AXIS = EnumProperty.of("axis", FourHorizontalAxis.class);

        protected AxisFacing(Identifier texture, Settings settings) {
            super(texture, settings);
            setDefaultState(getDefaultState().with(AXIS, FourHorizontalAxis.X));
        }

        @Override
        protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
            super.appendProperties(builder);
            builder.add(AXIS);
        }

        @Override
        public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
            final BlockState state = super.getPlacementState(ctx);
            if (state != null) {
                Direction facing = ctx.getPlayerFacing();
                if (facing.getAxis().isHorizontal()) {
                    return state.with(AXIS, FourHorizontalAxis.fromDirection(facing));
                }
                return state.with(AXIS, FourHorizontalAxis.X);
            }
            return null;
        }

        @Override
        public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
            FourHorizontalAxis axis = state.get(AXIS);
            if (axis == FourHorizontalAxis.X) {
                return state.get(ON_SLAB) ? SHAPE_ON_SLAB_X : SHAPE_X;
            } else if (axis == FourHorizontalAxis.Z) {
                return state.get(ON_SLAB) ? SHAPE_ON_SLAB_Z : SHAPE_Z;
            } else {
                return super.getOutlineShape(state, world, pos, context);
            }
        }

        @SuppressWarnings("deprecation")
        @Override
        public BlockState rotate(BlockState state, BlockRotation rotation) {
            return super.rotate(state, rotation).with(AXIS, state.get(AXIS).rotate(rotation));
        }

        @SuppressWarnings("deprecation")
        @Override
        public BlockState mirror(BlockState state, BlockMirror mirror) {
            return super.mirror(state, mirror).with(AXIS, state.get(AXIS).mirror());
        }

        @Override
        public void registerModels(ModelProvider modelProvider, BlockStateModelGenerator blockStateModelGenerator) {
            final TextureMap textures = TextureMap.all(texture);
            final Identifier modelId = ModelIds.getBlockModelId(this);
            final Identifier onSlabModelId = new Identifier(modelId.getNamespace(), modelId.getPath() + "_on_slab");

            net.minecraft.data.client.Models.CUBE_ALL.upload(modelId, textures, blockStateModelGenerator.modelCollector);
            net.minecraft.data.client.Models.CUBE_ALL.upload(onSlabModelId, textures, blockStateModelGenerator.modelCollector);
            blockStateModelGenerator.blockStateCollector.accept(
                    net.minecraft.data.client.VariantsBlockStateSupplier.create(this)
                .coordinate(net.minecraft.data.client.BlockStateVariantMap.create(ON_SLAB, AXIS)
                    .register(false, FourHorizontalAxis.X,
                            new net.minecraft.data.client.BlockStateVariant()
                                .put(net.minecraft.data.client.VariantSettings.MODEL, modelId))
                    .register(false, FourHorizontalAxis.Z,
                            new net.minecraft.data.client.BlockStateVariant()
                                .put(net.minecraft.data.client.VariantSettings.MODEL, modelId))
                    .register(true, FourHorizontalAxis.X,
                            new net.minecraft.data.client.BlockStateVariant()
                                .put(net.minecraft.data.client.VariantSettings.MODEL, onSlabModelId))
                    .register(true, FourHorizontalAxis.Z,
                            new net.minecraft.data.client.BlockStateVariant()
                                .put(net.minecraft.data.client.VariantSettings.MODEL, onSlabModelId))
                )
            );

            net.minecraft.data.client.Models.HANDHELD.upload(
                    net.minecraft.data.client.ModelIds.getItemModelId(asItem()),
                    TextureMap.layer0(texture),
                    blockStateModelGenerator.modelCollector);
        }
    }

    protected static class DirectionalFacing extends RoadMarkBlock {
        public static final EnumProperty<EightHorizontalDirection> FACING =
                EnumProperty.of("facing", EightHorizontalDirection.class);

        public DirectionalFacing(Identifier texture, Settings settings) {
            super(texture, settings);
            setDefaultState(getDefaultState().with(FACING, EightHorizontalDirection.SOUTH));
        }

        @Override
        protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
            super.appendProperties(builder);
            builder.add(FACING);
        }

        @Override
        public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
            final BlockState state = super.getPlacementState(ctx);
            if (state != null) {
                return state.with(FACING, EightHorizontalDirection.fromRotation(ctx.getPlayerYaw()));
            }
            return null;
        }

        @Override
        public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
            FourHorizontalAxis axis = state.get(FACING).axis;
            if (axis == FourHorizontalAxis.X) {
                return state.get(ON_SLAB) ? SHAPE_ON_SLAB_X : SHAPE_X;
            } else if (axis == FourHorizontalAxis.Z) {
                return state.get(ON_SLAB) ? SHAPE_ON_SLAB_Z : SHAPE_Z;
            } else {
                return super.getOutlineShape(state, world, pos, context);
            }
        }

        @SuppressWarnings("deprecation")
        @Override
        public BlockState rotate(BlockState state, BlockRotation rotation) {
            return super.rotate(state, rotation).with(FACING, state.get(FACING).rotate(rotation));
        }

        @SuppressWarnings("deprecation")
        @Override
        public BlockState mirror(BlockState state, BlockMirror mirror) {
            return super.mirror(state, mirror).with(FACING, state.get(FACING).mirror(mirror));
        }

        @Override
        public void registerModels(ModelProvider modelProvider, BlockStateModelGenerator blockStateModelGenerator) {
            final TextureMap textures = TextureMap.all(texture);
            final Identifier modelId = ModelIds.getBlockModelId(this);
            final Identifier rotatedModelId = new Identifier(modelId.getNamespace(), modelId.getPath() + "_rotated");
            final Identifier onSlabModelId = new Identifier(modelId.getNamespace(), modelId.getPath() + "_on_slab");
            final Identifier onSlabRotatedModelId = new Identifier(modelId.getNamespace(), modelId.getPath() + "_on_slab_rotated");

            net.minecraft.data.client.Models.CUBE_ALL.upload(modelId, textures, blockStateModelGenerator.modelCollector);
            net.minecraft.data.client.Models.CUBE_ALL.upload(rotatedModelId, textures, blockStateModelGenerator.modelCollector);
            net.minecraft.data.client.Models.CUBE_ALL.upload(onSlabModelId, textures, blockStateModelGenerator.modelCollector);
            net.minecraft.data.client.Models.CUBE_ALL.upload(onSlabRotatedModelId, textures, blockStateModelGenerator.modelCollector);

            final net.minecraft.data.client.BlockStateVariantMap.DoubleProperty<Boolean, EightHorizontalDirection> map =
                    net.minecraft.data.client.BlockStateVariantMap.create(ON_SLAB, FACING);
            for (EightHorizontalDirection direction : EightHorizontalDirection.VALUES) {
                int rotation = (int) direction.asRotation();
                boolean isDiagonal = direction.right().isPresent();
                if (isDiagonal) {
                    rotation -= 45;
                }

                map.register(false, direction,
                        net.minecraft.data.client.BlockStateVariant.create()
                            .put(net.minecraft.data.client.VariantSettings.MODEL,
                                    isDiagonal ? rotatedModelId : modelId)
                            .put(Y_VARIANT, rotation));
                map.register(true, direction,
                        net.minecraft.data.client.BlockStateVariant.create()
                            .put(net.minecraft.data.client.VariantSettings.MODEL,
                                    isDiagonal ? onSlabRotatedModelId : onSlabModelId)
                            .put(Y_VARIANT, rotation));
            }

            blockStateModelGenerator.blockStateCollector.accept(
                    net.minecraft.data.client.VariantsBlockStateSupplier.create(this).coordinate(map));

            net.minecraft.data.client.Models.HANDHELD.upload(
                    net.minecraft.data.client.ModelIds.getItemModelId(asItem()),
                    TextureMap.layer0(texture),
                    blockStateModelGenerator.modelCollector);
        }
    }
}
