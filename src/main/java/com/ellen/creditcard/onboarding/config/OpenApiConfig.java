package com.ellen.creditcard.onboarding.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI 3 (Swagger) configuration
 * Customizes API documentation and Swagger UI
 */
@Configuration
public class OpenApiConfig {

    /**
     * Configure main OpenAPI documentation
     */
    @Bean
    public OpenAPI creditCardOnboardingOpenAPI() {
        // Define security scheme (optional - no auth required per requirements)
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .name("X-API-Key");

        // Security requirement (disabled for this project as per requirements)
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("API Key");

        // API contact information
        Contact contact = new Contact()
                .name("Credit Card Onboarding Team")
                .email("support@creditcard.com")
                .url("https://creditcard.com/support");

        // API license information
        License license = new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT");

        // API metadata
        Info info = new Info()
                .title("Credit Card Onboarding API")
                .description("REST API for credit card application submission, third-party verification, scoring, and approval")
                .version("1.0.0")
                .contact(contact)
                .license(license)
                .termsOfService("https://creditcard.com/terms");

        // Server configuration
        Server localServer = new Server()
                .url("http://localhost:8080/api/credit-card")
                .description("Local Development Server");

        // Build OpenAPI configuration
        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer))
                // .components(new Components().addSecuritySchemes("API Key", securityScheme)) // Enable if auth is needed
                // .addSecurityItem(securityRequirement) // Enable if auth is needed
                ;
    }
}
