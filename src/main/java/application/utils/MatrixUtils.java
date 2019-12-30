package application.utils;

import application.Axis;
import application.BestValuesPojo;
import application.Voxel;

import java.util.*;

public class MatrixUtils {

    public int getMax(List<Voxel> voxels, Axis axis) {
        return Collections.max(voxels, Comparator.comparingInt((Voxel v) -> v.get(axis))).get(axis);
    }

    public int getMin(List<Voxel> voxels, Axis axis) {
        return Collections.min(voxels, Comparator.comparingInt((Voxel v) -> v.get(axis))).get(axis);
    }

    public int maxX(List<Voxel> voxels) {
        return Collections.max(voxels, Comparator.comparingInt(Voxel::getX)).getX();
    }

    public int minX(List<Voxel> voxels) {
        return Collections.min(voxels, Comparator.comparingInt(Voxel::getX)).getX();
    }

    public int maxY(List<Voxel> voxels) {
        return Collections.max(voxels, Comparator.comparingInt(Voxel::getY)).getY();
    }

    public int minY(List<Voxel> voxels) {
        return Collections.min(voxels, Comparator.comparingInt(Voxel::getY)).getY();
    }

    public int maxZ(List<Voxel> voxels) {
        return Collections.max(voxels, Comparator.comparingInt(Voxel::getZ)).getZ();
    }

    public int minZ(List<Voxel> voxels) {
        return Collections.min(voxels, Comparator.comparingInt(Voxel::getZ)).getZ();
    }

    public Set<Voxel> calcShellZAxis(List<Voxel> voxels, int x, int y) {
        int lastZ = -1000;
        Set<Voxel> zEdges = new HashSet<>();
        ArrayList<Voxel> filteredVoxels = new ArrayList<>();
        voxels.forEach(v -> {
            if (v.getX() == x && v.getY() == y) {
                filteredVoxels.add(v);
            }
        });
        for (Voxel v : filteredVoxels) {
            if (lastZ == -1000) {
                zEdges.add(new Voxel(x, y, v.getZ()));
            } else if (lastZ + 1 < v.getZ()) {
                zEdges.add(new Voxel(x, y, v.getZ()));
                zEdges.add(new Voxel(x, y, lastZ));
            }
            lastZ = v.getZ();
        }

        if (lastZ != -1000) {
            zEdges.add(new Voxel(x, y, lastZ));
        }
        return zEdges;

    }

    public Set<Voxel> calcShellYAxis(List<Voxel> voxels, int x, int z) {
        int lastY = -1000;
        Set<Voxel> yEdges = new HashSet<>();
        ArrayList<Voxel> filteredVoxels = new ArrayList<>();
        voxels.forEach(v -> {
            if (v.getX() == x && v.getZ() == z) {
                filteredVoxels.add(v);
            }
        });
        for (Voxel v : filteredVoxels) {
            if (lastY == -1000) {
                yEdges.add(new Voxel(x, v.getY(), z));
            } else if (lastY + 1 < v.getY()) {
                yEdges.add(new Voxel(x, v.getY(), z));
                yEdges.add(new Voxel(x, lastY, z));
            }
            lastY = v.getY();
        }

        if (lastY != -1000) {
            yEdges.add(new Voxel(x, lastY, z));
        }
        return yEdges;

    }

    public Set<Voxel> calcShellXAxis(List<Voxel> voxels, int y, int z) {
        int lastX = -1000;
        Set<Voxel> xEdges = new HashSet<>();
        ArrayList<Voxel> filteredVoxels = new ArrayList<>();
        voxels.forEach(v -> {
            if (v.getY() == y && v.getZ() == z) {
                filteredVoxels.add(v);
            }
        });
        for (Voxel v : filteredVoxels) {
            if (lastX == -1000) {
                xEdges.add(new Voxel(v.getX(), y, z));
            } else if (lastX + 1 < v.getX()) {
                xEdges.add(new Voxel(v.getX(), y, z));
                xEdges.add(new Voxel(lastX, y, z));
            }
            lastX = v.getX();
        }

        if (lastX != -1000) {
            xEdges.add(new Voxel(lastX, y, z));
        }
        return xEdges;

    }

    public Set<Voxel> edges(List<Voxel> voxels, Axis edgeType, int axis1, Axis axisType1, int axis2, Axis axisType2) {
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
            } else if (last + 1 < v.getX()) {
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


    public Set<Voxel> calcShellFromVoxels(List<Voxel> voxels) {
        Set<Voxel> shell = new HashSet<>();
        for (Axis axis : Axis.values()) {
            // sort the array by the relevant axis (i.e. X,Y,Z)
            voxels.sort(Comparator.comparingInt((Voxel v) -> v.get(axis)));
            List<Axis> arrayOfAxes = Axis.get2OtherAxes(axis);
            for (int axe1 = getMin(voxels, arrayOfAxes.get(0)); axe1 <= getMax(voxels, arrayOfAxes.get(0)); axe1++) {
                for (int axe2 = getMin(voxels, arrayOfAxes.get(1)); axe2 <= getMax(voxels, arrayOfAxes.get(1)); axe2++) {
                    shell.addAll(edges(voxels, axis, axe1, arrayOfAxes.get(0), axe2, arrayOfAxes.get(1)));
                }
            }
            System.out.println("added all " + axis + " edges");
        }
        return shell;

    }

    public Voxel calcBalancePoint(List<Voxel> voxelsFromXyzFile) {
        return null;
    }

    public Voxel calcCenterOfMass(List<Voxel> voxelsFromXyzFile) {
        return null;
    }

    public BestValuesPojo calcBestValues(Voxel balancePoint, Voxel initialCenterOfMass, Set<Voxel> shell, List<Voxel> voxelsFromXyzFile) {
        return null;
    }
}
