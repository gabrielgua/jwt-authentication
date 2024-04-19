package com.gabriel.jwtauthentication.infra.springdoc;

import com.gabriel.jwtauthentication.api.exception.Error;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Configuration
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class SpringDocConfig {

    private static final String badRequestResponse = "BadRequestResponse";
    private static final String internalServerErrorResponse = "InternalServerErrorResponse";
    private static final String unauthorizedResponse = "UnauthorizedResponse";
    private static final String badCredentialsResponse = "BadCredentialsResponse";


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
                        .url("https://github.com/gabrielgua")
                ).components(new Components()
                        .schemas(generateSchemas())
                        .responses(generateResponses())
                );
    }

    @Bean
    public OpenApiCustomizer openApiCustomizer() {
        return openApi -> {
            openApi.getPaths()
                    .values()
                    .forEach(pathItem -> pathItem.readOperationsMap()
                            .forEach((httpMethod, operation) -> {
                                var responses = operation.getResponses();
                                switch (httpMethod) {
                                    case GET:
                                        responses.addApiResponse("401", new ApiResponse().$ref(unauthorizedResponse));
                                        responses.addApiResponse("500", new ApiResponse().$ref(internalServerErrorResponse));
                                        break;
                                    case POST:
                                        responses.addApiResponse("401", new ApiResponse().$ref(badCredentialsResponse));
                                        responses.addApiResponse("400", new ApiResponse().$ref(badRequestResponse));
                                        responses.addApiResponse("500", new ApiResponse().$ref(internalServerErrorResponse));
                                        break;

                                    default:
                                        responses.addApiResponse("500", new ApiResponse().$ref(internalServerErrorResponse));
                                        break;
                                }
                            }));

        };
    }

    private Map<String, Schema> generateSchemas() {
        final Map<String, Schema> schemaMap = new HashMap<>();

        Map<String, Schema> errorSchema = ModelConverters.getInstance().read(Error.class);
        Map<String, Schema> errorFieldSchema = ModelConverters.getInstance().read(Error.ErrorField.class);

        schemaMap.putAll(errorSchema);
        schemaMap.putAll(errorFieldSchema);

        return schemaMap;
    }

    private Map<String, ApiResponse> generateResponses() {
        final Map<String, ApiResponse> apiResponsesMap = new HashMap<>();

        Content content  = new Content()
                .addMediaType(APPLICATION_JSON_VALUE, new MediaType().schema(new Schema<Error>().$ref("Error")));

        apiResponsesMap.put(internalServerErrorResponse, new ApiResponse().description("Internal Server Error").content(content));
        apiResponsesMap.put(badRequestResponse, new ApiResponse().description("Bad request").content(content));
        apiResponsesMap.put(badCredentialsResponse, new ApiResponse().description("Bad credentials").content(content));
        apiResponsesMap.put(unauthorizedResponse, new ApiResponse().description("Unauthorized request").content(content));


        return apiResponsesMap;
    }

}
