package application.utils;

import application.Axis;
import application.BestValuesPojo;
import application.Voxel;

import java.util.*;
import java.util.stream.Collectors;

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
        int z_min = getMin(voxelsFromXyzFile, Axis.Z);
        List<Voxel> allMinZVoxels = voxelsFromXyzFile.stream().filter(voxel -> voxel.getZ() == z_min).collect(Collectors
                .toList());
        return calcCenterOfMass(allMinZVoxels);
    }

    public Voxel calcCenterOfMass(List<Voxel> voxelsFromXyzFile) {
        return new Voxel(
                getAverage(voxelsFromXyzFile, Axis.X),
                getAverage(voxelsFromXyzFile, Axis.Y),
                getAverage(voxelsFromXyzFile, Axis.Z));
    }

    private int getAverage(List<Voxel> voxelsFromXyzFile, Axis axis) {
        int sum = 0;
        int count = 0;
        for (Voxel v : voxelsFromXyzFile) {
            if (v.getAlpha() == 1) {
                sum += v.get(axis);
                count += 1;
            }
        }
        return sum / count;
    }

    public BestValuesPojo calcBestValues(Voxel balancePoint, Voxel initialCenterOfMass, List<Voxel> shell, List<Voxel> voxelsFromXyzFile) {
        Voxel planes_coefs = calculateCuttingPlane(initialCenterOfMass, balancePoint);
        voxelsFromXyzFile.sort(Comparator.comparingDouble(p -> calcDistanceFromPlane(planes_coefs, (Voxel) p)).reversed());
        return removeVoxels(voxelsFromXyzFile, shell, balancePoint, initialCenterOfMass);
    }

    private BestValuesPojo removeVoxels(List<Voxel> voxelsSortedByDistanceFromPlane, List<Voxel> shell, Voxel balancePoint, Voxel initialCenterOfMass) {
        int counter = 0;
        Voxel best_com = new Voxel();
        Voxel currentVoxel;
        Voxel current_com = new Voxel(initialCenterOfMass);
        double min_Ecom = Double.MAX_VALUE;
        double current_Ecom;
        int last_point_index = 0;
        int xyz_updating_len = voxelsSortedByDistanceFromPlane.size();
        List<Voxel> best_alpha = new ArrayList<>(voxelsSortedByDistanceFromPlane);
        boolean flag = false;
        for (int i = 0; i < voxelsSortedByDistanceFromPlane.size(); i++) {
            currentVoxel = voxelsSortedByDistanceFromPlane.get(i);
            if (!shell.contains(currentVoxel)) {
                currentVoxel.setAlpha(0);
                xyz_updating_len--;
                updateCenterOfMass(current_com, currentVoxel, xyz_updating_len);
                current_Ecom = calcECom(current_com, balancePoint);
                if (current_Ecom < min_Ecom) {
                    last_point_index = i;
                    best_com = current_com;
                    min_Ecom = current_Ecom;
                    flag = true;
                } else if ((current_Ecom > min_Ecom) && flag) {
                    voxelsSortedByDistanceFromPlane.get(last_point_index).setAlpha(1);
                    flag = false;
                    best_alpha = voxelsSortedByDistanceFromPlane.subList(0, last_point_index + 1);
                }
                counter++;
                if (counter % 3000 == 0 && counter != 0) {
                    System.out.println("Deleted " + counter + "voxels, Ecom is: " + current_Ecom);
                }
            }
        }
        System.out.println("Final Ecom: " + min_Ecom);
        return new BestValuesPojo(best_alpha, new Voxel(best_com));
    }

    private double calcECom(Voxel current_com, Voxel balancePoint) {
        Voxel voxel = new Voxel(
                current_com.getX() - balancePoint.getX(),
                current_com.getY() - balancePoint.getY(),
                0
        );
        return calcNorm(voxel);
    }

    private double calcNorm(Voxel voxel) {
        int sum = 0;
        for (Axis axis : Axis.values()) {
            sum += voxel.get(axis) * voxel.get(axis);
        }
        return Math.sqrt(sum);
    }

    private void updateCenterOfMass(Voxel current_com, Voxel currentVoxel, int xyz_updating_len) {
        for (Axis axis : Axis.values()) {
            current_com.set(axis, current_com.get(axis) * xyz_updating_len + 1);
            current_com.set(axis, current_com.get(axis) - currentVoxel.get(axis));
            current_com.set(axis, current_com.get(axis) / xyz_updating_len);
        }
    }

    private double calcDistanceFromPlane(Voxel planes_coefs, Voxel point) {
        double length_coef = Math.sqrt(Math.pow(planes_coefs.getX(), 2) + Math.pow(planes_coefs.getY(), 2) + Math.pow(planes_coefs.getZ(), 2));
        double dot_point = (
                planes_coefs.getX() * point.getX() +
                        planes_coefs.getY() * point.getY() +
                        planes_coefs.getZ() * point.getZ() +
                        planes_coefs.getAlpha()
        );
        return dot_point / length_coef;
    }

    private Voxel calculateCuttingPlane(Voxel initialCenterOfMass, Voxel balancePoint) {
        Voxel planes_coefs = new Voxel(
                initialCenterOfMass.getX() - balancePoint.getX(),
                initialCenterOfMass.getY() - balancePoint.getY(),
                0,
                (initialCenterOfMass.getX() * balancePoint.getX() +
                        initialCenterOfMass.getX() * balancePoint.getX()) * -1
        );
        System.out.println(
                "Center of mass: " + initialCenterOfMass +
                        "\nBalance point: " + balancePoint +
                        "\nCutting Plane coefs: " + planes_coefs
        );
        return planes_coefs;
    }
}
