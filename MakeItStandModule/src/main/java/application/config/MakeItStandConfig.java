package application.config;

import application.ArgumentHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MakeItStandConfig {

    @Bean
    ArgumentHandler argumentHandler() {
        return new ArgumentHandler();
    }



}
