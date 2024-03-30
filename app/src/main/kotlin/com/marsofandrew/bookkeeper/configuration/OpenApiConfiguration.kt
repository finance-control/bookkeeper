package com.marsofandrew.bookkeeper.configuration

import io.swagger.v3.oas.annotations.Hidden
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
internal class OpenApiConfiguration {

    @Bean
    fun openApi(): OpenAPI {
        val info = Info()
            .title("Bookkeeper API")
            .version("1.0.0")
            .description("This API exposes endpoints to work with bookkeeper service.")

        return OpenAPI()
            .info(info)
    }
}