package bklmc.ocelotsign.block.custom;

import com.google.gson.JsonPrimitive;
import net.minecraft.block.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.client.*;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ModelProvider;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import bklmc.ocelotsign.util.EightHorizontalDirection;
import bklmc.ocelotsign.util.FourHorizontalAxis;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * 道路标线方块
 */
public class RoadMarkBlock extends Block implements SimpleWaterloggedBlock {
    public static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 1, 16);
    public static final VoxelShape SHAPE_X = Block.box(0, 0, 2, 16, 1, 14);
    public static final VoxelShape SHAPE_Z = Block.box(2, 0, 0, 14, 1, 16);
    public static final VoxelShape SHAPE_ON_SLAB = Block.box(0, -8, 0, 16, -7, 16);
    public static final VoxelShape SHAPE_ON_SLAB_X = Block.box(0, -8, 2, 16, -7, 14);
    public static final VoxelShape SHAPE_ON_SLAB_Z = Block.box(2, -8, 0, 14, -7, 16);

    public static final BooleanProperty ON_SLAB = BooleanProperty.create("on_slab");
    public static final net.minecraft.data.models.blockstates.VariantProperty<Integer> Y_VARIANT = new net.minecraft.data.models.blockstates.VariantProperty<>("y", JsonPrimitive::new);

    protected final ResourceLocation texture;

    private static final VoxelShape SHAPE_TOP_MASK = Block.box(0, 15.5, 0, 16, 16, 16);
    private static final VoxelShape SHAPE_SLAB_TOP_MASK = Block.box(0, 7.5, 0, 16, 8, 16);

    public RoadMarkBlock(@NotNull ResourceLocation texture, Properties settings) {
        super(settings);
        this.texture = texture;
        registerDefaultState(defaultBlockState()
            .setValue(BlockStateProperties.WATERLOGGED, false)
            .setValue(ON_SLAB, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BlockStateProperties.WATERLOGGED, ON_SLAB);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        final BlockPos downPos = pos.below();
        final BlockState downState = world.getBlockState(downPos);
        final VoxelShape downShape = downState.getBlockSupportShape(world, downPos);
        return !Shapes.joinIsNotEmpty(downShape, SHAPE_TOP_MASK, BooleanOp.ONLY_SECOND)
                || !Shapes.joinIsNotEmpty(downShape, SHAPE_SLAB_TOP_MASK, BooleanOp.ONLY_SECOND);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState state = super.getStateForPlacement(ctx);
        if (state != null) {
            final BlockPos blockPos = ctx.getClickedPos();
            final Level world = ctx.getLevel();
            state = state.setValue(BlockStateProperties.WATERLOGGED, world.getFluidState(blockPos).getType() == Fluids.WATER);
            final BlockPos downPos = blockPos.below();
            final BlockState downState = world.getBlockState(downPos);
            final VoxelShape downShape = downState.getBlockSupportShape(world, downPos);
            if (Shapes.joinIsNotEmpty(downShape, SHAPE_TOP_MASK, BooleanOp.ONLY_SECOND)
                    && !Shapes.joinIsNotEmpty(downShape, SHAPE_SLAB_TOP_MASK, BooleanOp.ONLY_SECOND)) {
                state = state.setValue(ON_SLAB, true);
            }
        }
        return state;
    }

    @SuppressWarnings("deprecation")
    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                                 LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(BlockStateProperties.WATERLOGGED)) {
            world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }
        if (direction == Direction.DOWN) {
            if (!this.canSurvive(state, world, pos)) {
                return Blocks.AIR.defaultBlockState();
            } else {
                return super.updateShape(state, direction, neighborState, world, pos, neighborPos)
                    .setValue(ON_SLAB, Shapes.joinIsNotEmpty(
                            world.getBlockState(neighborPos).getShape(world, neighborPos),
                            SHAPE_TOP_MASK, BooleanOp.ONLY_SECOND));
            }
        }
        return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return state.getValue(ON_SLAB) ? SHAPE_ON_SLAB : SHAPE;
    }

    @SuppressWarnings("deprecation")
    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        List<ItemStack> drops = super.getDrops(state, builder);
        if (drops.isEmpty()) {
            drops.add(new ItemStack(this));
        }
        return drops;
    }

    public static RoadMarkBlock createAxisFacing(ResourceLocation texture, Properties settings) {
        return new AxisFacing(texture, settings);
    }

    public static RoadMarkBlock createDirectionalFacing(ResourceLocation texture, Properties settings) {
        return new DirectionalFacing(texture, settings);
    }

    public void registerModels(ModelProvider modelProvider, BlockModelGenerators blockStateModelGenerator) {
        final TextureMapping textures = TextureMapping.cube(texture);
        final ResourceLocation modelId = ModelLocationUtils.getModelLocation(this);
        final ResourceLocation onSlabModelId = ResourceLocation.fromNamespaceAndPath(modelId.getNamespace(), modelId.getPath() + "_on_slab");

        net.minecraft.data.models.model.ModelTemplates.CUBE_ALL.create(modelId, textures, blockStateModelGenerator.modelOutput);
        net.minecraft.data.models.model.ModelTemplates.CUBE_ALL.create(onSlabModelId, textures, blockStateModelGenerator.modelOutput);

        blockStateModelGenerator.blockStateOutput.accept(net.minecraft.data.models.blockstates.MultiVariantGenerator.multiVariant(this)
            .with(net.minecraft.data.models.blockstates.PropertyDispatch.property(ON_SLAB)
                .select(false, new net.minecraft.data.models.blockstates.Variant()
                    .with(net.minecraft.data.models.blockstates.VariantProperties.MODEL, modelId))
                .select(true, new net.minecraft.data.models.blockstates.Variant()
                    .with(net.minecraft.data.models.blockstates.VariantProperties.MODEL, onSlabModelId))));

        net.minecraft.data.models.model.ModelTemplates.FLAT_HANDHELD_ITEM.create(
                net.minecraft.data.models.model.ModelLocationUtils.getModelLocation(asItem()),
                TextureMapping.layer0(texture),
                blockStateModelGenerator.modelOutput);
    }

    protected static class AxisFacing extends RoadMarkBlock {
        public static final EnumProperty<FourHorizontalAxis> AXIS = EnumProperty.create("axis", FourHorizontalAxis.class);

        protected AxisFacing(ResourceLocation texture, Properties settings) {
            super(texture, settings);
            registerDefaultState(defaultBlockState().setValue(AXIS, FourHorizontalAxis.X));
        }

        @Override
        protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
            super.createBlockStateDefinition(builder);
            builder.add(AXIS);
        }

        @Override
        public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
            final BlockState state = super.getStateForPlacement(ctx);
            if (state != null) {
                return state.setValue(AXIS, FourHorizontalAxis.fromDirection(ctx.getHorizontalDirection()));
            }
            return null;
        }

        @Override
        public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
            FourHorizontalAxis axis = state.getValue(AXIS);
            if (axis == FourHorizontalAxis.X) {
                return state.getValue(ON_SLAB) ? SHAPE_ON_SLAB_X : SHAPE_X;
            } else if (axis == FourHorizontalAxis.Z) {
                return state.getValue(ON_SLAB) ? SHAPE_ON_SLAB_Z : SHAPE_Z;
            } else {
                return super.getShape(state, world, pos, context);
            }
        }

        @SuppressWarnings("deprecation")
        @Override
        public BlockState rotate(BlockState state, Rotation rotation) {
            return super.rotate(state, rotation).setValue(AXIS, state.getValue(AXIS).rotate(rotation));
        }

        @SuppressWarnings("deprecation")
        @Override
        public BlockState mirror(BlockState state, Mirror mirror) {
            return super.mirror(state, mirror).setValue(AXIS, state.getValue(AXIS).mirror());
        }

        @Override
        public void registerModels(ModelProvider modelProvider, BlockModelGenerators blockStateModelGenerator) {
            final TextureMapping textures = TextureMapping.cube(texture);
            final ResourceLocation modelId = ModelLocationUtils.getModelLocation(this);
            final ResourceLocation onSlabModelId = ResourceLocation.fromNamespaceAndPath(modelId.getNamespace(), modelId.getPath() + "_on_slab");

            net.minecraft.data.models.model.ModelTemplates.CUBE_ALL.create(modelId, textures, blockStateModelGenerator.modelOutput);
            net.minecraft.data.models.model.ModelTemplates.CUBE_ALL.create(onSlabModelId, textures, blockStateModelGenerator.modelOutput);
            blockStateModelGenerator.blockStateOutput.accept(
                    net.minecraft.data.models.blockstates.MultiVariantGenerator.multiVariant(this)
                .with(net.minecraft.data.models.blockstates.PropertyDispatch.properties(ON_SLAB, AXIS)
                    .select(false, FourHorizontalAxis.X,
                            new net.minecraft.data.models.blockstates.Variant()
                                .with(net.minecraft.data.models.blockstates.VariantProperties.MODEL, modelId))
                    .select(false, FourHorizontalAxis.Z,
                            new net.minecraft.data.models.blockstates.Variant()
                                .with(net.minecraft.data.models.blockstates.VariantProperties.MODEL, modelId))
                    .select(true, FourHorizontalAxis.X,
                            new net.minecraft.data.models.blockstates.Variant()
                                .with(net.minecraft.data.models.blockstates.VariantProperties.MODEL, onSlabModelId))
                    .select(true, FourHorizontalAxis.Z,
                            new net.minecraft.data.models.blockstates.Variant()
                                .with(net.minecraft.data.models.blockstates.VariantProperties.MODEL, onSlabModelId))
                )
            );

            net.minecraft.data.models.model.ModelTemplates.FLAT_HANDHELD_ITEM.create(
                    net.minecraft.data.models.model.ModelLocationUtils.getModelLocation(asItem()),
                    TextureMapping.layer0(texture),
                    blockStateModelGenerator.modelOutput);
        }
    }

    protected static class DirectionalFacing extends RoadMarkBlock {
        public static final EnumProperty<EightHorizontalDirection> FACING =
                EnumProperty.create("facing", EightHorizontalDirection.class);

        public DirectionalFacing(ResourceLocation texture, Properties settings) {
            super(texture, settings);
            registerDefaultState(defaultBlockState().setValue(FACING, EightHorizontalDirection.SOUTH));
        }

        @Override
        protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
            super.createBlockStateDefinition(builder);
            builder.add(FACING);
        }

        @Override
        public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
            final BlockState state = super.getStateForPlacement(ctx);
            if (state != null) {
                return state.setValue(FACING, EightHorizontalDirection.fromRotation(ctx.getRotation()));
            }
            return null;
        }

        @Override
        public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
            FourHorizontalAxis axis = state.getValue(FACING).axis;
            if (axis == FourHorizontalAxis.X) {
                return state.getValue(ON_SLAB) ? SHAPE_ON_SLAB_X : SHAPE_X;
            } else if (axis == FourHorizontalAxis.Z) {
                return state.getValue(ON_SLAB) ? SHAPE_ON_SLAB_Z : SHAPE_Z;
            } else {
                return super.getShape(state, world, pos, context);
            }
        }

        @SuppressWarnings("deprecation")
        @Override
        public BlockState rotate(BlockState state, Rotation rotation) {
            return super.rotate(state, rotation).setValue(FACING, state.getValue(FACING).rotate(rotation));
        }

        @SuppressWarnings("deprecation")
        @Override
        public BlockState mirror(BlockState state, Mirror mirror) {
            return super.mirror(state, mirror).setValue(FACING, state.getValue(FACING).mirror(mirror));
        }

        @Override
        public void registerModels(ModelProvider modelProvider, BlockModelGenerators blockStateModelGenerator) {
            final TextureMapping textures = TextureMapping.cube(texture);
            final ResourceLocation modelId = ModelLocationUtils.getModelLocation(this);
            final ResourceLocation rotatedModelId = ResourceLocation.fromNamespaceAndPath(modelId.getNamespace(), modelId.getPath() + "_rotated");
            final ResourceLocation onSlabModelId = ResourceLocation.fromNamespaceAndPath(modelId.getNamespace(), modelId.getPath() + "_on_slab");
            final ResourceLocation onSlabRotatedModelId = ResourceLocation.fromNamespaceAndPath(modelId.getNamespace(), modelId.getPath() + "_on_slab_rotated");

            net.minecraft.data.models.model.ModelTemplates.CUBE_ALL.create(modelId, textures, blockStateModelGenerator.modelOutput);
            net.minecraft.data.models.model.ModelTemplates.CUBE_ALL.create(rotatedModelId, textures, blockStateModelGenerator.modelOutput);
            net.minecraft.data.models.model.ModelTemplates.CUBE_ALL.create(onSlabModelId, textures, blockStateModelGenerator.modelOutput);
            net.minecraft.data.models.model.ModelTemplates.CUBE_ALL.create(onSlabRotatedModelId, textures, blockStateModelGenerator.modelOutput);

            final net.minecraft.data.models.blockstates.PropertyDispatch.C2<Boolean, EightHorizontalDirection> map =
                    net.minecraft.data.models.blockstates.PropertyDispatch.properties(ON_SLAB, FACING);
            for (EightHorizontalDirection direction : EightHorizontalDirection.VALUES) {
                int rotation = (int) direction.asRotation();
                boolean isDiagonal = direction.right().isPresent();
                if (isDiagonal) {
                    rotation -= 45;
                }

                map.select(false, direction,
                        net.minecraft.data.models.blockstates.Variant.variant()
                            .with(net.minecraft.data.models.blockstates.VariantProperties.MODEL,
                                    isDiagonal ? rotatedModelId : modelId)
                            .with(Y_VARIANT, rotation));
                map.select(true, direction,
                        net.minecraft.data.models.blockstates.Variant.variant()
                            .with(net.minecraft.data.models.blockstates.VariantProperties.MODEL,
                                    isDiagonal ? onSlabRotatedModelId : onSlabModelId)
                            .with(Y_VARIANT, rotation));
            }

            blockStateModelGenerator.blockStateOutput.accept(
                    net.minecraft.data.models.blockstates.MultiVariantGenerator.multiVariant(this).with(map));

            net.minecraft.data.models.model.ModelTemplates.FLAT_HANDHELD_ITEM.create(
                    net.minecraft.data.models.model.ModelLocationUtils.getModelLocation(asItem()),
                    TextureMapping.layer0(texture),
                    blockStateModelGenerator.modelOutput);
        }
    }
}
