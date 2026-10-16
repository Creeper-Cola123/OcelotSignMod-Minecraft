package bklmc.ocelotsign.block.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

/**
 * 立柱方块
 */
public class PillarBlock extends Block {
    public static final DirectionProperty FACING = DirectionProperty.of("facing",
            Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST);

    public enum PillarType {
        UPRIGHT_A,
        UPRIGHT_B_LEFT,
        UPRIGHT_B_RIGHT,
        UPRIGHT_B_DOUBLE,
        UPRIGHT_C,
        UPRIGHT_C_HALF,
        UPRIGHT_C_INCLINED_LEFT,
        UPRIGHT_C_INCLINED_RIGHT,
        HORIZONTAL_A,
        HORIZONTAL_D,
        HORIZONTAL_D_UP,
        HORIZONTAL_D_DOWN,
        THREE_SIDES_A_LEFT,
        THREE_SIDES_A_RIGHT,
        THREE_SIDES_A_TOP_LEFT,
        THREE_SIDES_A_TOP_RIGHT,
        FOUR_SIDES_A,
        FOUR_SIDES_A_TOP,
        FOUR_SIDES_D,
        FOUR_SIDES_D_LEFT,
        FOUR_SIDES_D_RIGHT
    }

    private final PillarType type;

    public static final VoxelShape UPRIGHT_A_N = Block.createCuboidShape(5, 0, 0, 11, 16, 6);
    public static final VoxelShape UPRIGHT_A_S = Block.createCuboidShape(5, 0, 10, 11, 16, 16);
    public static final VoxelShape UPRIGHT_A_E = Block.createCuboidShape(10, 0, 5, 16, 16, 11);
    public static final VoxelShape UPRIGHT_A_W = Block.createCuboidShape(0, 0, 5, 6, 16, 11);

    public static final VoxelShape UPRIGHT_B_LEFT_N = Block.createCuboidShape(11, 0, 0, 13, 16, 2);
    public static final VoxelShape UPRIGHT_B_LEFT_S = Block.createCuboidShape(3, 0, 14, 5, 16, 16);
    public static final VoxelShape UPRIGHT_B_LEFT_E = Block.createCuboidShape(14, 0, 11, 16, 16, 13);
    public static final VoxelShape UPRIGHT_B_LEFT_W = Block.createCuboidShape(0, 0, 3, 2, 16, 5);

    public static final VoxelShape UPRIGHT_B_RIGHT_N = Block.createCuboidShape(3, 0, 0, 5, 16, 2);
    public static final VoxelShape UPRIGHT_B_RIGHT_S = Block.createCuboidShape(11, 0, 14, 13, 16, 16);
    public static final VoxelShape UPRIGHT_B_RIGHT_E = Block.createCuboidShape(14, 0, 3, 16, 16, 5);
    public static final VoxelShape UPRIGHT_B_RIGHT_W = Block.createCuboidShape(0, 0, 11, 2, 16, 13);

    public static final VoxelShape UPRIGHT_B_DOUBLE_N = VoxelShapes.union(
            Block.createCuboidShape(11, 0, 0, 13, 16, 2),
            Block.createCuboidShape(3, 0, 0, 5, 16, 2));
    public static final VoxelShape UPRIGHT_B_DOUBLE_S = VoxelShapes.union(
            Block.createCuboidShape(3, 0, 14, 5, 16, 16),
            Block.createCuboidShape(11, 0, 14, 13, 16, 16));
    public static final VoxelShape UPRIGHT_B_DOUBLE_E = VoxelShapes.union(
            Block.createCuboidShape(14, 0, 11, 16, 16, 13),
            Block.createCuboidShape(14, 0, 3, 16, 16, 5));
    public static final VoxelShape UPRIGHT_B_DOUBLE_W = VoxelShapes.union(
            Block.createCuboidShape(0, 0, 3, 2, 16, 5),
            Block.createCuboidShape(0, 0, 11, 2, 16, 13));

    public static final VoxelShape UPRIGHT_C_N = Block.createCuboidShape(7, 0, 0, 9, 16, 2);
    public static final VoxelShape UPRIGHT_C_S = Block.createCuboidShape(7, 0, 14, 9, 16, 16);
    public static final VoxelShape UPRIGHT_C_E = Block.createCuboidShape(14, 0, 7, 16, 16, 9);
    public static final VoxelShape UPRIGHT_C_W = Block.createCuboidShape(0, 0, 7, 2, 16, 9);

    public static final VoxelShape UPRIGHT_C_HALF_N = Block.createCuboidShape(7, 0, 0, 9, 8, 2);
    public static final VoxelShape UPRIGHT_C_HALF_S = Block.createCuboidShape(7, 0, 14, 9, 8, 16);
    public static final VoxelShape UPRIGHT_C_HALF_E = Block.createCuboidShape(14, 0, 7, 16, 8, 9);
    public static final VoxelShape UPRIGHT_C_HALF_W = Block.createCuboidShape(0, 0, 7, 2, 8, 9);

