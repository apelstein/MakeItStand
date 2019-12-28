package application;

import org.springframework.boot.CommandLineRunner;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

public class MakeItStandService implements CommandLineRunner {

    private FindShell findShell;
    private Set<Voxel> shell;
    private ArrayList<Voxel> voxels;

    public ArrayList<Voxel> createVoxelsFromXyzFile(String xyzFile) throws IOException {
        ArrayList<Voxel> arrayList = new ArrayList<>();
        try{
            Files.lines(Paths.get(xyzFile)).forEach(line -> {
                arrayList.add(new Voxel(Stream.of(line.split(" ")).mapToInt(Integer::parseInt).toArray()));
            });
        } catch (IOException e){
            System.out.println(e.getMessage()+ "\n"); //+ e.getLocalizedMessage() + "\n" + e.getCause().toString());
            throw e;
        }
        return arrayList;
    }

    public void writeVoxelsToFile(String fileName) throws IOException {
        Files.write(Paths.get(fileName), (Iterable<? extends CharSequence>) voxels); //StandardCharsets.UTF_8

    }

        @Override
    public void run(String... args) throws Exception {
        System.out.println("Hello");
        this.voxels = createVoxelsFromXyzFile("./files/test1.xyz");
        voxels.forEach(voxel -> {
            System.out.println(voxel);
        });
        this.findShell = new FindShell(voxels);
        this.shell = this.findShell.getShell();

        this.writeVoxelsToFile("voxelsEdges");
    }

}
