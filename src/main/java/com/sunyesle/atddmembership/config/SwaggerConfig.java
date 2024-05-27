package com.sunyesle.atddmembership.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@SecurityScheme(
//        name = "Authorization",
//        type = SecuritySchemeType.HTTP,
//        scheme = "bearer",
//        bearerFormat = "JWT"
//)
//@OpenAPIDefinition(
//        info = @Info(title = "멤버십 적립 서비스 API"),
//        security = @SecurityRequirement(name = "Authorization")
//)
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .name("Authorization");

        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("Bearer Token");

        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("Bearer Token", securityScheme)
                )
                .addSecurityItem(securityRequirement)
                .info(new Info()
                        .title("멤버십 적립 서비스 API")
                );
    }
}
