package mini.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import io.swagger.v3.oas.models.security.SecurityScheme.In;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("mini4 api")
                        .version("1.0")
                        .description("mini4 api document"))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .in(In.HEADER)
                                        .name("Authorization")));
    }

    @Bean
    public GroupedOpenApi api() {
        String[] paths = {"/**"};
        String[] packagesToScan = {"mini.backend"};
        return GroupedOpenApi.builder()
                .group("mini4 api")
                .pathsToMatch(paths)
                .packagesToScan(packagesToScan)
                .build();
    }
}
