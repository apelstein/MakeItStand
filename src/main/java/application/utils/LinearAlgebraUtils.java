package application.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class LinearAlgebraUtils {

    public int getMaxVoxelByAxis(List<Voxel> voxels, Axis axis) {
        return Collections.max(voxels, Comparator.comparingInt((Voxel v) -> v.get(axis))).get(axis);
    }

    public int getMinVoxelByAxis(List<Voxel> voxels, Axis axis) {
        return Collections.min(voxels, Comparator.comparingInt((Voxel v) -> v.get(axis))).get(axis);
    }

    public DoubleVoxel calcBalancePoint(List<Voxel> voxelsFromXyzFile) {
        int minimumZValue = getMinVoxelByAxis(voxelsFromXyzFile, Axis.Z);
        List<Voxel> allMinZVoxels = voxelsFromXyzFile.stream().filter(voxel -> voxel.getZ() == minimumZValue).collect(Collectors
                .toList());
        return calcCenterOfMass(allMinZVoxels);
    }

    public DoubleVoxel calcCenterOfMass(List<Voxel> voxelsFromXyzFile) {
        return new DoubleVoxel(
                calcAverage(voxelsFromXyzFile, Axis.X),
                calcAverage(voxelsFromXyzFile, Axis.Y),
                calcAverage(voxelsFromXyzFile, Axis.Z));
    }

    private double calcAverage(List<Voxel> voxelsFromXyzFile, Axis axis) {
        double sum = 0;
        double count = 0;
        for (Voxel v : voxelsFromXyzFile) {
            if (v.getAlpha() == 1) {
                sum += v.get(axis);
                count += 1;
            }
        }
        return sum / count;
    }

    public double calcNorm(DoubleVoxel voxel) {
        double sum = 0;
        for (Axis axis : Axis.values()) {
            sum += Math.pow(voxel.get(axis), 2);
        }
        return Math.sqrt(sum);
    }

    public double calcDistanceBetweenPointAndPlane(DoubleVoxel cuttingPlane, Voxel point) {
        double norm = Math.sqrt(Math.pow(cuttingPlane.getX(), 2) + Math.pow(cuttingPlane.getY(), 2) + Math.pow(cuttingPlane.getZ(), 2));
        double distanceFromPoint = (
                cuttingPlane.getX() * point.getX() +
                        cuttingPlane.getY() * point.getY() +
                        cuttingPlane.getZ() * point.getZ() +
                        cuttingPlane.getAlpha()
        );
        return distanceFromPoint / norm;
    }

    public DoubleVoxel calculateCuttingPlane(DoubleVoxel initialCenterOfMass, DoubleVoxel balancePoint) {
        DoubleVoxel planeCoefficients = new DoubleVoxel(
                initialCenterOfMass.getX() - balancePoint.getX(),
                initialCenterOfMass.getY() - balancePoint.getY(),
                0,
                0
        );
        planeCoefficients.setAlpha((planeCoefficients.getX() * balancePoint.getX() + planeCoefficients.getY() * balancePoint.getY()) * -1);
        return planeCoefficients;
    }
}
