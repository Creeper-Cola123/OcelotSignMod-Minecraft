package bklmc.ocelotsign.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * 立柱方块
 */
public class PillarBlock extends Block {
    public static final EnumProperty<Direction> FACING = EnumProperty.create("facing", Direction.class,
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

    public static final VoxelShape UPRIGHT_A_N = Block.box(5, 0, 0, 11, 16, 6);
    public static final VoxelShape UPRIGHT_A_S = Block.box(5, 0, 10, 11, 16, 16);
    public static final VoxelShape UPRIGHT_A_E = Block.box(10, 0, 5, 16, 16, 11);
    public static final VoxelShape UPRIGHT_A_W = Block.box(0, 0, 5, 6, 16, 11);

    public static final VoxelShape UPRIGHT_B_LEFT_N = Block.box(11, 0, 0, 13, 16, 2);
    public static final VoxelShape UPRIGHT_B_LEFT_S = Block.box(3, 0, 14, 5, 16, 16);
    public static final VoxelShape UPRIGHT_B_LEFT_E = Block.box(14, 0, 11, 16, 16, 13);
    public static final VoxelShape UPRIGHT_B_LEFT_W = Block.box(0, 0, 3, 2, 16, 5);

    public static final VoxelShape UPRIGHT_B_RIGHT_N = Block.box(3, 0, 0, 5, 16, 2);
    public static final VoxelShape UPRIGHT_B_RIGHT_S = Block.box(11, 0, 14, 13, 16, 16);
    public static final VoxelShape UPRIGHT_B_RIGHT_E = Block.box(14, 0, 3, 16, 16, 5);
    public static final VoxelShape UPRIGHT_B_RIGHT_W = Block.box(0, 0, 11, 2, 16, 13);

    public static final VoxelShape UPRIGHT_B_DOUBLE_N = Shapes.or(
            Block.box(11, 0, 0, 13, 16, 2),
            Block.box(3, 0, 0, 5, 16, 2));
    public static final VoxelShape UPRIGHT_B_DOUBLE_S = Shapes.or(
            Block.box(3, 0, 14, 5, 16, 16),
            Block.box(11, 0, 14, 13, 16, 16));
    public static final VoxelShape UPRIGHT_B_DOUBLE_E = Shapes.or(
            Block.box(14, 0, 11, 16, 16, 13),
            Block.box(14, 0, 3, 16, 16, 5));
    public static final VoxelShape UPRIGHT_B_DOUBLE_W = Shapes.or(
            Block.box(0, 0, 3, 2, 16, 5),
            Block.box(0, 0, 11, 2, 16, 13));

    public static final VoxelShape UPRIGHT_C_N = Block.box(7, 0, 0, 9, 16, 2);
    public static final VoxelShape UPRIGHT_C_S = Block.box(7, 0, 14, 9, 16, 16);
    public static final VoxelShape UPRIGHT_C_E = Block.box(14, 0, 7, 16, 16, 9);
    public static final VoxelShape UPRIGHT_C_W = Block.box(0, 0, 7, 2, 16, 9);

    public static final VoxelShape UPRIGHT_C_HALF_N = Block.box(7, 0, 0, 9, 8, 2);
    public static final VoxelShape UPRIGHT_C_HALF_S = Block.box(7, 0, 14, 9, 8, 16);
    public static final VoxelShape UPRIGHT_C_HALF_E = Block.box(14, 0, 7, 16, 8, 9);
    public static final VoxelShape UPRIGHT_C_HALF_W = Block.box(0, 0, 7, 2, 8, 9);

    public static final VoxelShape UPRIGHT_C_INCLINED_LEFT_N = Shapes.or(
            Block.box(7,  7,  0,  9,  16, 2),
            Block.box(6,  6,  0,  8,  8,  2),
            Block.box(5,  5,  0,  7,  7,  2),
            Block.box(4,  4,  0,  6,  6,  2),
            Block.box(3,  3,  0,  5,  5,  2),
            Block.box(2,  2,  0,  4,  4,  2),
            Block.box(1,  1,  0,  3,  3,  2),
            Block.box(0,  0,  0,  2,  2,  2));
    public static final VoxelShape UPRIGHT_C_INCLINED_LEFT_S = Shapes.or(
            Block.box(7,  7,  14, 9,  16, 16),
            Block.box(8,  6,  14, 10, 8,  16),
            Block.box(9,  5,  14, 11, 7,  16),
            Block.box(10, 4,  14, 12, 6,  16),
            Block.box(11, 3,  14, 13, 5,  16),
            Block.box(12, 2,  14, 14, 4,  16),
            Block.box(13, 1,  14, 15, 3,  16),
            Block.box(14, 0,  14, 16, 2,  16));
    public static final VoxelShape UPRIGHT_C_INCLINED_LEFT_E = Shapes.or(
            Block.box(14, 7,  7,  16, 16, 9),
            Block.box(14, 6,  6,  16, 8,  8),
            Block.box(14, 5,  5,  16, 7,  7),
            Block.box(14, 4,  4,  16, 6,  6),
            Block.box(14, 3,  3,  16, 5,  5),
            Block.box(14, 2,  2,  16, 4,  4),
            Block.box(14, 1,  1,  16, 3,  3),
            Block.box(14, 0,  0,  16, 2,  2));
    public static final VoxelShape UPRIGHT_C_INCLINED_LEFT_W = Shapes.or(
            Block.box(0, 7,  7,  2,  16, 9),
            Block.box(0, 6,  8,  2,  8,  10),
            Block.box(0, 5,  9,  2,  7,  11),
            Block.box(0, 4,  10, 2,  6,  12),
            Block.box(0, 3,  11, 2,  5,  13),
            Block.box(0, 2,  12, 2,  4,  14),
            Block.box(0, 1,  13, 2,  3,  15),
            Block.box(0, 0,  14, 2,  2,  16));

    public static final VoxelShape UPRIGHT_C_INCLINED_RIGHT_N = Shapes.or(
            Block.box(7,  7,  0,  9,  16, 2),
            Block.box(8,  6,  0,  10, 8,  2),
            Block.box(9,  5,  0,  11, 7,  2),
            Block.box(10, 4,  0,  12, 6,  2),
            Block.box(11, 3,  0,  13, 5,  2),
            Block.box(12, 2,  0,  14, 4,  2),
            Block.box(13, 1,  0,  15, 3,  2),
            Block.box(14, 0,  0,  16, 2,  2));
    public static final VoxelShape UPRIGHT_C_INCLINED_RIGHT_S = Shapes.or(
            Block.box(7,  7,  14, 9,  16, 16),
            Block.box(8,  6,  14, 10, 8,  16),
            Block.box(9,  5,  14, 11, 7,  16),
            Block.box(10, 4,  14, 12, 6,  16),
            Block.box(11, 3,  14, 13, 5,  16),
            Block.box(12, 2,  14, 14, 4,  16),
            Block.box(13, 1,  14, 15, 3,  16),
            Block.box(14, 0,  14, 16, 2,  16));
    public static final VoxelShape UPRIGHT_C_INCLINED_RIGHT_E = Shapes.or(
            Block.box(14, 7,  7,  16, 16, 9),
            Block.box(14, 6,  8,  16, 8,  10),
            Block.box(14, 5,  9,  16, 7,  11),
            Block.box(14, 4,  10, 16, 6,  12),
            Block.box(14, 3,  11, 16, 5,  13),
            Block.box(14, 2,  12, 16, 4,  14),
            Block.box(14, 1,  13, 16, 3,  15),
            Block.box(14, 0,  14, 16, 2,  16));
    public static final VoxelShape UPRIGHT_C_INCLINED_RIGHT_W = Shapes.or(
            Block.box(0, 7,  7,  2,  16, 9),
            Block.box(0, 6,  6,  2,  8,  8),
            Block.box(0, 5,  5,  2,  7,  7),
            Block.box(0, 4,  4,  2,  6,  6),
            Block.box(0, 3,  3,  2,  5,  5),
            Block.box(0, 2,  2,  2,  4,  4),
            Block.box(0, 1,  1,  2,  3,  3),
            Block.box(0, 0,  0,  2,  2,  2));

    public static final VoxelShape HORIZONTAL_A_N = Shapes.or(
            Block.box(8, 7, 1, 16, 9, 3),
            Block.box(0, 7, 1, 8, 9, 3));
    public static final VoxelShape HORIZONTAL_A_S = Shapes.or(
            Block.box(0, 7, 13, 8, 9, 15),
            Block.box(8, 7, 13, 16, 9, 15));
    public static final VoxelShape HORIZONTAL_A_E = Shapes.or(
            Block.box(13, 7, 8, 15, 9, 16),
            Block.box(13, 7, 0, 15, 9, 8));
    public static final VoxelShape HORIZONTAL_A_W = Shapes.or(
            Block.box(1, 7, 0, 3, 9, 8),
            Block.box(1, 7, 8, 3, 9, 16));

    public static final VoxelShape HORIZONTAL_D_N = Shapes.or(
            Block.box(0, 7, 3, 16, 9, 4),
            Block.box(0, 6, 1, 16, 10, 3),
            Block.box(0, 7, 0, 16, 9, 1));
    public static final VoxelShape HORIZONTAL_D_S = Shapes.or(
            Block.box(0, 7, 12, 16, 9, 13),
            Block.box(0, 6, 13, 16, 10, 15),
            Block.box(0, 7, 15, 16, 9, 16));
    public static final VoxelShape HORIZONTAL_D_E = Shapes.or(
            Block.box(12, 7, 0, 13, 9, 16),
            Block.box(13, 6, 0, 15, 10, 16),
            Block.box(15, 7, 0, 16, 9, 16));
    public static final VoxelShape HORIZONTAL_D_W = Shapes.or(
            Block.box(3, 7, 0, 4, 9, 16),
            Block.box(1, 6, 0, 3, 10, 16),
            Block.box(0, 7, 0, 1, 9, 16));

    public static final VoxelShape HORIZONTAL_D_UP_N = Shapes.or(
            Block.box(0, 7, 3, 16, 9, 4),
            Block.box(0, 6, 1, 16, 10, 3),
            Block.box(0, 7, 0, 16, 9, 1),
            Block.box(7, 0, 3, 9, 7, 4),
            Block.box(6, 0, 1, 10, 6, 3),
            Block.box(7, 0, 0, 9, 7, 1));
    public static final VoxelShape HORIZONTAL_D_UP_S = Shapes.or(
            Block.box(0, 7, 12, 16, 9, 13),
            Block.box(0, 6, 13, 16, 10, 15),
            Block.box(0, 7, 15, 16, 9, 16),
            Block.box(7, 0, 12, 9, 7, 13),
            Block.box(6, 0, 13, 10, 6, 15),
            Block.box(7, 0, 15, 9, 7, 16));
    public static final VoxelShape HORIZONTAL_D_UP_E = Shapes.or(
            Block.box(12, 7, 0, 13, 9, 16),
            Block.box(13, 6, 0, 15, 10, 16),
            Block.box(15, 7, 0, 16, 9, 16),
            Block.box(12, 0, 7, 13, 7, 9),
            Block.box(13, 0, 6, 15, 6, 10),
            Block.box(15, 0, 7, 16, 7, 9));
    public static final VoxelShape HORIZONTAL_D_UP_W = Shapes.or(
            Block.box(3, 7, 0, 4, 9, 16),
            Block.box(1, 6, 0, 3, 10, 16),
            Block.box(0, 7, 0, 1, 9, 16),
            Block.box(3, 0, 7, 4, 7, 9),
            Block.box(1, 0, 6, 3, 6, 10),
            Block.box(0, 0, 7, 1, 7, 9));

    public static final VoxelShape HORIZONTAL_D_DOWN_N = Shapes.or(
            Block.box(0, 7, 3, 16, 9, 4),
            Block.box(0, 6, 1, 16, 10, 3),
            Block.box(0, 7, 0, 16, 9, 1),
            Block.box(7, 9, 3, 9, 16, 4),
            Block.box(6, 10, 1, 10, 16, 3),
            Block.box(7, 9, 0, 9, 16, 1));
    public static final VoxelShape HORIZONTAL_D_DOWN_S = Shapes.or(
            Block.box(0, 7, 12, 16, 9, 13),
            Block.box(0, 6, 13, 16, 10, 15),
            Block.box(0, 7, 15, 16, 9, 16),
            Block.box(7, 9, 12, 9, 16, 13),
            Block.box(6, 10, 13, 10, 16, 15),
            Block.box(7, 9, 15, 9, 16, 16));
    public static final VoxelShape HORIZONTAL_D_DOWN_E = Shapes.or(
            Block.box(12, 7, 0, 13, 9, 16),
            Block.box(13, 6, 0, 15, 10, 16),
            Block.box(15, 7, 0, 16, 9, 16),
            Block.box(12, 9, 7, 13, 16, 9),
            Block.box(13, 10, 6, 15, 16, 10),
            Block.box(15, 9, 7, 16, 16, 9));
    public static final VoxelShape HORIZONTAL_D_DOWN_W = Shapes.or(
            Block.box(3, 7, 0, 4, 9, 16),
            Block.box(1, 6, 0, 3, 10, 16),
            Block.box(0, 7, 0, 1, 9, 16),
            Block.box(3, 9, 7, 4, 16, 9),
            Block.box(1, 10, 6, 3, 16, 10),
            Block.box(0, 9, 7, 1, 16, 9));

    public static final VoxelShape THREE_SIDES_A_LEFT_N = Shapes.or(
            Block.box(8, 7, 1, 16, 9, 3),
            Block.box(5, 0, 1, 6, 16, 5),
            Block.box(6, 0, 0, 10, 16, 6),
            Block.box(10, 0, 1, 11, 16, 5));
    public static final VoxelShape THREE_SIDES_A_LEFT_S = Shapes.or(
            Block.box(0, 7, 13, 8, 9, 15),
            Block.box(5, 0, 11, 6, 16, 15),
            Block.box(6, 0, 10, 10, 16, 16),
            Block.box(10, 0, 11, 11, 16, 15));
    public static final VoxelShape THREE_SIDES_A_LEFT_E = Shapes.or(
            Block.box(13, 7, 8, 15, 9, 16),
            Block.box(11, 0, 5, 15, 16, 6),
            Block.box(10, 0, 6, 16, 16, 10),
            Block.box(11, 0, 10, 15, 16, 11));
    public static final VoxelShape THREE_SIDES_A_LEFT_W = Shapes.or(
            Block.box(1, 7, 0, 3, 9, 8),
            Block.box(1, 0, 5, 5, 16, 6),
            Block.box(0, 0, 6, 6, 16, 10),
            Block.box(1, 0, 10, 5, 16, 11));

    public static final VoxelShape THREE_SIDES_A_RIGHT_N = Shapes.or(
            Block.box(0, 7, 1, 8, 9, 3),
            Block.box(5, 0, 1, 6, 16, 5),
            Block.box(6, 0, 0, 10, 16, 6),
            Block.box(10, 0, 1, 11, 16, 5));
    public static final VoxelShape THREE_SIDES_A_RIGHT_S = Shapes.or(
            Block.box(8, 7, 13, 16, 9, 15),
            Block.box(5, 0, 11, 6, 16, 15),
            Block.box(6, 0, 10, 10, 16, 16),
            Block.box(10, 0, 11, 11, 16, 15));
    public static final VoxelShape THREE_SIDES_A_RIGHT_E = Shapes.or(
            Block.box(13, 7, 0, 15, 9, 8),
            Block.box(11, 0, 5, 15, 16, 6),
            Block.box(10, 0, 6, 16, 16, 10),
            Block.box(11, 0, 10, 15, 16, 11));
    public static final VoxelShape THREE_SIDES_A_RIGHT_W = Shapes.or(
            Block.box(1, 7, 8, 3, 9, 16),
            Block.box(1, 0, 5, 5, 16, 6),
            Block.box(0, 0, 6, 6, 16, 10),
            Block.box(1, 0, 10, 5, 16, 11));

    public static final VoxelShape THREE_SIDES_A_TOP_LEFT_N = Shapes.or(
            Block.box(8, 7, 1, 16, 9, 3),
            Block.box(5, 0, 1, 6, 12, 5),
            Block.box(6, 0, 0, 10, 12, 6),
            Block.box(10, 0, 1, 11, 12, 5));
    public static final VoxelShape THREE_SIDES_A_TOP_LEFT_S = Shapes.or(
            Block.box(0, 7, 13, 8, 9, 15),
            Block.box(5, 0, 11, 6, 12, 15),
            Block.box(6, 0, 10, 10, 12, 16),
            Block.box(10, 0, 11, 11, 12, 15));
    public static final VoxelShape THREE_SIDES_A_TOP_LEFT_E = Shapes.or(
            Block.box(13, 7, 8, 15, 9, 16),
            Block.box(11, 0, 5, 15, 12, 6),
            Block.box(10, 0, 6, 16, 12, 10),
            Block.box(11, 0, 10, 15, 12, 11));
    public static final VoxelShape THREE_SIDES_A_TOP_LEFT_W = Shapes.or(
            Block.box(1, 7, 0, 3, 9, 8),
            Block.box(1, 0, 5, 5, 12, 6),
            Block.box(0, 0, 6, 6, 12, 10),
            Block.box(1, 0, 10, 5, 12, 11));

    public static final VoxelShape THREE_SIDES_A_TOP_RIGHT_N = Shapes.or(
            Block.box(0, 7, 1, 8, 9, 3),
            Block.box(5, 0, 1, 6, 12, 5),
            Block.box(6, 0, 0, 10, 12, 6),
            Block.box(10, 0, 1, 11, 12, 5));
    public static final VoxelShape THREE_SIDES_A_TOP_RIGHT_S = Shapes.or(
            Block.box(8, 7, 13, 16, 9, 15),
            Block.box(5, 0, 11, 6, 12, 15),
            Block.box(6, 0, 10, 10, 12, 16),
            Block.box(10, 0, 11, 11, 12, 15));
    public static final VoxelShape THREE_SIDES_A_TOP_RIGHT_E = Shapes.or(
            Block.box(13, 7, 0, 15, 9, 8),
            Block.box(11, 0, 5, 15, 12, 6),
            Block.box(10, 0, 6, 16, 12, 10),
            Block.box(11, 0, 10, 15, 12, 11));
    public static final VoxelShape THREE_SIDES_A_TOP_RIGHT_W = Shapes.or(
            Block.box(1, 7, 8, 3, 9, 16),
            Block.box(1, 0, 5, 5, 12, 6),
            Block.box(0, 0, 6, 6, 12, 10),
            Block.box(1, 0, 10, 5, 12, 11));

    public static final VoxelShape FOUR_SIDES_A_N = Shapes.or(
            Block.box(0, 7, 1, 16, 9, 3),
            Block.box(5, 0, 1, 6, 16, 5),
            Block.box(6, 0, 0, 10, 16, 6),
            Block.box(10, 0, 1, 11, 16, 5));
    public static final VoxelShape FOUR_SIDES_A_S = Shapes.or(
            Block.box(0, 7, 13, 16, 9, 15),
            Block.box(5, 0, 11, 6, 16, 15),
            Block.box(6, 0, 10, 10, 16, 16),
            Block.box(10, 0, 11, 11, 16, 15));
    public static final VoxelShape FOUR_SIDES_A_E = Shapes.or(
            Block.box(13, 7, 0, 15, 9, 16),
            Block.box(11, 0, 5, 15, 16, 6),
            Block.box(10, 0, 6, 16, 16, 10),
            Block.box(11, 0, 10, 15, 16, 11));
    public static final VoxelShape FOUR_SIDES_A_W = Shapes.or(
            Block.box(1, 7, 0, 3, 9, 16),
            Block.box(1, 0, 5, 5, 16, 6),
            Block.box(0, 0, 6, 6, 16, 10),
            Block.box(1, 0, 10, 5, 16, 11));

    public static final VoxelShape FOUR_SIDES_A_TOP_N = Shapes.or(
            Block.box(0, 7, 1, 16, 9, 3),
            Block.box(5, 0, 1, 6, 12, 5),
            Block.box(6, 0, 0, 10, 12, 6),
            Block.box(10, 0, 1, 11, 12, 5));
    public static final VoxelShape FOUR_SIDES_A_TOP_S = Shapes.or(
            Block.box(0, 7, 13, 16, 9, 15),
            Block.box(5, 0, 11, 6, 12, 15),
            Block.box(6, 0, 10, 10, 12, 16),
            Block.box(10, 0, 11, 11, 12, 15));
    public static final VoxelShape FOUR_SIDES_A_TOP_E = Shapes.or(
            Block.box(13, 7, 0, 15, 9, 16),
            Block.box(11, 0, 5, 15, 12, 6),
            Block.box(10, 0, 6, 16, 12, 10),
            Block.box(11, 0, 10, 15, 12, 11));
    public static final VoxelShape FOUR_SIDES_A_TOP_W = Shapes.or(
            Block.box(1, 7, 0, 3, 9, 16),
            Block.box(1, 0, 5, 5, 12, 6),
            Block.box(0, 0, 6, 6, 12, 10),
            Block.box(1, 0, 10, 5, 12, 11));

    public static final VoxelShape FOUR_SIDES_D_N = Shapes.or(
            Block.box(5, 0, 1, 6, 16, 5),
            Block.box(10, 0, 1, 11, 16, 5),
            Block.box(6, 0, 0, 10, 16, 6),
            Block.box(0, 7, 3, 16, 9, 4),
            Block.box(0, 6, 1, 16, 10, 3),
            Block.box(0, 7, 0, 16, 9, 1));
    public static final VoxelShape FOUR_SIDES_D_S = Shapes.or(
            Block.box(10, 0, 11, 11, 16, 15),
            Block.box(5, 0, 11, 6, 16, 15),
            Block.box(6, 0, 10, 10, 16, 16),
            Block.box(0, 7, 12, 16, 9, 13),
            Block.box(0, 6, 13, 16, 10, 15),
            Block.box(0, 7, 15, 16, 9, 16));
    public static final VoxelShape FOUR_SIDES_D_E = Shapes.or(
            Block.box(11, 0, 5, 15, 16, 6),
            Block.box(11, 0, 10, 15, 16, 11),
            Block.box(10, 0, 6, 16, 16, 10),
            Block.box(12, 7, 0, 13, 9, 16),
            Block.box(13, 6, 0, 15, 10, 16),
            Block.box(15, 7, 0, 16, 9, 16));
    public static final VoxelShape FOUR_SIDES_D_W = Shapes.or(
            Block.box(1, 0, 10, 5, 16, 11),
            Block.box(1, 0, 5, 5, 16, 6),
            Block.box(0, 0, 6, 6, 16, 10),
            Block.box(3, 7, 0, 4, 9, 16),
            Block.box(1, 6, 0, 3, 10, 16),
            Block.box(0, 7, 0, 1, 9, 16));

    public static final VoxelShape FOUR_SIDES_D_LEFT_N = Shapes.or(
            Block.box(5, 0, 1, 6, 16, 5),
            Block.box(10, 0, 1, 11, 16, 5),
            Block.box(6, 0, 0, 10, 16, 6),
            Block.box(11, 7, 3, 16, 9, 4),
            Block.box(11, 6, 1, 16, 10, 3),
            Block.box(10, 7, 0, 16, 9, 1));
    public static final VoxelShape FOUR_SIDES_D_LEFT_S = Shapes.or(
            Block.box(10, 0, 11, 11, 16, 15),
            Block.box(5, 0, 11, 6, 16, 15),
            Block.box(6, 0, 10, 10, 16, 16),
            Block.box(0, 7, 12, 5, 9, 13),
            Block.box(0, 6, 13, 5, 10, 15),
            Block.box(0, 7, 15, 6, 9, 16));
    public static final VoxelShape FOUR_SIDES_D_LEFT_E = Shapes.or(
            Block.box(11, 0, 5, 15, 16, 6),
            Block.box(11, 0, 10, 15, 16, 11),
            Block.box(10, 0, 6, 16, 16, 10),
            Block.box(12, 7, 11, 13, 9, 16),
            Block.box(13, 6, 11, 15, 10, 16),
            Block.box(15, 7, 10, 16, 9, 16));
    public static final VoxelShape FOUR_SIDES_D_LEFT_W = Shapes.or(
            Block.box(1, 0, 10, 5, 16, 11),
            Block.box(1, 0, 5, 5, 16, 6),
            Block.box(0, 0, 6, 6, 16, 10),
            Block.box(3, 7, 0, 4, 9, 5),
            Block.box(1, 6, 0, 3, 10, 5),
            Block.box(0, 7, 0, 1, 9, 6));

    public static final VoxelShape FOUR_SIDES_D_RIGHT_N = Shapes.or(
            Block.box(5, 0, 1, 6, 16, 5),
            Block.box(10, 0, 1, 11, 16, 5),
            Block.box(6, 0, 0, 10, 16, 6),
            Block.box(0, 7, 3, 5, 9, 4),
            Block.box(0, 6, 1, 5, 10, 3),
            Block.box(0, 7, 0, 6, 9, 1));
    public static final VoxelShape FOUR_SIDES_D_RIGHT_S = Shapes.or(
            Block.box(10, 0, 11, 11, 16, 15),
            Block.box(5, 0, 11, 6, 16, 15),
            Block.box(6, 0, 10, 10, 16, 16),
            Block.box(11, 7, 12, 16, 9, 13),
            Block.box(11, 6, 13, 16, 10, 15),
            Block.box(10, 7, 15, 16, 9, 16));
    public static final VoxelShape FOUR_SIDES_D_RIGHT_E = Shapes.or(
            Block.box(11, 0, 5, 15, 16, 6),
            Block.box(11, 0, 10, 15, 16, 11),
            Block.box(10, 0, 6, 16, 16, 10),
            Block.box(12, 7, 0, 13, 9, 5),
            Block.box(13, 6, 0, 15, 10, 5),
            Block.box(15, 7, 0, 16, 9, 6));
    public static final VoxelShape FOUR_SIDES_D_RIGHT_W = Shapes.or(
            Block.box(1, 0, 10, 5, 16, 11),
            Block.box(1, 0, 5, 5, 16, 6),
            Block.box(0, 0, 6, 6, 16, 10),
            Block.box(3, 7, 11, 4, 9, 16),
            Block.box(1, 6, 11, 3, 10, 16),
            Block.box(0, 7, 10, 1, 9, 16));

    /**
     * @param type     立柱类型
     * @param settings 方块设置
     */
    public PillarBlock(PillarType type, Properties settings) {
        super(settings);
        this.type = type;
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Direction horizontalFacing = ctx.getHorizontalDirection().getOpposite();
        if (horizontalFacing.getAxis().isHorizontal()) {
            return this.defaultBlockState().setValue(FACING, horizontalFacing);
        }
        return this.defaultBlockState();
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
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }
}
