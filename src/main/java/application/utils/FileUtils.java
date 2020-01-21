package application.utils;

import application.Voxel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
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
            System.out.println("cannot create voxels from file, " + e.getMessage());
            throw e;
        }
        return voxels;
    }

    public void writeVoxelsToFile(List<Voxel> voxelsToWrite, String fileName) {
        String lineBreak = "\n";
        voxelsToWrite.stream()
                .filter(voxel -> voxel.getAlpha() == 1)
                .map(voxel -> voxel.toString() + lineBreak)
                .limit(1)
                .forEach(voxel -> {
                    try {
                        Files.write(Paths.get(fileName), voxel.getBytes(), StandardOpenOption.CREATE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        voxelsToWrite.stream()
                .filter(voxel -> voxel.getAlpha() == 1)
                .skip(1)
                .map(voxel -> voxel.toString() + lineBreak)
                .forEach(voxel -> {
                    try {
                        Files.write(Paths.get(fileName), voxel.getBytes(), StandardOpenOption.APPEND);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

    }

}
