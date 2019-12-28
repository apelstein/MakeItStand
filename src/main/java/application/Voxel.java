package application;

import lombok.Data;
import lombok.ToString;

import java.util.Map;

@Data
@ToString(includeFieldNames = true)
public class Voxel {
    private int x;
    private int y;
    private int z;
    private int alpha;

    public Voxel(int[] x) {
        this(x[0], x[1], x[2]);
    }

    public Voxel(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.alpha = 1;
    }

    public Voxel(Map<Axis, Integer> map) {
        map.forEach((k, v) -> {
            switch (k) {
                case X:
                    x = v;
                    break;
                case Y:
                    y = v;
                    break;
                case Z:
                    z = v;
                    break;
            }
        });
    }

    public int get(Axis axis) {
        switch (axis) {
            case X:
                return x;
            case Y:
                return y;
            case Z:
                return z;
        }
        return -1;
    }

    public int getY() {
        return 0;
    }
}