    public static final VoxelShape UPRIGHT_C_INCLINED_LEFT_N = VoxelShapes.union(
            Block.createCuboidShape(7, 7, 0, 9, 16, 2),
            Block.createCuboidShape(6, 6, 0, 8, 8, 2),
            Block.createCuboidShape(5, 5, 0, 7, 7, 2),
            Block.createCuboidShape(4, 4, 0, 6, 6, 2),
            Block.createCuboidShape(3, 3, 0, 5, 5, 2),
            Block.createCuboidShape(2, 2, 0, 4, 4, 2),
            Block.createCuboidShape(1, 1, 0, 3, 3, 2),
            Block.createCuboidShape(0, 0, 0, 2, 2, 2));
    public static final VoxelShape UPRIGHT_C_INCLINED_LEFT_S = VoxelShapes.union(
            Block.createCuboidShape(7, 7, 14, 9, 16, 16),
            Block.createCuboidShape(8, 6, 14, 10, 8, 16),
            Block.createCuboidShape(9, 5, 14, 11, 7, 16),
            Block.createCuboidShape(10, 4, 14, 12, 6, 16),
            Block.createCuboidShape(11, 3, 14, 13, 5, 16),
            Block.createCuboidShape(12, 2, 14, 14, 4, 16),
            Block.createCuboidShape(13, 1, 14, 15, 3, 16),
            Block.createCuboidShape(14, 0, 14, 16, 2, 16));
    public static final VoxelShape UPRIGHT_C_INCLINED_LEFT_E = VoxelShapes.union(
            Block.createCuboidShape(14, 7, 7, 16, 16, 9),
            Block.createCuboidShape(14, 6, 6, 16, 8, 8),
            Block.createCuboidShape(14, 5, 5, 16, 7, 7),
            Block.createCuboidShape(14, 4, 4, 16, 6, 6),
            Block.createCuboidShape(14, 3, 3, 16, 5, 5),
            Block.createCuboidShape(14, 2, 2, 16, 4, 4),
            Block.createCuboidShape(14, 1, 1, 16, 3, 3),
            Block.createCuboidShape(14, 0, 0, 16, 2, 2));
    public static final VoxelShape UPRIGHT_C_INCLINED_LEFT_W = VoxelShapes.union(
            Block.createCuboidShape(0, 7, 7, 2, 16, 9),
            Block.createCuboidShape(0, 6, 8, 2, 8, 10),
            Block.createCuboidShape(0, 5, 9, 2, 7, 11),
            Block.createCuboidShape(0, 4, 10, 2, 6, 12),
            Block.createCuboidShape(0, 3, 11, 2, 5, 13),
            Block.createCuboidShape(0, 2, 12, 2, 4, 14),
            Block.createCuboidShape(0, 1, 13, 2, 3, 15),
            Block.createCuboidShape(0, 0, 14, 2, 2, 16));

    public static final VoxelShape UPRIGHT_C_INCLINED_RIGHT_N = VoxelShapes.union(
            Block.createCuboidShape(7, 7, 0, 9, 16, 2),
            Block.createCuboidShape(8, 6, 0, 10, 8, 2),
            Block.createCuboidShape(9, 5, 0, 11, 7, 2),
            Block.createCuboidShape(10, 4, 0, 12, 6, 2),
            Block.createCuboidShape(11, 3, 0, 13, 5, 2),
            Block.createCuboidShape(12, 2, 0, 14, 4, 2),
            Block.createCuboidShape(13, 1, 0, 15, 3, 2),
            Block.createCuboidShape(14, 0, 0, 16, 2, 2));
    public static final VoxelShape UPRIGHT_C_INCLINED_RIGHT_S = VoxelShapes.union(
            Block.createCuboidShape(7, 7, 14, 9, 16, 16),
            Block.createCuboidShape(6, 6, 14, 8, 8, 16),
            Block.createCuboidShape(5, 5, 14, 7, 7, 16),
            Block.createCuboidShape(4, 4, 14, 6, 6, 16),
            Block.createCuboidShape(3, 3, 14, 5, 5, 16),
            Block.createCuboidShape(2, 2, 14, 4, 4, 16),
            Block.createCuboidShape(1, 1, 14, 3, 3, 16),
            Block.createCuboidShape(0, 0, 14, 2, 2, 16));
    public static final VoxelShape UPRIGHT_C_INCLINED_RIGHT_E = VoxelShapes.union(
            Block.createCuboidShape(14, 7, 7, 16, 16, 9),
            Block.createCuboidShape(14, 6, 8, 16, 8, 10),
            Block.createCuboidShape(14, 5, 9, 16, 7, 11),
            Block.createCuboidShape(14, 4, 10, 16, 6, 12),
            Block.createCuboidShape(14, 3, 11, 16, 5, 13),
            Block.createCuboidShape(14, 2, 12, 16, 4, 14),
            Block.createCuboidShape(14, 1, 13, 16, 3, 15),
            Block.createCuboidShape(14, 0, 14, 16, 2, 16));
    public static final VoxelShape UPRIGHT_C_INCLINED_RIGHT_W = VoxelShapes.union(
            Block.createCuboidShape(0, 7, 7, 2, 16, 9),
            Block.createCuboidShape(0, 6, 6, 2, 8, 8),
            Block.createCuboidShape(0, 5, 5, 2, 7, 7),
            Block.createCuboidShape(0, 4, 4, 2, 6, 6),
            Block.createCuboidShape(0, 3, 3, 2, 5, 5),
            Block.createCuboidShape(0, 2, 2, 2, 4, 4),
            Block.createCuboidShape(0, 1, 1, 2, 3, 3),
            Block.createCuboidShape(0, 0, 0, 2, 2, 2));

