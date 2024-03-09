package com.marsofandrew.bookkeeper.credentials.encoder

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
internal class CredentialsEncryptionContextConfiguration {

    @Bean
    fun passwordEncoder(): PasswordEncoder = Argon2PasswordEncoder(
        10,
        256,
        1,
        512,
        3
    )
}