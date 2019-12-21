package application.config;

import application.MakeItStandService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MakeItStandConfig {

    @Bean
    MakeItStandService makeItStandService() {
        return new MakeItStandService();
    }
}