    public static final VoxelShape HORIZONTAL_A_N = VoxelShapes.union(
            Block.createCuboidShape(8, 7, 1, 16, 9, 3),
            Block.createCuboidShape(0, 7, 1, 8, 9, 3));
    public static final VoxelShape HORIZONTAL_A_S = VoxelShapes.union(
            Block.createCuboidShape(0, 7, 13, 8, 9, 15),
            Block.createCuboidShape(8, 7, 13, 16, 9, 15));
    public static final VoxelShape HORIZONTAL_A_E = VoxelShapes.union(
            Block.createCuboidShape(13, 7, 8, 15, 9, 16),
            Block.createCuboidShape(13, 7, 0, 15, 9, 8));
    public static final VoxelShape HORIZONTAL_A_W = VoxelShapes.union(
            Block.createCuboidShape(1, 7, 0, 3, 9, 8),
            Block.createCuboidShape(1, 7, 8, 3, 9, 16));

    public static final VoxelShape HORIZONTAL_D_N = VoxelShapes.union(
            Block.createCuboidShape(0, 7, 3, 16, 9, 4),
            Block.createCuboidShape(0, 6, 1, 16, 10, 3),
            Block.createCuboidShape(0, 7, 0, 16, 9, 1));
    public static final VoxelShape HORIZONTAL_D_S = VoxelShapes.union(
            Block.createCuboidShape(0, 7, 12, 16, 9, 13),
            Block.createCuboidShape(0, 6, 13, 16, 10, 15),
            Block.createCuboidShape(0, 7, 15, 16, 9, 16));
    public static final VoxelShape HORIZONTAL_D_E = VoxelShapes.union(
            Block.createCuboidShape(12, 7, 0, 13, 9, 16),
            Block.createCuboidShape(13, 6, 0, 15, 10, 16),
            Block.createCuboidShape(15, 7, 0, 16, 9, 16));
    public static final VoxelShape HORIZONTAL_D_W = VoxelShapes.union(
            Block.createCuboidShape(3, 7, 0, 4, 9, 16),
            Block.createCuboidShape(1, 6, 0, 3, 10, 16),
            Block.createCuboidShape(0, 7, 0, 1, 9, 16));

    public static final VoxelShape HORIZONTAL_D_UP_N = VoxelShapes.union(
            Block.createCuboidShape(0, 7, 3, 16, 9, 4),
            Block.createCuboidShape(0, 6, 1, 16, 10, 3),
            Block.createCuboidShape(0, 7, 0, 16, 9, 1),
            Block.createCuboidShape(7, 0, 3, 9, 7, 4),
            Block.createCuboidShape(6, 0, 1, 10, 6, 3),
            Block.createCuboidShape(7, 0, 0, 9, 7, 1));
    public static final VoxelShape HORIZONTAL_D_UP_S = VoxelShapes.union(
            Block.createCuboidShape(0, 7, 12, 16, 9, 13),
            Block.createCuboidShape(0, 6, 13, 16, 10, 15),
            Block.createCuboidShape(0, 7, 15, 16, 9, 16),
            Block.createCuboidShape(7, 0, 12, 9, 7, 13),
            Block.createCuboidShape(6, 0, 13, 10, 6, 15),
            Block.createCuboidShape(7, 0, 15, 9, 7, 16));
    public static final VoxelShape HORIZONTAL_D_UP_E = VoxelShapes.union(
            Block.createCuboidShape(12, 7, 0, 13, 9, 16),
            Block.createCuboidShape(13, 6, 0, 15, 10, 16),
            Block.createCuboidShape(15, 7, 0, 16, 9, 16),
            Block.createCuboidShape(12, 0, 7, 13, 7, 9),
            Block.createCuboidShape(13, 0, 6, 15, 6, 10),
            Block.createCuboidShape(15, 0, 7, 16, 7, 9));
    public static final VoxelShape HORIZONTAL_D_UP_W = VoxelShapes.union(
            Block.createCuboidShape(3, 7, 0, 4, 9, 16),
            Block.createCuboidShape(1, 6, 0, 3, 10, 16),
            Block.createCuboidShape(0, 7, 0, 1, 9, 16),
            Block.createCuboidShape(3, 0, 7, 4, 7, 9),
            Block.createCuboidShape(1, 0, 6, 3, 6, 10),
            Block.createCuboidShape(0, 0, 7, 1, 7, 9));

