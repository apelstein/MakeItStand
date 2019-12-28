package application.utils;

import application.Voxel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FileUtils {

    public List<Voxel> createVoxelsFromXyzFile(String xyzFile) throws IOException {
        List<Voxel> voxels = new ArrayList<>();
        try {
            Files.lines(Paths.get(xyzFile)).forEach(line -> voxels
                    .add(new Voxel(Stream.of(line.split(" "))
                            .mapToInt(Integer::parseInt)
                            .toArray())));
        } catch (IOException e) {
            System.out.println(e.getMessage() + "\n"); //+ e.getLocalizedMessage() + "\n" + e.getCause().toString());
            throw e;
        }
        return voxels;
    }

    public void writeVoxelsToFile(String fileName) throws IOException {
//        Files.write(Paths.get(fileName), (Iterable<? extends CharSequence>) voxels); //StandardCharsets.UTF_8
    }

}
