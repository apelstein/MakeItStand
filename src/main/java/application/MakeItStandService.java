package application;

import application.utils.BestAlphaCalculator;
import application.utils.FileUtils;
import application.utils.ShellCalculator;
import application.utils.Voxel;
import org.springframework.boot.CommandLineRunner;

import java.util.List;

public class MakeItStandService implements CommandLineRunner {

    private final FileUtils fileUtils;
    private final BestAlphaCalculator bestAlphaCalculator;
    private final ShellCalculator shellCalculator;

    public MakeItStandService(FileUtils fileUtils, BestAlphaCalculator bestAlphaCalculator, ShellCalculator shellCalculator) {
        this.fileUtils = fileUtils;
        this.bestAlphaCalculator = bestAlphaCalculator;
        this.shellCalculator = shellCalculator;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Starting MakeItStand Application");
        String inputName = args[0];
        List<Voxel> voxelsFromXyzFile = fileUtils.createVoxelsFromXyzFile(inputName + ".xyz");
        List<Voxel> shell = shellCalculator.calcShellFromVoxels(voxelsFromXyzFile);
        fileUtils.writeVoxelsToFile(shell, inputName + "_shell.xyz");
        List<Voxel> bestAlphaValues = bestAlphaCalculator.calcBestAlpha(shell, voxelsFromXyzFile);
        fileUtils.writeVoxelsToFile(bestAlphaValues, inputName + "_bestAlpha.xyz");
        System.out.println("Finished MakeItStand Successfully");
    }

}