    public static final VoxelShape HORIZONTAL_D_DOWN_N = VoxelShapes.union(
            Block.createCuboidShape(0, 7, 3, 16, 9, 4),
            Block.createCuboidShape(0, 6, 1, 16, 10, 3),
            Block.createCuboidShape(0, 7, 0, 16, 9, 1),
            Block.createCuboidShape(7, 9, 3, 9, 16, 4),
            Block.createCuboidShape(6, 10, 1, 10, 16, 3),
            Block.createCuboidShape(7, 9, 0, 9, 16, 1));
    public static final VoxelShape HORIZONTAL_D_DOWN_S = VoxelShapes.union(
            Block.createCuboidShape(0, 7, 12, 16, 9, 13),
            Block.createCuboidShape(0, 6, 13, 16, 10, 15),
            Block.createCuboidShape(0, 7, 15, 16, 9, 16),
            Block.createCuboidShape(7, 9, 12, 9, 16, 13),
            Block.createCuboidShape(6, 10, 13, 10, 16, 15),
            Block.createCuboidShape(7, 9, 15, 9, 16, 16));
    public static final VoxelShape HORIZONTAL_D_DOWN_E = VoxelShapes.union(
            Block.createCuboidShape(12, 7, 0, 13, 9, 16),
            Block.createCuboidShape(13, 6, 0, 15, 10, 16),
            Block.createCuboidShape(15, 7, 0, 16, 9, 16),
            Block.createCuboidShape(12, 9, 7, 13, 16, 9),
            Block.createCuboidShape(13, 10, 6, 15, 16, 10),
            Block.createCuboidShape(15, 9, 7, 16, 16, 9));
    public static final VoxelShape HORIZONTAL_D_DOWN_W = VoxelShapes.union(
            Block.createCuboidShape(3, 7, 0, 4, 9, 16),
            Block.createCuboidShape(1, 6, 0, 3, 10, 16),
            Block.createCuboidShape(0, 7, 0, 1, 9, 16),
            Block.createCuboidShape(3, 9, 7, 4, 16, 9),
            Block.createCuboidShape(1, 10, 6, 3, 16, 10),
            Block.createCuboidShape(0, 9, 7, 1, 16, 9));

    public static final VoxelShape THREE_SIDES_A_LEFT_N = VoxelShapes.union(
            Block.createCuboidShape(8, 7, 1, 16, 9, 3),
            Block.createCuboidShape(5, 0, 1, 6, 16, 5),
            Block.createCuboidShape(6, 0, 0, 10, 16, 6),
            Block.createCuboidShape(10, 0, 1, 11, 16, 5));
    public static final VoxelShape THREE_SIDES_A_LEFT_S = VoxelShapes.union(
            Block.createCuboidShape(0, 7, 13, 8, 9, 15),
            Block.createCuboidShape(5, 0, 11, 6, 16, 15),
            Block.createCuboidShape(6, 0, 10, 10, 16, 16),
            Block.createCuboidShape(10, 0, 11, 11, 16, 15));
    public static final VoxelShape THREE_SIDES_A_LEFT_E = VoxelShapes.union(
            Block.createCuboidShape(13, 7, 8, 15, 9, 16),
            Block.createCuboidShape(11, 0, 5, 15, 16, 6),
            Block.createCuboidShape(10, 0, 6, 16, 16, 10),
            Block.createCuboidShape(11, 0, 10, 15, 16, 11));
    public static final VoxelShape THREE_SIDES_A_LEFT_W = VoxelShapes.union(
            Block.createCuboidShape(1, 7, 0, 3, 9, 8),
            Block.createCuboidShape(1, 0, 5, 5, 16, 6),
            Block.createCuboidShape(0, 0, 6, 6, 16, 10),
            Block.createCuboidShape(1, 0, 10, 5, 16, 11));

    public static final VoxelShape THREE_SIDES_A_RIGHT_N = VoxelShapes.union(
            Block.createCuboidShape(0, 7, 1, 8, 9, 3),
            Block.createCuboidShape(5, 0, 1, 6, 16, 5),
            Block.createCuboidShape(6, 0, 0, 10, 16, 6),
            Block.createCuboidShape(10, 0, 1, 11, 16, 5));
    public static final VoxelShape THREE_SIDES_A_RIGHT_S = VoxelShapes.union(
            Block.createCuboidShape(8, 7, 13, 16, 9, 15),
            Block.createCuboidShape(5, 0, 11, 6, 16, 15),
            Block.createCuboidShape(6, 0, 10, 10, 16, 16),
            Block.createCuboidShape(10, 0, 11, 11, 16, 15));
    public static final VoxelShape THREE_SIDES_A_RIGHT_E = VoxelShapes.union(
            Block.createCuboidShape(13, 7, 0, 15, 9, 8),
            Block.createCuboidShape(11, 0, 5, 15, 16, 6),
            Block.createCuboidShape(10, 0, 6, 16, 16, 10),
            Block.createCuboidShape(11, 0, 10, 15, 16, 11));
    public static final VoxelShape THREE_SIDES_A_RIGHT_W = VoxelShapes.union(
            Block.createCuboidShape(1, 7, 8, 3, 9, 16),
            Block.createCuboidShape(1, 0, 5, 5, 16, 6),
            Block.createCuboidShape(0, 0, 6, 6, 16, 10),
            Block.createCuboidShape(1, 0, 10, 5, 16, 11));

