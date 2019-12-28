package application;

import lombok.Getter;

import java.util.*;

public class FindShell {

    private ArrayList<Voxel> voxels;
    @Getter
    private Set<Voxel> shell;
    public FindShell(ArrayList<Voxel> voxels){
        this.voxels = voxels;
        this.createShell();

    }

    public int getMax(Axe axe){
        return Collections.max(voxels, (Voxel v1, Voxel v2)-> v1.get(axe)-v2.get(axe)).get(axe);
    }

    public int getMin(Axe axe){
        return Collections.min(voxels, (Voxel v1, Voxel v2)-> v1.get(axe)-v2.get(axe)).get(axe);
    }

    public int maxX(){
        return Collections.max(voxels, (Voxel v1, Voxel v2) -> v1.getX()-v2.getX()).getX();
    }

    public int minX(){
        return Collections.min(voxels, (Voxel v1, Voxel v2) -> v1.getX()-v2.getX()).getX();
    }
    public int maxY(){
        return Collections.max(voxels, (Voxel v1, Voxel v2) -> v1.getY()-v2.getY()).getY();
    }

    public int minY(){
        return Collections.min(voxels, (Voxel v1, Voxel v2) -> v1.getY()-v2.getY()).getY();
    }
    public int maxZ(){
        return Collections.max(voxels, (Voxel v1, Voxel v2) -> v1.getZ()-v2.getZ()).getZ();
    }

    public int minZ(){
        return Collections.min(voxels, (Voxel v1, Voxel v2) -> v1.getZ()-v2.getZ()).getZ();
    }

    public Set<Voxel> zEdges(int x, int y){
        int lastZ = -1000;
        Set<Voxel> zEdges = new HashSet<>();
        ArrayList<Voxel> filteredVoxels = new ArrayList<>();
        voxels.forEach(v ->{
            if (v.getX() == x && v.getY()==y){
                filteredVoxels.add(v);
            }
        });
        for(Voxel v: filteredVoxels){
            if(lastZ == -1000){
                zEdges.add(new Voxel(x, y, v.getZ()));
            } else if(lastZ+1 < v.getZ()){
                zEdges.add(new Voxel(x, y, v.getZ()));
                zEdges.add(new Voxel(x, y, lastZ));
            }
            lastZ = v.getZ();
        }

        if(lastZ != -1000){
            zEdges.add(new Voxel(x, y, lastZ));
        }
         return zEdges;

    }

    public Set<Voxel> yEdges(int x, int z){
        int lastY = -1000;
        Set<Voxel> yEdges = new HashSet<>();
        ArrayList<Voxel> filteredVoxels = new ArrayList<>();
        voxels.forEach(v ->{
            if (v.getX() == x && v.getZ()==z){
                filteredVoxels.add(v);
            }
        });
        for(Voxel v: filteredVoxels){
            if(lastY == -1000){
                yEdges.add(new Voxel(x, v.getY(), z));
            } else if(lastY+1 < v.getY()){
                yEdges.add(new Voxel(x, v.getY(), z));
                yEdges.add(new Voxel(x, lastY, z));
            }
            lastY = v.getY();
        }

        if(lastY != -1000){
            yEdges.add(new Voxel(x, lastY, z));
        }
        return yEdges;

    }

    public Set<Voxel> xEdges(int y, int z){
        int lastX = -1000;
        Set<Voxel> xEdges = new HashSet<>();
        ArrayList<Voxel> filteredVoxels = new ArrayList<>();
        voxels.forEach(v ->{
            if (v.getY() == y && v.getZ()==z){
                filteredVoxels.add(v);
            }
        });
        for(Voxel v: filteredVoxels){
            if(lastX == -1000){
                xEdges.add(new Voxel(v.getX(), y, z));
            } else if(lastX+1 < v.getX()){
                xEdges.add(new Voxel(v.getX(), y, z));
                xEdges.add(new Voxel(lastX, y, z));
            }
            lastX = v.getX();
        }

        if(lastX != -1000){
            xEdges.add(new Voxel(lastX, y, z));
        }
        return xEdges;

    }

    public Set<Voxel> edges(Axe edgeType, int axe1, Axe axeType1, int axe2, Axe axeType2){
        int last = -1000;
        Set<Voxel> edges = new HashSet<>();
        ArrayList<Voxel> filteredVoxels = new ArrayList<>();
        voxels.forEach(v ->{
            if (v.get(axeType1) == axe1 && v.get(axeType2)==axe2){
                filteredVoxels.add(v);
            }
        });
        Map<Axe, Integer> initMap = new HashMap<>();
        initMap.put(axeType1, axe1);
        initMap.put(axeType2, axe2);
        for(Voxel v: filteredVoxels){
            if(last == -1000){
                initMap.put(edgeType, v.get(edgeType));
                edges.add(new Voxel(initMap));
            } else if(last+1 < v.getX()){
                initMap.put(edgeType, v.get(edgeType));
                edges.add(new Voxel(initMap));
                initMap.put(edgeType, last);
                edges.add(new Voxel(initMap));
            }
            last = v.get(edgeType);
        }

        if(last != -1000){
            initMap.put(edgeType, last);
            edges.add(new Voxel(initMap));
        }
        return edges;

    }



    private void createShell(){
        shell = new HashSet<>();
        for(Axe axe: Axe.values()){
            //sort the array by the relavent axe (i.e. X,Y,Z)
            Collections.sort(voxels, (Voxel v1, Voxel v2)->v1.get(axe) - v2.get(axe));
            ArrayList<Axe> arrayOfAxes = Axe.get2OtherAxes(axe);
            for(int axe1 = getMin(arrayOfAxes.get(0)); axe1<= getMax(arrayOfAxes.get(0)); axe1++){
                for(int axe2 = getMin(arrayOfAxes.get(1)); axe2<=getMax(arrayOfAxes.get(1)); axe2++){
                    shell.addAll(edges(axe, axe1, arrayOfAxes.get(0), axe2, arrayOfAxes.get(1)));
                }
            }
            System.out.println("added all " + axe.toString() + " edges");
        }

    }
}
