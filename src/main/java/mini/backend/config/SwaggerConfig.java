package mini.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("mini4 api")
                        .version("1.0")
                        .description("mini4 api document"));
    }

    @Bean
    public GroupedOpenApi api() {
        String[] paths = {"/api/v1/**"};
        String[] packagesToScan = {"mini.backend"};
        return GroupedOpenApi.builder()
                .group("mini4 api")
                .pathsToMatch(paths)
                .packagesToScan(packagesToScan)
                .build();
    }
}
