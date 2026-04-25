package com.gcompagno.ecommerce.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Bazar Manager API",
                version = "0.0.1",
                description = """
                        REST API para la gestión de un bazar: clientes, productos y ventas.
                        Autenticación stateless con JWT (HS256). Los endpoints protegidos requieren
                        un Bearer token obtenido vía POST /auth/login.""",
                contact = @Contact(name = "Guido Compagno", email = "guidocompagno@gmail.com"),
                license = @License(name = "MIT")
        ),
        security = @SecurityRequirement(name = "Security Token")
)
@SecurityScheme(
        name = "Security Token",
        description = "Access Token for the API",
        type = SecuritySchemeType.HTTP,
        paramName = "Authorization",
        in = SecuritySchemeIn.HEADER,
        scheme = "bearer",
        bearerFormat = "JWT"

)
public class SwaggerConfig {
}
