package com.globalco.jobboard.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger / OpenAPI auto-configuration class.
 * Generates API schemas, paths, operations list, and auth settings.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Smart Job Board REST API")
                        .version("1.0.0")
                        .description("Interactive REST API documentation for the Smart Job Board web application. " +
                                "Exposes endpoints for user registration, profiles, company directories, category/skill trees, job postings, and applications pipelines.")
                        .contact(new Contact()
                                .name("GlobalCo Enterprise Services")
                                .email("support@globalco.com")
                                .url("https://www.globalco.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                // Configure global JWT auth lock for future integrations
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", new SecurityScheme()
                                .name("Bearer Authentication")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Enter authorization JWT token (e.g. 'Bearer <token>').")));
    }
}