    public static final VoxelShape THREE_SIDES_A_TOP_LEFT_N = VoxelShapes.union(
            Block.createCuboidShape(8, 7, 1, 16, 9, 3),
            Block.createCuboidShape(5, 0, 1, 6, 12, 5),
            Block.createCuboidShape(6, 0, 0, 10, 12, 6),
            Block.createCuboidShape(10, 0, 1, 11, 12, 5));
    public static final VoxelShape THREE_SIDES_A_TOP_LEFT_S = VoxelShapes.union(
            Block.createCuboidShape(0, 7, 13, 8, 9, 15),
            Block.createCuboidShape(5, 0, 11, 6, 12, 15),
            Block.createCuboidShape(6, 0, 10, 10, 12, 16),
            Block.createCuboidShape(10, 0, 11, 11, 12, 15));
    public static final VoxelShape THREE_SIDES_A_TOP_LEFT_E = VoxelShapes.union(
            Block.createCuboidShape(13, 7, 8, 15, 9, 16),
            Block.createCuboidShape(11, 0, 5, 15, 12, 6),
            Block.createCuboidShape(10, 0, 6, 16, 12, 10),
            Block.createCuboidShape(11, 0, 10, 15, 12, 11));
    public static final VoxelShape THREE_SIDES_A_TOP_LEFT_W = VoxelShapes.union(
            Block.createCuboidShape(1, 7, 0, 3, 9, 8),
            Block.createCuboidShape(1, 0, 5, 5, 12, 6),
            Block.createCuboidShape(0, 0, 6, 6, 12, 10),
            Block.createCuboidShape(1, 0, 10, 5, 12, 11));

    public static final VoxelShape THREE_SIDES_A_TOP_RIGHT_N = VoxelShapes.union(
            Block.createCuboidShape(0, 7, 1, 8, 9, 3),
            Block.createCuboidShape(5, 0, 1, 6, 12, 5),
            Block.createCuboidShape(6, 0, 0, 10, 12, 6),
            Block.createCuboidShape(10, 0, 1, 11, 12, 5));
    public static final VoxelShape THREE_SIDES_A_TOP_RIGHT_S = VoxelShapes.union(
            Block.createCuboidShape(8, 7, 13, 16, 9, 15),
            Block.createCuboidShape(5, 0, 11, 6, 12, 15),
            Block.createCuboidShape(6, 0, 10, 10, 12, 16),
            Block.createCuboidShape(10, 0, 11, 11, 12, 15));
    public static final VoxelShape THREE_SIDES_A_TOP_RIGHT_E = VoxelShapes.union(
            Block.createCuboidShape(13, 7, 0, 15, 9, 8),
            Block.createCuboidShape(11, 0, 5, 15, 12, 6),
            Block.createCuboidShape(10, 0, 6, 16, 12, 10),
            Block.createCuboidShape(11, 0, 10, 15, 12, 11));
    public static final VoxelShape THREE_SIDES_A_TOP_RIGHT_W = VoxelShapes.union(
            Block.createCuboidShape(1, 7, 8, 3, 9, 16),
            Block.createCuboidShape(1, 0, 5, 5, 12, 6),
            Block.createCuboidShape(0, 0, 6, 6, 12, 10),
            Block.createCuboidShape(1, 0, 10, 5, 12, 11));

    public static final VoxelShape FOUR_SIDES_A_N = VoxelShapes.union(
            Block.createCuboidShape(0, 7, 1, 16, 9, 3),
            Block.createCuboidShape(5, 0, 1, 6, 16, 5),
            Block.createCuboidShape(6, 0, 0, 10, 16, 6),
            Block.createCuboidShape(10, 0, 1, 11, 16, 5));
    public static final VoxelShape FOUR_SIDES_A_S = VoxelShapes.union(
            Block.createCuboidShape(0, 7, 13, 16, 9, 15),
            Block.createCuboidShape(5, 0, 11, 6, 16, 15),
            Block.createCuboidShape(6, 0, 10, 10, 16, 16),
            Block.createCuboidShape(10, 0, 11, 11, 16, 15));
    public static final VoxelShape FOUR_SIDES_A_E = VoxelShapes.union(
            Block.createCuboidShape(13, 7, 0, 15, 9, 16),
            Block.createCuboidShape(11, 0, 5, 15, 16, 6),
            Block.createCuboidShape(10, 0, 6, 16, 16, 10),
            Block.createCuboidShape(11, 0, 10, 15, 16, 11));
    public static final VoxelShape FOUR_SIDES_A_W = VoxelShapes.union(
            Block.createCuboidShape(1, 7, 0, 3, 9, 16),
            Block.createCuboidShape(1, 0, 5, 5, 16, 6),
            Block.createCuboidShape(0, 0, 6, 6, 16, 10),
            Block.createCuboidShape(1, 0, 10, 5, 16, 11));

    public static final VoxelShape FOUR_SIDES_A_TOP_N = VoxelShapes.union(
            Block.createCuboidShape(0, 7, 1, 16, 9, 3),
            Block.createCuboidShape(5, 0, 1, 6, 12, 5),
            Block.createCuboidShape(6, 0, 0, 10, 12, 6),
            Block.createCuboidShape(10, 0, 1, 11, 12, 5));
    public static final VoxelShape FOUR_SIDES_A_TOP_S = VoxelShapes.union(
            Block.createCuboidShape(0, 7, 13, 16, 9, 15),
            Block.createCuboidShape(5, 0, 11, 6, 12, 15),
            Block.createCuboidShape(6, 0, 10, 10, 12, 16),
            Block.createCuboidShape(10, 0, 11, 11, 12, 15));
    public static final VoxelShape FOUR_SIDES_A_TOP_E = VoxelShapes.union(
            Block.createCuboidShape(13, 7, 0, 15, 9, 16),
            Block.createCuboidShape(11, 0, 5, 15, 12, 6),
            Block.createCuboidShape(10, 0, 6, 16, 12, 10),
            Block.createCuboidShape(11, 0, 10, 15, 12, 11));
    public static final VoxelShape FOUR_SIDES_A_TOP_W = VoxelShapes.union(
            Block.createCuboidShape(1, 7, 0, 3, 9, 16),
            Block.createCuboidShape(1, 0, 5, 5, 12, 6),
            Block.createCuboidShape(0, 0, 6, 6, 12, 10),
            Block.createCuboidShape(1, 0, 10, 5, 12, 11));

