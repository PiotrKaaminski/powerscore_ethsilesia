package pl.kaminski.powerscore;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collections;

@Configuration
public class SwaggerConfiguration implements WebMvcConfigurer {

    // skopiowane na szybko. Do zrobienia od nowa
    @Bean
    public OpenApiCustomizer customOpenAPI() {
        return openApi -> {
            openApi.setInfo(swaggerInfo());
            openApi.servers(Collections.singletonList(new Server().url("/").description("Defualt Server URL")));
        };
    }

    private Info swaggerInfo() {
        return new Info()
                .title("Powerscore API")
                .description("Powerscore API")
                .version("1.0");
    }

}
