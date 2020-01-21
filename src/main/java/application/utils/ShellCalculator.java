package application.utils;

import java.util.*;

public class ShellCalculator {

    private final LinearAlgebraUtils linearAlgebraUtils;

    public ShellCalculator(LinearAlgebraUtils linearAlgebraUtils) {
        this.linearAlgebraUtils = linearAlgebraUtils;
    }

    public List<Voxel> calcShellFromVoxels(List<Voxel> voxels) {
        System.out.println("Starting to calculate shell");
        Set<Voxel> shell = new HashSet<>();
        for (Axis axis : Axis.values()) {
            // sort the array by the relevant axis (i.e. X,Y,Z)
            voxels.sort(Comparator.comparingInt((Voxel v) -> v.get(axis)));
            List<Axis> arrayOfAxes = Axis.get2OtherAxes(axis);
            for (int axis1 = linearAlgebraUtils.getMinVoxelByAxis(voxels, arrayOfAxes.get(0)); axis1 <= linearAlgebraUtils.getMaxVoxelByAxis(voxels, arrayOfAxes.get(0)); axis1++) {
                for (int axis2 = linearAlgebraUtils.getMinVoxelByAxis(voxels, arrayOfAxes.get(1)); axis2 <= linearAlgebraUtils.getMaxVoxelByAxis(voxels, arrayOfAxes.get(1)); axis2++) {
                    shell.addAll(calcEdgesByAxis(voxels, axis, axis1, arrayOfAxes.get(0), axis2, arrayOfAxes.get(1)));
                }
            }
        }
        System.out.println("Finished calculating shell");
        return new ArrayList<>(shell);

    }

    public Set<Voxel> calcEdgesByAxis(List<Voxel> voxels, Axis edgeType, int axis1, Axis axisType1, int axis2, Axis axisType2) {
        int last = -1000;
        Set<Voxel> shell = new HashSet<>();
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
                shell.add(new Voxel(initMap));
            } else if (last + 1 < v.get(edgeType)) {
                initMap.put(edgeType, v.get(edgeType));
                shell.add(new Voxel(initMap));
                initMap.put(edgeType, last);
                shell.add(new Voxel(initMap));
            }
            last = v.get(edgeType);
        }

        if (last != -1000) {
            initMap.put(edgeType, last);
            shell.add(new Voxel(initMap));
        }
        return shell;
    }

}