    public static final VoxelShape FOUR_SIDES_D_N = VoxelShapes.union(
            Block.createCuboidShape(5, 0, 1, 6, 16, 5),
            Block.createCuboidShape(10, 0, 1, 11, 16, 5),
            Block.createCuboidShape(6, 0, 0, 10, 16, 6),
            Block.createCuboidShape(0, 7, 3, 16, 9, 4),
            Block.createCuboidShape(0, 6, 1, 16, 10, 3),
            Block.createCuboidShape(0, 7, 0, 16, 9, 1));
    public static final VoxelShape FOUR_SIDES_D_S = VoxelShapes.union(
            Block.createCuboidShape(10, 0, 11, 11, 16, 15),
            Block.createCuboidShape(5, 0, 11, 6, 16, 15),
            Block.createCuboidShape(6, 0, 10, 10, 16, 16),
            Block.createCuboidShape(0, 7, 12, 16, 9, 13),
            Block.createCuboidShape(0, 6, 13, 16, 10, 15),
            Block.createCuboidShape(0, 7, 15, 16, 9, 16));
    public static final VoxelShape FOUR_SIDES_D_E = VoxelShapes.union(
            Block.createCuboidShape(11, 0, 5, 15, 16, 6),
            Block.createCuboidShape(11, 0, 10, 15, 16, 11),
            Block.createCuboidShape(10, 0, 6, 16, 16, 10),
            Block.createCuboidShape(12, 7, 0, 13, 9, 16),
            Block.createCuboidShape(13, 6, 0, 15, 10, 16),
            Block.createCuboidShape(15, 7, 0, 16, 9, 16));
    public static final VoxelShape FOUR_SIDES_D_W = VoxelShapes.union(
            Block.createCuboidShape(1, 0, 10, 5, 16, 11),
            Block.createCuboidShape(1, 0, 5, 5, 16, 6),
            Block.createCuboidShape(0, 0, 6, 6, 16, 10),
            Block.createCuboidShape(3, 7, 0, 4, 9, 16),
            Block.createCuboidShape(1, 6, 0, 3, 10, 16),
            Block.createCuboidShape(0, 7, 0, 1, 9, 16));

    public static final VoxelShape FOUR_SIDES_D_LEFT_N = VoxelShapes.union(
            Block.createCuboidShape(5, 0, 1, 6, 16, 5),
            Block.createCuboidShape(10, 0, 1, 11, 16, 5),
            Block.createCuboidShape(6, 0, 0, 10, 16, 6),
            Block.createCuboidShape(11, 7, 3, 16, 9, 4),
            Block.createCuboidShape(11, 6, 1, 16, 10, 3),
            Block.createCuboidShape(10, 7, 0, 16, 9, 1));
    public static final VoxelShape FOUR_SIDES_D_LEFT_S = VoxelShapes.union(
            Block.createCuboidShape(10, 0, 11, 11, 16, 15),
            Block.createCuboidShape(5, 0, 11, 6, 16, 15),
            Block.createCuboidShape(6, 0, 10, 10, 16, 16),
            Block.createCuboidShape(0, 7, 12, 5, 9, 13),
            Block.createCuboidShape(0, 6, 13, 5, 10, 15),
            Block.createCuboidShape(0, 7, 15, 6, 9, 16));
    public static final VoxelShape FOUR_SIDES_D_LEFT_E = VoxelShapes.union(
            Block.createCuboidShape(11, 0, 5, 15, 16, 6),
            Block.createCuboidShape(11, 0, 10, 15, 16, 11),
            Block.createCuboidShape(10, 0, 6, 16, 16, 10),
            Block.createCuboidShape(12, 7, 11, 13, 9, 16),
            Block.createCuboidShape(13, 6, 11, 15, 10, 16),
            Block.createCuboidShape(15, 7, 10, 16, 9, 16));
    public static final VoxelShape FOUR_SIDES_D_LEFT_W = VoxelShapes.union(
            Block.createCuboidShape(1, 0, 10, 5, 16, 11),
            Block.createCuboidShape(1, 0, 5, 5, 16, 6),
            Block.createCuboidShape(0, 0, 6, 6, 16, 10),
            Block.createCuboidShape(3, 7, 0, 4, 9, 5),
            Block.createCuboidShape(1, 6, 0, 3, 10, 5),
            Block.createCuboidShape(0, 7, 0, 1, 9, 6));

