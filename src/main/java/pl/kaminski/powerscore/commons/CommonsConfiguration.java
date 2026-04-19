package pl.kaminski.powerscore.commons;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonsConfiguration {

    @Bean
    DateTimeProvider dateTimeProvider() {
        return new DateTimeProvider();
    }
}
