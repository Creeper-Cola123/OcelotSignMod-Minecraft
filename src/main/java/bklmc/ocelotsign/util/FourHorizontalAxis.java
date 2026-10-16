package bklmc.ocelotsign.util;

import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.Direction;

/**
 * 四个水平轴方向枚举，用于道路标线方块。
 */
public enum FourHorizontalAxis implements StringIdentifiable {
    X, NW_SE, Z, NE_SW;

    public static final FourHorizontalAxis[] VALUES = values();
    private final String name = name().toLowerCase();

    /** 根据 Minecraft 方向解析水平轴 */
    public static FourHorizontalAxis fromDirection(Direction direction) {
        return of(direction.getAxis());
    }

    /** 根据 Minecraft 轴解析水平轴 */
    public static FourHorizontalAxis of(Direction.Axis axis) {
        return switch (axis) {
            case X -> X;
            case Z -> Z;
            default -> throw new IllegalArgumentException();
        };
    }

    /** 应用旋转变换 */
    public FourHorizontalAxis rotate(BlockRotation rotation) {
        return switch (rotation) {
            case CLOCKWISE_90 -> VALUES[(ordinal() + 2) % 4];
            case COUNTERCLOCKWISE_90 -> VALUES[(ordinal() - 2 + 4) % 4];
            case CLOCKWISE_180 -> VALUES[(ordinal() + 4) % 4];
            default -> this;
        };
    }

    /** 应用镜像变换，交换对角线轴 */
    public FourHorizontalAxis mirror() {
        return switch (this) {
            case NW_SE -> NE_SW;
            case NE_SW -> NW_SE;
            default -> this;
        };
    }

    /** 应用镜像变换 */
    public FourHorizontalAxis mirror(BlockMirror mirror) {
        return mirror();
    }

    @Override
    public String asString() {
        return name;
    }
}
