package com.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfiguration implements WebMvcConfigurer {

	@Bean
	public OpenAPI customOpenAPI() {
		// Create a security scheme for JWT Bearer token
		SecurityScheme securityScheme = new SecurityScheme()
				.type(SecurityScheme.Type.HTTP)
				.scheme("bearer")
				.bearerFormat("JWT");

		// Set the global security requirement for all API operations
		SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

		return new OpenAPI().addSecurityItem(securityRequirement)
				.components(new io.swagger.v3.oas.models.Components()
				.addSecuritySchemes("bearerAuth", securityScheme))
				.info(new Info().title("SpringBoot")
			    .description("SpringBoot REST API Doc").version("1.0.0"));
	}
}
