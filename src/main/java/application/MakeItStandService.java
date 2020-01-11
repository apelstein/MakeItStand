package application;

import application.utils.FileUtils;
import application.utils.MatrixUtils;
import org.springframework.boot.CommandLineRunner;

import java.util.List;

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
        String inputName = args[0];
        List<Voxel> voxelsFromXyzFile = fileUtils.createVoxelsFromXyzFile("Inputs/" + inputName + ".xyz");
        List<Voxel> shell = matrixUtils.calcShellFromVoxels(voxelsFromXyzFile);
        fileUtils.writeVoxelsToFile(shell, "Outputs/" + inputName + "_shell.asc");
        DoubleVoxel balancePoint = matrixUtils.calcBalancePoint(voxelsFromXyzFile);
        DoubleVoxel initialCenterOfMass = matrixUtils.calcCenterOfMass(voxelsFromXyzFile);
        BestValuesPojo bestAlphaValues = matrixUtils.calcBestValues(balancePoint, initialCenterOfMass, shell, voxelsFromXyzFile);
//        fileUtils.writeVoxelsToFile(Collections.singletonList(balancePoint), "Outputs/" + inputName + "_balancedPoint.asc");
        DoubleVoxel bestCenterOfMass = bestAlphaValues.getBestCenterOfMass();
//        fileUtils.writeVoxelsToFile(Collections.singletonList(bestCenterOfMass), "Outputs/" + inputName + "_bestCoM.asc");
        fileUtils.writeVoxelsToFile(bestAlphaValues.getBestAlpha(), "Outputs/" + inputName + "_bestAlpha.asc");
        System.out.println("Finished MakeItStand Successfully");
    }

}
