package application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import application.MakeItStandService;

@Configuration
public class MakeItStandConfig {

    @Bean
    MakeItStandService makeItStandService() {
        return new MakeItStandService();
    }
}
