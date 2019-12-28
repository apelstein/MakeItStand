package application;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Data
public class Voxel {
    private int x;
    private int y;
    private int z;
    private int alpha;

    public Voxel(int x[]){
        this(x[0],x[1],x[2]);
    }

    public Voxel(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.alpha = 1;
    }

    public Voxel(Map<Axe, Integer> map){
        map.forEach((k, v)->{
            switch (k){
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
    }

    public int get(Axe axe){
        switch (axe){
            case X:
                return this.x;
            case Y:
                return this.y;
            case Z:
                return this.z;
        }
        return -1;
    }
}