    public static final VoxelShape FOUR_SIDES_D_RIGHT_N = VoxelShapes.union(
            Block.createCuboidShape(5, 0, 1, 6, 16, 5),
            Block.createCuboidShape(10, 0, 1, 11, 16, 5),
            Block.createCuboidShape(6, 0, 0, 10, 16, 6),
            Block.createCuboidShape(0, 7, 3, 5, 9, 4),
            Block.createCuboidShape(0, 6, 1, 5, 10, 3),
            Block.createCuboidShape(0, 7, 0, 6, 9, 1));
    public static final VoxelShape FOUR_SIDES_D_RIGHT_S = VoxelShapes.union(
            Block.createCuboidShape(10, 0, 11, 11, 16, 15),
            Block.createCuboidShape(5, 0, 11, 6, 16, 15),
            Block.createCuboidShape(6, 0, 10, 10, 16, 16),
            Block.createCuboidShape(11, 7, 12, 16, 9, 13),
            Block.createCuboidShape(11, 6, 13, 16, 10, 15),
            Block.createCuboidShape(10, 7, 15, 16, 9, 16));
    public static final VoxelShape FOUR_SIDES_D_RIGHT_E = VoxelShapes.union(
            Block.createCuboidShape(11, 0, 5, 15, 16, 6),
            Block.createCuboidShape(11, 0, 10, 15, 16, 11),
            Block.createCuboidShape(10, 0, 6, 16, 16, 10),
            Block.createCuboidShape(12, 7, 0, 13, 9, 5),
            Block.createCuboidShape(13, 6, 0, 15, 10, 5),
            Block.createCuboidShape(15, 7, 0, 16, 9, 6));
    public static final VoxelShape FOUR_SIDES_D_RIGHT_W = VoxelShapes.union(
            Block.createCuboidShape(1, 0, 10, 5, 16, 11),
            Block.createCuboidShape(1, 0, 5, 5, 16, 6),
            Block.createCuboidShape(0, 0, 6, 6, 16, 10),
            Block.createCuboidShape(3, 7, 11, 4, 9, 16),
            Block.createCuboidShape(1, 6, 11, 3, 10, 16),
            Block.createCuboidShape(0, 7, 10, 1, 9, 16));

