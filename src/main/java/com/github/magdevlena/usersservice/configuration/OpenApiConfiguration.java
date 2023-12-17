package com.github.magdevlena.usersservice.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition
@Configuration
public class OpenApiConfiguration {
    @Bean
    public OpenAPI getOpenApiDocumentation() {
        return new OpenAPI()
                .info(new Info()
                        .title("Users application")
                        .description("Simple REST web service that provides information about GitHub user.")
                        .contact(new Contact()
                                .name("Latkowska Magdalena")
                                .email("latkowska.magdalena@gmail.com")
                        )
                );
    }
}
