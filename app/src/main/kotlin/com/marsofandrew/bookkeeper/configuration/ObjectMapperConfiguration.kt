package com.marsofandrew.bookkeeper.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
internal class ObjectMapperConfiguration {

    @Bean
    fun objectMapper(): ObjectMapper = with(ObjectMapper()) {
        registerKotlinModule()
        registerModule(JavaTimeModule())
    }
}
