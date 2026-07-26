package bklmc.ocelotsign.util;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Optional;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;

/**
 * 八个水平方向枚举：四个主方向与四个对角方向。
 */
public enum EightHorizontalDirection implements StringRepresentable {
    SOUTH(Direction.SOUTH, 0, FourHorizontalAxis.Z),
    SOUTH_WEST(null, 315, FourHorizontalAxis.NE_SW),
    WEST(Direction.WEST, 270, FourHorizontalAxis.X),
    NORTH_WEST(null, 225, FourHorizontalAxis.NW_SE),
    NORTH(Direction.NORTH, 180, FourHorizontalAxis.Z),
    NORTH_EAST(null, 135, FourHorizontalAxis.NE_SW),
    EAST(Direction.EAST, 90, FourHorizontalAxis.X),
    SOUTH_EAST(null, 45, FourHorizontalAxis.NW_SE);

    public static final EightHorizontalDirection[] VALUES = values();
    private final Direction direction;
    private final float rotation;
    public final FourHorizontalAxis axis;

    /**
     * 构造一个水平方向。
     *
     * @param direction 对应的 Minecraft 方向，对角线方向为 {@code null}
     * @param rotation  旋转角度（度）
     * @param axis      所属水平轴
     */
    EightHorizontalDirection(@Nullable Direction direction, float rotation, FourHorizontalAxis axis) {
        this.direction = direction;
        this.rotation = rotation;
        this.axis = axis;
    }

    /**
     * 根据旋转角度解析方向。
     *
     * @param rotation 旋转角度（度）
     * @return 对应的水平方向
     */
    public static EightHorizontalDirection fromRotation(float rotation) {
        return VALUES[Mth.floor(rotation / 45 + 0.5) & 7];
    }

    /**
     * 根据 Minecraft 方向解析水平方向。
     *
     * @param direction Minecraft 方向
     * @return 对应的水平方向
     * @throws IllegalArgumentException 当方向不是水平方向时抛出
     */
    public static EightHorizontalDirection fromDirection(Direction direction) {
        return switch (direction) {
            case NORTH -> NORTH;
            case SOUTH -> SOUTH;
            case EAST -> EAST;
            case WEST -> WEST;
            default -> throw new IllegalArgumentException("Direction must be horizontal");
        };
    }

    /**
     * 获取对应的 Minecraft 方向。
     *
     * @return Minecraft 方向；对角线方向返回 {@code null}
     */
    public Direction asDirection() {
        return direction;
    }

    /**
     * 获取旋转角度。
     *
     * @return 旋转角度（度）
     */
    public float asRotation() {
        return rotation;
    }

    /**
     * 获取 Minecraft 方向形式。
     *
     * @return 包装后的 Minecraft 方向；对角线方向为空
     */
    public Optional<Direction> left() {
        return Optional.ofNullable(direction);
    }

    /**
     * 获取水平方向形式。
     *
     * @return 包装后的水平方向；非对角线方向返回空
     */
    public Optional<EightHorizontalDirection> right() {
        return Optional.ofNullable(direction == null ? this : null);
    }

    /**
     * 是否为对角线方向。
     *
     * @return 对角线方向返回 {@code true}
     */
    public boolean isDiagonal() {
        return direction == null;
    }

    /**
     * 获取对应的轴。
     *
     * @return Minecraft 轴；对角线方向返回 {@code null}
     */
    public Direction.Axis getAxis() {
        return direction != null ? direction.getAxis() : null;
    }

    /**
     * 获取垂直的水平轴。
     *
     * @return 垂直的 Minecraft 轴；对角线方向返回 {@code null}
     */
    public Direction.Axis getPerpendicularAxis() {
        return direction != null ? direction.getAxis() == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X : null;
    }

    /**
     * 应用旋转变换。
     *
     * @param rotation 旋转变换
     * @return 旋转后的水平方向
     */
    public EightHorizontalDirection rotate(Rotation rotation) {
        return switch (rotation) {
            case CLOCKWISE_90 -> VALUES[(ordinal() + 2) % 8];
            case COUNTERCLOCKWISE_90 -> VALUES[(ordinal() - 2 + 8) % 8];
            case CLOCKWISE_180 -> VALUES[(ordinal() + 4) % 8];
            default -> this;
        };
    }

    /**
     * 应用镜像变换。
     *
     * @param mirror 镜像变换
     * @return 镜像后的水平方向
     */
    public EightHorizontalDirection mirror(Mirror mirror) {
        return switch (mirror) {
            case LEFT_RIGHT -> switch (this) {
                case NORTH -> NORTH;
                case SOUTH -> SOUTH;
                case EAST -> WEST;
                case WEST -> EAST;
                case NORTH_EAST -> NORTH_WEST;
                case NORTH_WEST -> NORTH_EAST;
                case SOUTH_EAST -> SOUTH_WEST;
                case SOUTH_WEST -> SOUTH_EAST;
                default -> this;
            };
            case FRONT_BACK -> switch (this) {
                case NORTH -> SOUTH;
                case SOUTH -> NORTH;
                case EAST -> EAST;
                case WEST -> WEST;
                case NORTH_EAST -> SOUTH_EAST;
                case NORTH_WEST -> SOUTH_WEST;
                case SOUTH_EAST -> NORTH_EAST;
                case SOUTH_WEST -> NORTH_WEST;
                default -> this;
            };
            default -> this;
        };
    }

    /**
     * 获取方向的小写名称。
     *
     * @return 小写名称
     */
    @Override
    public String getSerializedName() {
        return name().toLowerCase();
    }
}