    /**
     * @param type     立柱类型
     * @param settings 方块设置
     */
    public PillarBlock(PillarType type, Settings settings) {
        super(settings);
        this.type = type;
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
        return switch (type) {
            case UPRIGHT_A -> switch (facing) {
                case NORTH -> UPRIGHT_A_N;
                case SOUTH -> UPRIGHT_A_S;
                case EAST -> UPRIGHT_A_E;
                case WEST -> UPRIGHT_A_W;
                default -> UPRIGHT_A_N;
            };
            case UPRIGHT_B_LEFT -> switch (facing) {
                case NORTH -> UPRIGHT_B_LEFT_N;
                case SOUTH -> UPRIGHT_B_LEFT_S;
                case EAST -> UPRIGHT_B_LEFT_E;
                case WEST -> UPRIGHT_B_LEFT_W;
                default -> UPRIGHT_B_LEFT_N;
            };
            case UPRIGHT_B_RIGHT -> switch (facing) {
                case NORTH -> UPRIGHT_B_RIGHT_N;
                case SOUTH -> UPRIGHT_B_RIGHT_S;
                case EAST -> UPRIGHT_B_RIGHT_E;
                case WEST -> UPRIGHT_B_RIGHT_W;
                default -> UPRIGHT_B_RIGHT_N;
            };
            case UPRIGHT_B_DOUBLE -> switch (facing) {
                case NORTH -> UPRIGHT_B_DOUBLE_N;
                case SOUTH -> UPRIGHT_B_DOUBLE_S;
                case EAST -> UPRIGHT_B_DOUBLE_E;
                case WEST -> UPRIGHT_B_DOUBLE_W;
                default -> UPRIGHT_B_DOUBLE_N;
            };
            case UPRIGHT_C -> switch (facing) {
                case NORTH -> UPRIGHT_C_N;
                case SOUTH -> UPRIGHT_C_S;
                case EAST -> UPRIGHT_C_E;
                case WEST -> UPRIGHT_C_W;
                default -> UPRIGHT_C_N;
            };
            case UPRIGHT_C_HALF -> switch (facing) {
                case NORTH -> UPRIGHT_C_HALF_N;
                case SOUTH -> UPRIGHT_C_HALF_S;
                case EAST -> UPRIGHT_C_HALF_E;
                case WEST -> UPRIGHT_C_HALF_W;
                default -> UPRIGHT_C_HALF_N;
            };
            case UPRIGHT_C_INCLINED_LEFT -> switch (facing) {
                case NORTH -> UPRIGHT_C_INCLINED_LEFT_N;
                case SOUTH -> UPRIGHT_C_INCLINED_LEFT_S;
                case EAST -> UPRIGHT_C_INCLINED_LEFT_E;
                case WEST -> UPRIGHT_C_INCLINED_LEFT_W;
                default -> UPRIGHT_C_INCLINED_LEFT_N;
            };
            case UPRIGHT_C_INCLINED_RIGHT -> switch (facing) {
                case NORTH -> UPRIGHT_C_INCLINED_RIGHT_N;
                case SOUTH -> UPRIGHT_C_INCLINED_RIGHT_S;
                case EAST -> UPRIGHT_C_INCLINED_RIGHT_E;
                case WEST -> UPRIGHT_C_INCLINED_RIGHT_W;
                default -> UPRIGHT_C_INCLINED_RIGHT_N;
            };
            case HORIZONTAL_A -> switch (facing) {
                case NORTH -> HORIZONTAL_A_N;
                case SOUTH -> HORIZONTAL_A_S;
                case EAST -> HORIZONTAL_A_E;
                case WEST -> HORIZONTAL_A_W;
                default -> HORIZONTAL_A_N;
            };
            case HORIZONTAL_D -> switch (facing) {
                case NORTH -> HORIZONTAL_D_N;
                case SOUTH -> HORIZONTAL_D_S;
                case EAST -> HORIZONTAL_D_E;
                case WEST -> HORIZONTAL_D_W;
                default -> HORIZONTAL_D_N;
            };
            case HORIZONTAL_D_UP -> switch (facing) {
                case NORTH -> HORIZONTAL_D_UP_N;
                case SOUTH -> HORIZONTAL_D_UP_S;
                case EAST -> HORIZONTAL_D_UP_E;
                case WEST -> HORIZONTAL_D_UP_W;
                default -> HORIZONTAL_D_UP_N;
            };
            case HORIZONTAL_D_DOWN -> switch (facing) {
                case NORTH -> HORIZONTAL_D_DOWN_N;
                case SOUTH -> HORIZONTAL_D_DOWN_S;
                case EAST -> HORIZONTAL_D_DOWN_E;
                case WEST -> HORIZONTAL_D_DOWN_W;
                default -> HORIZONTAL_D_DOWN_N;
            };
            case THREE_SIDES_A_LEFT -> switch (facing) {
                case NORTH -> THREE_SIDES_A_LEFT_N;
                case SOUTH -> THREE_SIDES_A_LEFT_S;
                case EAST -> THREE_SIDES_A_LEFT_E;
                case WEST -> THREE_SIDES_A_LEFT_W;
                default -> THREE_SIDES_A_LEFT_N;
            };
            case THREE_SIDES_A_RIGHT -> switch (facing) {
                case NORTH -> THREE_SIDES_A_RIGHT_N;
                case SOUTH -> THREE_SIDES_A_RIGHT_S;
                case EAST -> THREE_SIDES_A_RIGHT_E;
                case WEST -> THREE_SIDES_A_RIGHT_W;
                default -> THREE_SIDES_A_RIGHT_N;
            };
            case THREE_SIDES_A_TOP_LEFT -> switch (facing) {
                case NORTH -> THREE_SIDES_A_TOP_LEFT_N;
                case SOUTH -> THREE_SIDES_A_TOP_LEFT_S;
                case EAST -> THREE_SIDES_A_TOP_LEFT_E;
                case WEST -> THREE_SIDES_A_TOP_LEFT_W;
                default -> THREE_SIDES_A_TOP_LEFT_N;
            };
            case THREE_SIDES_A_TOP_RIGHT -> switch (facing) {
                case NORTH -> THREE_SIDES_A_TOP_RIGHT_N;
                case SOUTH -> THREE_SIDES_A_TOP_RIGHT_S;
                case EAST -> THREE_SIDES_A_TOP_RIGHT_E;
                case WEST -> THREE_SIDES_A_TOP_RIGHT_W;
                default -> THREE_SIDES_A_TOP_RIGHT_N;
            };
            case FOUR_SIDES_A -> switch (facing) {
                case NORTH -> FOUR_SIDES_A_N;
                case SOUTH -> FOUR_SIDES_A_S;
                case EAST -> FOUR_SIDES_A_E;
                case WEST -> FOUR_SIDES_A_W;
                default -> FOUR_SIDES_A_N;
            };
            case FOUR_SIDES_A_TOP -> switch (facing) {
                case NORTH -> FOUR_SIDES_A_TOP_N;
                case SOUTH -> FOUR_SIDES_A_TOP_S;
                case EAST -> FOUR_SIDES_A_TOP_E;
                case WEST -> FOUR_SIDES_A_TOP_W;
                default -> FOUR_SIDES_A_TOP_N;
            };
            case FOUR_SIDES_D -> switch (facing) {
                case NORTH -> FOUR_SIDES_D_N;
                case SOUTH -> FOUR_SIDES_D_S;
                case EAST -> FOUR_SIDES_D_E;
                case WEST -> FOUR_SIDES_D_W;
                default -> FOUR_SIDES_D_N;
            };
            case FOUR_SIDES_D_LEFT -> switch (facing) {
                case NORTH -> FOUR_SIDES_D_LEFT_N;
                case SOUTH -> FOUR_SIDES_D_LEFT_S;
                case EAST -> FOUR_SIDES_D_LEFT_E;
                case WEST -> FOUR_SIDES_D_LEFT_W;
                default -> FOUR_SIDES_D_LEFT_N;
            };
            case FOUR_SIDES_D_RIGHT -> switch (facing) {
                case NORTH -> FOUR_SIDES_D_RIGHT_N;
                case SOUTH -> FOUR_SIDES_D_RIGHT_S;
                case EAST -> FOUR_SIDES_D_RIGHT_E;
                case WEST -> FOUR_SIDES_D_RIGHT_W;
                default -> FOUR_SIDES_D_RIGHT_N;
            };
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
