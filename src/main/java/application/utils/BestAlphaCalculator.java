package application.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BestAlphaCalculator {

    private final LinearAlgebraUtils linearAlgebraUtils;

    public BestAlphaCalculator(LinearAlgebraUtils linearAlgebraUtils) {
        this.linearAlgebraUtils = linearAlgebraUtils;
    }

    public List<Voxel> calcBestAlpha(List<Voxel> shell, List<Voxel> voxelsFromXyzFile) {
        System.out.println("Starting to calculate best alpha values");
        DoubleVoxel balancePoint = linearAlgebraUtils.calcBalancePoint(voxelsFromXyzFile);
        DoubleVoxel initialCenterOfMass = linearAlgebraUtils.calcCenterOfMass(voxelsFromXyzFile);
        DoubleVoxel cuttingPlane = linearAlgebraUtils.calculateCuttingPlane(initialCenterOfMass, balancePoint);
        voxelsFromXyzFile.sort(
                Comparator.comparingDouble(p -> linearAlgebraUtils.calcDistanceBetweenPointAndPlane(cuttingPlane, (Voxel) p))
                        .reversed());
        return optimize(voxelsFromXyzFile, shell, balancePoint, initialCenterOfMass, cuttingPlane);
    }

    private List<Voxel> optimize(List<Voxel> voxelsSortedByDistanceFromPlane, List<Voxel> shell, DoubleVoxel balancePoint, DoubleVoxel initialCenterOfMass, DoubleVoxel cuttingPlane) {
        Voxel currentVoxel;
        DoubleVoxel currentCenterOfMass = new DoubleVoxel(initialCenterOfMass);
        double minEcom = Double.MAX_VALUE;
        double currentEcom;
        int bestAlphaIndex = 0;
        int len = voxelsSortedByDistanceFromPlane.size();
        List<Voxel> bestAlpha = new ArrayList<>();
        voxelsSortedByDistanceFromPlane.forEach(voxel -> bestAlpha.add(new Voxel(voxel)));
        for (int i = 0; i < voxelsSortedByDistanceFromPlane.size(); i++) {
            currentVoxel = voxelsSortedByDistanceFromPlane.get(i);
            if (!shell.contains(currentVoxel)) {
                if (linearAlgebraUtils.calcDistanceBetweenPointAndPlane(cuttingPlane, currentVoxel) <= 0.0) {
                    break;
                }
                currentVoxel.setAlpha(0);
                len--;
                updateCenterOfMass(currentCenterOfMass, currentVoxel, len);
                currentEcom = calcECom(currentCenterOfMass, balancePoint);
                if (currentEcom < minEcom) {
                    bestAlphaIndex = i;
                    minEcom = currentEcom;
                } else if (currentEcom > minEcom) {
                    voxelsSortedByDistanceFromPlane.get(bestAlphaIndex).setAlpha(1);
                    for (int j = 0; j < bestAlphaIndex; j++) {
                        bestAlpha.set(j, new Voxel(voxelsSortedByDistanceFromPlane.get(j)));
                    }
                }
            }
        }
        System.out.println("Finished calculating best alpha values");
        return bestAlpha;
    }

    private double calcECom(DoubleVoxel currentCenterOfMass, DoubleVoxel balancePoint) {
        DoubleVoxel voxel = new DoubleVoxel(
                currentCenterOfMass.getX() - balancePoint.getX(),
                currentCenterOfMass.getY() - balancePoint.getY(),
                currentCenterOfMass.getZ() - balancePoint.getZ()
        );
        return linearAlgebraUtils.calcNorm(voxel);
    }

    private void updateCenterOfMass(DoubleVoxel currentCenterOfMass, Voxel currentVoxel, int len) {
        for (Axis axis : Axis.values()) {
            currentCenterOfMass.set(axis, currentCenterOfMass.get(axis) * (len + 1));
            currentCenterOfMass.set(axis, currentCenterOfMass.get(axis) - currentVoxel.get(axis));
            currentCenterOfMass.set(axis, currentCenterOfMass.get(axis) / len);
        }
    }

}
