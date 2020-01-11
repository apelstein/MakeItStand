package application.utils;

import application.Axis;
import application.BestValuesPojo;
import application.DoubleVoxel;
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

    public DoubleVoxel calcBalancePoint(List<Voxel> voxelsFromXyzFile) {
        int z_min = getMin(voxelsFromXyzFile, Axis.Z);
        List<Voxel> allMinZVoxels = voxelsFromXyzFile.stream().filter(voxel -> voxel.getZ() == z_min).collect(Collectors
                .toList());
        return calcCenterOfMass(allMinZVoxels);
    }

    public DoubleVoxel calcCenterOfMass(List<Voxel> voxelsFromXyzFile) {
        return new DoubleVoxel(
                getAverage(voxelsFromXyzFile, Axis.X),
                getAverage(voxelsFromXyzFile, Axis.Y),
                getAverage(voxelsFromXyzFile, Axis.Z));
    }

    private double getAverage(List<Voxel> voxelsFromXyzFile, Axis axis) {
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

    public BestValuesPojo calcBestValues(DoubleVoxel balancePoint, DoubleVoxel initialCenterOfMass, List<Voxel> shell, List<Voxel> voxelsFromXyzFile) {
        DoubleVoxel planes_coefs = calculateCuttingPlane(initialCenterOfMass, balancePoint);
        voxelsFromXyzFile.sort(Comparator.comparingDouble(p -> calcDistanceFromPlane(planes_coefs, (Voxel) p)).reversed());
        return removeVoxels(voxelsFromXyzFile, shell, balancePoint, initialCenterOfMass);
    }

    private BestValuesPojo removeVoxels(List<Voxel> voxelsSortedByDistanceFromPlane, List<Voxel> shell, DoubleVoxel balancePoint, DoubleVoxel initialCenterOfMass) {
        int counter = 0;
        DoubleVoxel best_com = new DoubleVoxel();
        Voxel currentVoxel;
        DoubleVoxel current_com = new DoubleVoxel(initialCenterOfMass);
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
                    System.out.println("Deleted " + counter + " voxels, Ecom is: " + current_Ecom);
                }
            }
        }
        System.out.println("Final Ecom: " + min_Ecom);
        return new BestValuesPojo(best_alpha, new DoubleVoxel(best_com));
        // best_alpha.stream().filter(voxel -> voxel.getAlpha()==1).skip(24258).forEach(System.out::println)
    }

    private double calcECom(DoubleVoxel current_com, DoubleVoxel balancePoint) {
        DoubleVoxel voxel = new DoubleVoxel(
                current_com.getX() - balancePoint.getX(),
                current_com.getY() - balancePoint.getY(),
                0
        );
        return calcNorm(voxel);
    }

    private double calcNorm(DoubleVoxel voxel) {
        double sum = 0;
        for (Axis axis : Axis.values()) {
            sum += Math.pow(voxel.get(axis), 2);
        }
        return Math.sqrt(sum);
    }

    private void updateCenterOfMass(DoubleVoxel current_com, Voxel currentVoxel, int xyz_updating_len) {
        for (Axis axis : Axis.values()) {
            current_com.set(axis, current_com.get(axis) * (xyz_updating_len + 1));
            current_com.set(axis, current_com.get(axis) - currentVoxel.get(axis));
            current_com.set(axis, current_com.get(axis) / xyz_updating_len);
        }
    }

    private double calcDistanceFromPlane(DoubleVoxel planes_coefs, Voxel point) {
        double length_coef = Math.sqrt(Math.pow(planes_coefs.getX(), 2) + Math.pow(planes_coefs.getY(), 2) + Math.pow(planes_coefs.getZ(), 2));
        double dot_point = (
                planes_coefs.getX() * point.getX() +
                        planes_coefs.getY() * point.getY() +
                        planes_coefs.getZ() * point.getZ() +
                        planes_coefs.getAlpha()
        );
        return dot_point / length_coef;
    }

    private DoubleVoxel calculateCuttingPlane(DoubleVoxel initialCenterOfMass, DoubleVoxel balancePoint) {
        DoubleVoxel planes_coefs = new DoubleVoxel(
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
