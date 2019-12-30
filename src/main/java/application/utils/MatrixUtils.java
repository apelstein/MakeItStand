package application.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import application.Axis;
import application.BestValuesPojo;
import application.Voxel;

public class MatrixUtils {

    public int getMax(List<Voxel> voxels, Axis axis) {
        return Collections.max(voxels, Comparator.comparingInt((Voxel v) -> v.get(axis))).get(axis);
    }

    public int getMin(List<Voxel> voxels, Axis axis) {
        return Collections.min(voxels, Comparator.comparingInt((Voxel v) -> v.get(axis))).get(axis);
    }

    public Set<Voxel> calcEdgesByAxis(List<Voxel> voxels, Axis edgeType, int axis1, Axis axisType1, int axis2, Axis axisType2) {
        int last = -1000;
        Set<Voxel> edges = new HashSet<>();
        ArrayList<Voxel> filteredVoxels = new ArrayList<>();
        voxels.forEach(v -> {
            if (v.get(axisType1) == axis1 && v.get(axisType2) == axis2) {
                filteredVoxels.add(v);
            }
        });
        Map<Axis, Integer> initMap = new HashMap<>();
        initMap.put(axisType1, axis1);
        initMap.put(axisType2, axis2);
        for (Voxel v : filteredVoxels) {
            if (last == -1000) {
                initMap.put(edgeType, v.get(edgeType));
                edges.add(new Voxel(initMap));
            } else if (last + 1 < v.get(edgeType)) {
                initMap.put(edgeType, v.get(edgeType));
                edges.add(new Voxel(initMap));
                initMap.put(edgeType, last);
                edges.add(new Voxel(initMap));
            }
            last = v.get(edgeType);
        }

        if (last != -1000) {
            initMap.put(edgeType, last);
            edges.add(new Voxel(initMap));
        }
        return edges;
    }


    public List<Voxel> calcShellFromVoxels(List<Voxel> voxels) {
        Set<Voxel> shell = new HashSet<>();
        for (Axis axis : Axis.values()) {
            // sort the array by the relevant axis (i.e. X,Y,Z)
            voxels.sort(Comparator.comparingInt((Voxel v) -> v.get(axis)));
            List<Axis> arrayOfAxes = Axis.get2OtherAxes(axis);
            for (int axis1 = getMin(voxels, arrayOfAxes.get(0)); axis1 <= getMax(voxels, arrayOfAxes.get(0)); axis1++) {
                for (int axis2 = getMin(voxels, arrayOfAxes.get(1)); axis2 <= getMax(voxels, arrayOfAxes.get(1)); axis2++) {
                    shell.addAll(calcEdgesByAxis(voxels, axis, axis1, arrayOfAxes.get(0), axis2, arrayOfAxes.get(1)));
                }
            }
            System.out.println("added all " + axis + " edges");
        }
        return new ArrayList<>(shell);

    }

    public Voxel calcBalancePoint(List<Voxel> voxelsFromXyzFile) {
        return null;
    }

    public Voxel calcCenterOfMass(List<Voxel> voxelsFromXyzFile) {
        return null;
    }

    public BestValuesPojo calcBestValues(Voxel balancePoint, Voxel initialCenterOfMass, List<Voxel> shell, List<Voxel> voxelsFromXyzFile) {
        return null;
    }
}
