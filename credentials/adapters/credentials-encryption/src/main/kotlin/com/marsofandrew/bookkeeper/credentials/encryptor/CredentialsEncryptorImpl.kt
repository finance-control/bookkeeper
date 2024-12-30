package com.marsofandrew.bookkeeper.credentials.encryptor

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
internal class CredentialsEncryptorImpl(
    private val passwordEncoder: PasswordEncoder
) : CredentialsEncryptor {

    override fun encode(value: String): String {
        return passwordEncoder.encode(value)
    }

    override fun matches(rawPassword: String, encodedPassword: String): Boolean {
        return passwordEncoder.matches(rawPassword, encodedPassword)
    }
}