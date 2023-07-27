/*
 * MIT License
 *
 * Copyright (c) 2017 Benjamin K
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.tridevmc.architecture.legacy.math;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;

import java.util.function.DoubleUnaryOperator;

import static java.lang.Math.abs;

@Deprecated
public record LegacyVector3(double x, double y, double z) {

    public static final LegacyVector3 ZERO = new LegacyVector3(0, 0, 0);

    public static final LegacyVector3 ONE = new LegacyVector3(1, 1, 1);
    public static final LegacyVector3 BLOCK_CENTER = new LegacyVector3(0.5, 0.5, 0.5);
    public static final LegacyVector3 INV_BLOCK_CENTER = BLOCK_CENTER.mul(-1);

    public static final LegacyVector3 UNIT_X = new LegacyVector3(1, 0, 0);
    public static final LegacyVector3 UNIT_Y = new LegacyVector3(0, 1, 0);
    public static final LegacyVector3 UNIT_Z = new LegacyVector3(0, 0, 1);

    public static final LegacyVector3 UNIT_NX = new LegacyVector3(-1, 0, 0);
    public static final LegacyVector3 UNIT_NY = new LegacyVector3(0, -1, 0);
    public static final LegacyVector3 UNIT_NZ = new LegacyVector3(0, 0, -1);

    public static final LegacyVector3 UNIT_PYNZ = new LegacyVector3(0, 0.707, -0.707);
    public static final LegacyVector3 UNIT_PXPY = new LegacyVector3(0.707, 0.707, 0);
    public static final LegacyVector3 UNIT_PYPZ = new LegacyVector3(0, 0.707, 0.707);
    public static final LegacyVector3 UNIT_NXPY = new LegacyVector3(-0.707, 0.707, 0);
    public static final LegacyVector3[][] FACE_BASES = {
            {UNIT_X, UNIT_Z}, // DOWN
            {UNIT_X, UNIT_NZ}, // UP
            {UNIT_NX, UNIT_Y}, // NORTH
            {UNIT_X, UNIT_Y}, // SOUTH
            {UNIT_Z, UNIT_Y}, // WEST
            {UNIT_NZ, UNIT_Y}, // EAST
    };
    public static final Vec3i[] DIRECTION_VEC = {
            new Vec3i(0, -1, 0),
            new Vec3i(0, 1, 0),
            new Vec3i(0, 0, -1),
            new Vec3i(0, 0, 1),
            new Vec3i(-1, 0, 0),
            new Vec3i(1, 0, 0)
    };

    public LegacyVector3(Vector3d v) {
        this(v.x, v.y, v.z);
    }

    public LegacyVector3(Vec3i v) {
        this(v.getX(), v.getY(), v.getZ());
    }

    public LegacyVector3(Direction f) {
        this(getDirectionVec(f));
    }

    public LegacyVector3(LegacyVector3 v) {
        this(v.x, v.y, v.z);
    }

    public LegacyVector3(Vec3 v) {
        this(v.x, v.y, v.z);
    }

    public static LegacyVector3 blockCenter(double x, double y, double z) {
        return BLOCK_CENTER.add(x, y, z);
    }

    public static LegacyVector3 blockCenter(BlockPos pos) {
        return BLOCK_CENTER.add(pos);
    }

    public static LegacyVector3 sub(double[] u, double[] v) {
        return new LegacyVector3(u[0] - v[0], u[1] - v[1], u[2] - v[2]);
    }

    public static LegacyVector3 unit(LegacyVector3 v) {
        return v.mul(1 / v.length());
    }

    public static LegacyVector3 average(LegacyVector3... va) {
        double x = 0, y = 0, z = 0;
        for (LegacyVector3 v : va) {
            x += v.x;
            y += v.y;
            z += v.z;
        }
        int n = va.length;
        return new LegacyVector3(x / n, y / n, z / n);
    }

    public static LegacyVector3 average(double[]... va) {
        double x = 0, y = 0, z = 0;
        for (double[] v : va) {
            x += v[0];
            y += v[1];
            z += v[2];
        }
        int n = va.length;
        return new LegacyVector3(x / n, y / n, z / n);
    }

    public static Direction facing(double dx, double dy, double dz) {
        double ax = abs(dx), ay = abs(dy), az = abs(dz);
        if (ay >= ax && ay >= az)
            return dy < 0 ? Direction.DOWN : Direction.UP;
        else if (ax >= az)
            return dx < 0 ? Direction.WEST : Direction.EAST;
        else
            return dz < 0 ? Direction.NORTH : Direction.SOUTH;
    }

    public static LegacyVector3[] faceBasis(Direction f) {
        return FACE_BASES[f.ordinal()];
    }

    public static Vec3i getDirectionVec(Direction f) {
        return DIRECTION_VEC[f.ordinal()];
    }

    public Vec3i toVec3i() {
        return new Vec3i((int) this.x, (int) this.y, (int) this.z);
    }

    public Vec3 toMCVec3() {
        return new Vec3(this.x, this.y, this.z);
    }

    public LegacyVector3 add(double x, double y, double z) {
        return new LegacyVector3(this.x + x, this.y + y, this.z + z);
    }

    public LegacyVector3 add(LegacyVector3 v) {
        return this.add(v.x, v.y, v.z);
    }

    public LegacyVector3 add(BlockPos pos) {
        return this.add(pos.getX(), pos.getY(), pos.getZ());
    }

    public LegacyVector3 sub(double x, double y, double z) {
        return new LegacyVector3(this.x - x, this.y - y, this.z - z);
    }

    public LegacyVector3 sub(LegacyVector3 v) {
        return this.sub(v.x, v.y, v.z);
    }

    public LegacyVector3 mul(double c) {
        return new LegacyVector3(c * this.x, c * this.y, c * this.z);
    }

    public LegacyVector3 div(double c) {
        return new LegacyVector3(this.x / c, this.y / c, this.z / c);
    }

    public double dot(LegacyVector3 v) {
        return this.dot(v.x, v.y, v.z);
    }

    public double dot(double[] v) {
        return this.dot(v[0], v[1], v[2]);
    }

    public double dot(Direction f) {
        Vec3i v = getDirectionVec(f);
        return this.dot(v.getX(), v.getY(), v.getZ());
    }

    public double dot(double vx, double vy, double vz) {
        return this.x * vx + this.y * vy + this.z * vz;
    }

    public LegacyVector3 cross(LegacyVector3 v) {
        return new LegacyVector3(
                this.y * v.z - this.z * v.y,
                this.z * v.x - this.x * v.z,
                this.x * v.y - this.y * v.x);
    }

    /**
     * Creates a new Vector3 with the minimum components of the two vectors.
     *
     * @param x The x component of the vector to compare.
     * @param y The y component of the vector to compare.
     * @param z The z component of the vector to compare.
     * @return The new Vector3 with the minimum components.
     */
    public LegacyVector3 min(double x, double y, double z) {
        return new LegacyVector3(Math.min(this.x, x), Math.min(this.y, y), Math.min(this.z, z));
    }

    /**
     * Creates a new Vector3 with the minimum components of the two vectors.
     *
     * @param v The other vector to compare with.
     * @return The new vector with the minimum components.
     */
    public LegacyVector3 min(LegacyVector3 v) {
        return this.min(v.x, v.y, v.z);
    }

    /**
     * Creates a new Vector3 with the maximum components of the two vectors.
     *
     * @param x The x component of the vector to compare.
     * @param y The y component of the vector to compare.
     * @param z The z component of the vector to compare.
     * @return The new Vector3 with the maximum components.
     */
    public LegacyVector3 max(double x, double y, double z) {
        return new LegacyVector3(Math.max(this.x, x), Math.max(this.y, y), Math.max(this.z, z));
    }

    /**
     * Creates a new Vector3 with the maximum components of the two vectors.
     *
     * @param v The other vector to compare with.
     * @return The new vector with the maximum components.
     */
    public LegacyVector3 max(LegacyVector3 v) {
        return this.max(v.x, v.y, v.z);
    }

    public LegacyVector3 normalize() {
        var l = this.length();
        return l < 1.0E-4D ? LegacyVector3.ZERO : new LegacyVector3(this.x / l, this.y / l, this.z / l);
    }

    public double length() {
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    public double distance(LegacyVector3 v) {
        double dx = this.x - v.x, dy = this.y - v.y, dz = this.z - v.z;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    public double getComponent(int axis) {
        return switch (axis) {
            case 0 -> this.x;
            case 1 -> this.y;
            case 2 -> this.z;
            default -> throw new IllegalArgumentException("Axis must be between 0 and 2");
        };
    }

    public int floorX() {
        return (int) Math.floor(this.x);
    }

    public int floorY() {
        return (int) Math.floor(this.y);
    }

    public int floorZ() {
        return (int) Math.floor(this.z);
    }

    // Normals at 45 degrees are biased towards UP or DOWN.
    // In 1.8 this is important for item lighting in inventory to work well.

    public int roundX() {
        return (int) Math.round(this.x);
    }

    public int roundY() {
        return (int) Math.round(this.y);
    }

    public int roundZ() {
        return (int) Math.round(this.z);
    }

    /**
     * Calculates the slope of x in terms of y.
     */
    public double getXYSlope(LegacyVector3 other) {
        return (this.y - other.y) / (this.x - other.x);
    }

    public double getXZSlope(LegacyVector3 other) {
        return (this.z - other.z) / (this.x - other.x);
    }

    /**
     * Calculates the slope of z in terms of y.
     */
    public double getZYSlope(LegacyVector3 other) {
        return (this.y - other.y) / (this.z - other.z);
    }

    public double getZXSlope(LegacyVector3 other) {
        return (this.x - other.x) / (this.z - other.z);
    }

    /**
     * Calculates the slope of y in terms of x.
     */
    public double getYZSlope(LegacyVector3 other) {
        return (this.x - other.x) / (this.y - other.y);
    }

    public double getYXSlope(LegacyVector3 other) {
        return (this.z - other.z) / (this.y - other.y);
    }

    // Workaround for EnumFacing.getDirectionVec being client-side only

    public Direction facing() {
        return facing(this.x, this.y, this.z);
    }

    public BlockPos blockPos() {
        return new BlockPos(this.floorX(), this.floorY(), this.floorZ());
    }

    public LegacyVector3 apply(DoubleUnaryOperator operator) {
        return new LegacyVector3(operator.applyAsDouble(this.x),
                operator.applyAsDouble(this.y),
                operator.applyAsDouble(this.z));
    }

    public double[] toArray() {
        return new double[]{this.x, this.y, this.z};
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof LegacyVector3 v) {
            return Double.compare(v.x, this.x) == 0 &&
                    Double.compare(v.y, this.y) == 0 &&
                    Double.compare(v.z, this.z) == 0;
        }
        return false;
    }

    @Override
    public int hashCode() {
        var out = 1;
        out = 30 * out + Double.hashCode(this.x);
        out = 30 * out + Double.hashCode(this.y);
        out = 30 * out + Double.hashCode(this.z);
        return out;
    }

}
