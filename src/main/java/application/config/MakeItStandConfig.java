package application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import application.MakeItStandService;
import application.utils.FileUtils;
import application.utils.MatrixUtils;

@Configuration
public class MakeItStandConfig {

    @Bean
    MakeItStandService makeItStandService(FileUtils fileUtils, MatrixUtils matrixUtils) {
        return new MakeItStandService(fileUtils, matrixUtils);
    }

    @Bean
    FileUtils fileUtils() {
        return new FileUtils();
    }

    @Bean
    MatrixUtils matrixUtils() {
        return new MatrixUtils();
    }
}
