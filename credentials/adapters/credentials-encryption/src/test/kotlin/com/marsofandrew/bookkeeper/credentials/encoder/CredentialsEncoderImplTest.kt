package com.marsofandrew.bookkeeper.credentials.encoder

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder

@SpringBootTest(classes = [CredentialsEncryptionContextConfiguration::class])
internal class CredentialsEncoderImplTest {

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    private lateinit var credentialsEncoderImpl: CredentialsEncoderImpl

    @BeforeEach
    fun setup() {
        credentialsEncoderImpl = CredentialsEncoderImpl(passwordEncoder)
    }

    @ParameterizedTest
    @ValueSource(strings = ["test", "25631h", "sklwhs"])
    fun `encode when value is provided returns encoded string`(raw: String) {
        credentialsEncoderImpl.encode(raw) shouldNotBe raw
    }

    @Test
    fun `matches when encoded string matches raw returns true`() {
        val raw = "test"
        val encoded = credentialsEncoderImpl.encode(raw)

        credentialsEncoderImpl.matches(raw, encoded) shouldBe true
    }

    @Test
    fun `matches when encoded string does not match raw returns false`() {
        val raw = "test"
        val encoded = credentialsEncoderImpl.encode(raw + "tt")

        credentialsEncoderImpl.matches(raw, encoded) shouldBe false
    }
}
