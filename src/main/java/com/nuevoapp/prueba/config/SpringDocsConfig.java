package com.nuevoapp.prueba.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;


/**
 * Clase de configuración que disponibiliza el Bean de OpenApí para generar la página de SWAGGER con la documentación disponible.
 */
@SecurityScheme(
        name = "Authorization",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class SpringDocsConfig {

    @Bean
    public OpenAPI openApi() {
        // Add general info for api specification
        return new OpenAPI()
                .addServersItem(new Server().url("http://localhost:8080/admin").description("Local Host"))
                .info(
                        new Info()
                                .title("Task Services")
                                .description("Services REST for NEORIS test")
                                .termsOfService("")
                                .version("v1")
                                .license(new License().name("").url("https://unlicense.org"))
                                .contact(
                                        new io.swagger.v3.oas.models.info.Contact()
                                                .email("diegopena.quezada@gmail.com")));
    }
}
