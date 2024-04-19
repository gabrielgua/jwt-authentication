package com.gabriel.jwtauthentication.infra.springdoc;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class SpringDocConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("JWT Authentication")
                        .version("v1")
                        .description("REST API with authentication using JWT Tokens")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://springdoc.org")
                        )
                ).externalDocs(new ExternalDocumentation()
                        .description("gabrielgua")
                        .url("https://github.com/gabrielgua"));
    }

    @Bean
    public OpenApiCustomizer openApiCustomizer() {
        return openApi -> {
            openApi.getPaths()
                    .values()
                    .stream()
                    .flatMap(pathItem -> pathItem.readOperations().stream())
                    .forEach(operation -> {
                        ApiResponses responses = operation.getResponses();

                        var apiResponseNotFound = new ApiResponse().description("Resource not found");
                        var apiResponseInternalError = new ApiResponse().description("Internal server error");
                        var apiResponseNoRepresentation = new ApiResponse().description("Resource does not have a representation that could be consumed");


                        responses.addApiResponse("404", apiResponseNotFound);
                        responses.addApiResponse("406", apiResponseNoRepresentation);
                        responses.addApiResponse("500", apiResponseInternalError);
                    });

        };
    }


}
