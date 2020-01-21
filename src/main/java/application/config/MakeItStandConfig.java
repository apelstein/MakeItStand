package application.config;

import application.MakeItStandService;
import application.utils.BestAlphaCalculator;
import application.utils.FileUtils;
import application.utils.LinearAlgebraUtils;
import application.utils.ShellCalculator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MakeItStandConfig {

    @Bean
    MakeItStandService makeItStandService(FileUtils fileUtils, BestAlphaCalculator bestAlphaCalculator, ShellCalculator shellCalculator) {
        return new MakeItStandService(fileUtils, bestAlphaCalculator, shellCalculator);
    }

    @Bean
    ShellCalculator shellCalculator(LinearAlgebraUtils linearAlgebraUtils) {
        return new ShellCalculator(linearAlgebraUtils);
    }

    @Bean
    LinearAlgebraUtils linearAlgebraUtils() {
        return new LinearAlgebraUtils();
    }

    @Bean
    FileUtils fileUtils() {
        return new FileUtils();
    }

    @Bean
    BestAlphaCalculator matrixUtils(LinearAlgebraUtils linearAlgebraUtils) {
        return new BestAlphaCalculator(linearAlgebraUtils);
    }
}
