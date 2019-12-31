package application.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import application.Voxel;

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

    public void writeVoxelsToFile(List<Voxel> voxelsToWrite, String fileName) {
        voxelsToWrite.stream()
                .map(Voxel::toString)
                .forEach(voxel -> {
                    try {
                        Files.write(Paths.get(fileName), voxel.getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                });
    }

}
