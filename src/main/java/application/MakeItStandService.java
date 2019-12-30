package application;

import java.util.List;

import org.springframework.boot.CommandLineRunner;

import application.utils.FileUtils;
import application.utils.MatrixUtils;

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
        // String itemName = args[1];
        List<Voxel> voxelsFromXyzFile = fileUtils.createVoxelsFromXyzFile("./files/test1.xyz");
        voxelsFromXyzFile.forEach(System.out::println);
        List<Voxel> shell = matrixUtils.calcShellFromVoxels(voxelsFromXyzFile);
//        fileUtils.writeVoxelsToFile(shell, "3DFiles/Outputs/" + itemName + "_edges.xyz");
        Voxel balancePoint = matrixUtils.calcBalancePoint(voxelsFromXyzFile);
        Voxel initialCenterOfMass = matrixUtils.calcCenterOfMass(voxelsFromXyzFile);
        BestValuesPojo bestAlphaValues = matrixUtils.calcBestValues(balancePoint, initialCenterOfMass, shell, voxelsFromXyzFile);
//        fileUtils.writeVoxelsToFile(balancePoint, "3DFiles/Outputs/" + itemName + "_balancedPoint.xyz");
//        fileUtils.writeVoxelsToFile(bestAlphaValues.getBestCenterOfMass(), "3DFiles/Outputs/" + itemName + "_bestCoM.xyz");
//        fileUtils.writeVoxelsToFile(bestAlphaValues.getBestAlpha(), "3DFiles/Outputs/" + itemName + "_bestAlpha.xyz");
        System.out.printf("Finished MakeItStand Successfully");
    }

}
