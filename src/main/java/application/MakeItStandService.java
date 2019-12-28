package application;

import application.utils.FileUtils;
import application.utils.MatrixUtils;
import org.springframework.boot.CommandLineRunner;

import java.util.List;
import java.util.Set;

public class MakeItStandService implements CommandLineRunner {

    private final FileUtils fileUtils;
    private final MatrixUtils matrixUtils;

    public MakeItStandService(FileUtils fileUtils, MatrixUtils matrixUtils) {
        this.fileUtils = fileUtils;
        this.matrixUtils = matrixUtils;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Starting MakeItStand Application");
        List<Voxel> voxels = fileUtils.createVoxelsFromXyzFile("./files/test1.xyz");
        voxels.forEach(System.out::println);
        Set<Voxel> shell = matrixUtils.calcShellFromVoxels(voxels);

//        writeVoxelsToFile("voxelsEdges");
    }

}
