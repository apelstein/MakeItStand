package application;

import lombok.Data;

import java.util.List;

@Data
public class BestValuesPojo {

    private final List<Voxel> bestAlpha;
    private final DoubleVoxel bestCenterOfMass;

}
