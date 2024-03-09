package com.marsofandrew.bookkeeper.credentials.encoder

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
internal class CredentialsEncoderImpl(
    private val passwordEncoder: PasswordEncoder
) : CredentialsEncoder {

    override fun encode(value: String): String {
        return passwordEncoder.encode(value)
    }

    override fun matches(rawPassword: String, encodedPassword: String): Boolean {
        return passwordEncoder.matches(rawPassword, encodedPassword)
    }
}