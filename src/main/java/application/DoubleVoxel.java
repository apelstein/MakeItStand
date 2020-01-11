package application;

import lombok.Data;

@Data
public class DoubleVoxel {
    private double x;
    private double y;
    private double z;
    private double alpha;

    public DoubleVoxel() {
    }

    public DoubleVoxel(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.alpha = 1;
    }

    public DoubleVoxel(double x, double y, double z, double alpha) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.alpha = alpha;
    }

    public DoubleVoxel(DoubleVoxel o) {
        if (o == null) {
            return;
        }
        this.x = o.getX();
        this.y = o.getY();
        this.z = o.getZ();
        this.alpha = o.getAlpha();
    }

    public double get(Axis axis) {
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

    public void set(Axis axis, double value) {
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
}
