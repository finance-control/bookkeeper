package com.marsofandrew.bookkeeper.configuration

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.extensions.Extension
import io.swagger.v3.oas.annotations.security.SecurityScheme
import io.swagger.v3.oas.annotations.security.SecuritySchemes
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@SecuritySchemes(
    SecurityScheme(
        type = SecuritySchemeType.HTTP,
        name = "basicAuth",
        scheme = "basic"
    ),
)
internal class OpenApiConfiguration {
    @Bean
    fun openApi(): OpenAPI {
        val info = Info()
            .title("Bookkeeper API")
            .version("1.0.0")
            .description("This API exposes endpoints to work with bookkeeper service.")

        return OpenAPI()
            .info(info)
            .security(mutableListOf(SecurityRequirement().addList("basicAuth")))
    }
}