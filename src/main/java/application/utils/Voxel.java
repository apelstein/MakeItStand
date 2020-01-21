package application.utils;

import lombok.Data;

import java.util.Map;

@Data
public class Voxel {
    private int x;
    private int y;
    private int z;
    private int alpha;

    public Voxel() {
    }

    public Voxel(int[] x) {
        this(x[0], x[1], x[2]);
    }

    public Voxel(Voxel o) {
        if (o == null) {
            return;
        }
        this.x = o.getX();
        this.y = o.getY();
        this.z = o.getZ();
        this.alpha = o.getAlpha();
    }

    public Voxel(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.alpha = 1;
    }

    public Voxel(int x, int y, int z, int alpha) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.alpha = alpha;
    }

    public Voxel(Map<Axis, Integer> map) {
        map.forEach((k, v) -> {
            switch (k) {
                case X:
                    this.x = v;
                    break;
                case Y:
                    this.y = v;
                    break;
                case Z:
                    this.z = v;
                    break;
            }
        });
        alpha = 1;
    }

    public int get(Axis axis) {
        switch (axis) {
            case X:
                return x;
            case Y:
                return y;
            case Z:
                return z;
            default:
                return -1;
        }
    }

    public void set(Axis axis, int value) {
        switch (axis) {
            case X:
                setX(value);
                break;
            case Y:
                setY(value);
                break;
            case Z:
                setZ(value);
                break;
            default:
                break;
        }
    }

    @Override
    public String toString() {
        return this.x + " " + this.y + " " + this.z;
    }

}
