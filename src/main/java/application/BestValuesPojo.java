package application;

import java.util.List;

import lombok.Data;

@Data
public class BestValuesPojo {

    private final List<Voxel> bestAlpha;
    private final Voxel bestCenterOfMass;

    public BestValuesPojo(List<Voxel> bestAlpha, Voxel bestCenterOfMass) {
        this.bestAlpha = bestAlpha;
        this.bestCenterOfMass = bestCenterOfMass;
    }
}
