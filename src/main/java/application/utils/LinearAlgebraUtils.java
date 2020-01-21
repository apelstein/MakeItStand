package application.utils;

import application.Axis;
import application.DoubleVoxel;
import application.Voxel;

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
                calcVoxelsByAxis(voxelsFromXyzFile, Axis.X),
                calcVoxelsByAxis(voxelsFromXyzFile, Axis.Y),
                calcVoxelsByAxis(voxelsFromXyzFile, Axis.Z));
    }

    private double calcVoxelsByAxis(List<Voxel> voxelsFromXyzFile, Axis axis) {
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

    public double calcDistanceFromPlane(DoubleVoxel doubleVoxel, Voxel point) {
        double distance = Math.sqrt(Math.pow(doubleVoxel.getX(), 2) + Math.pow(doubleVoxel.getY(), 2) + Math.pow(doubleVoxel.getZ(), 2));
        double distanceFromPoint = (
                doubleVoxel.getX() * point.getX() +
                        doubleVoxel.getY() * point.getY() +
                        doubleVoxel.getZ() * point.getZ() +
                        doubleVoxel.getAlpha()
        );
        return distanceFromPoint / distance;
    }

    public DoubleVoxel calculateCuttingPlane(DoubleVoxel initialCenterOfMass, DoubleVoxel balancePoint) {
        DoubleVoxel doubleVoxel = new DoubleVoxel(
                initialCenterOfMass.getX() - balancePoint.getX(),
                initialCenterOfMass.getY() - balancePoint.getY(),
                0,
                0
        );
        doubleVoxel.setAlpha((doubleVoxel.getX() * balancePoint.getX() + doubleVoxel.getY() * balancePoint.getY()) * -1);
        return doubleVoxel;
    }
}
