package application;

import org.springframework.boot.CommandLineRunner;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

public class MakeItStandService implements CommandLineRunner {

    private FindShell findShell;
    private Set<Voxel> shell;
    private List<Voxel> voxels;

    public List<Voxel> createVoxelsFromXyzFile(String xyzFile) throws IOException {
        List<Voxel> voxels = new ArrayList<>();
        try {
            Files.lines(Paths.get(xyzFile)).forEach(line -> {
                voxels.add(new Voxel(Stream.of(line.split(" ")).mapToInt(Integer::parseInt).toArray()));
            });
        } catch (IOException e) {
            System.out.println(e.getMessage() + "\n"); //+ e.getLocalizedMessage() + "\n" + e.getCause().toString());
            throw e;
        }
        return voxels;
    }

    public void writeVoxelsToFile(String fileName) throws IOException {
        Files.write(Paths.get(fileName), (Iterable<? extends CharSequence>) voxels); //StandardCharsets.UTF_8

    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Hello");
        voxels = createVoxelsFromXyzFile("./files/test1.xyz");
        voxels.forEach(System.out::println);
        findShell = new FindShell(voxels);
        shell = findShell.getShell();

        writeVoxelsToFile("voxelsEdges");
    }

}
