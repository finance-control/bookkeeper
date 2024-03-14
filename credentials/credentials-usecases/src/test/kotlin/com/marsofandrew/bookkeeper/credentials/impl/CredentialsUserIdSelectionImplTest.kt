package com.marsofandrew.bookkeeper.credentials.impl

import com.marsofandrew.bookkeeper.credentials.access.CredentialsStorage
import com.marsofandrew.bookkeeper.credentials.credentials
import com.marsofandrew.bookkeeper.credentials.encoder.CredentialsEncoder
import com.marsofandrew.bookkeeper.credentials.user.User
import com.marsofandrew.bookkeeper.credentials.userCredentials
import com.marsofandrew.bookkeeper.properties.id.asId
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class CredentialsUserIdSelectionImplTest {

    private val credentialsEncoder = mockk<CredentialsEncoder>()
    private val credentialsStorage = mockk<CredentialsStorage>()

    private lateinit var credentialsUserIdSelectionImpl: CredentialsUserIdSelectionImpl

    @BeforeEach
    fun setup() {
        credentialsUserIdSelectionImpl = CredentialsUserIdSelectionImpl(credentialsEncoder, credentialsStorage)
    }

    @Test
    fun `select when user is absent returns null`() {
        val credentials = credentials()

        every { credentialsStorage.findByEmail(credentials.email) } returns null

        val result = credentialsUserIdSelectionImpl.select(credentials)

        result shouldBe null
    }

    @Test
    fun `select when passwords don not match returns null`() {
        val credentials = credentials()
        val userCredentials = userCredentials(5.asId()) {
            password = "different"
        }

        every { credentialsStorage.findByEmail(credentials.email) } returns userCredentials
        every { credentialsEncoder.matches(credentials.password, userCredentials.password) } returns false

        val result = credentialsUserIdSelectionImpl.select(credentials)

        result shouldBe null
    }

    @Test
    fun `select when user by email is present and passwords matches returns user ID`() {
        val credentials = credentials()
        val userId = 5.asId<User>()
        val userCredentials = userCredentials(userId)

        every { credentialsStorage.findByEmail(credentials.email) } returns userCredentials
        every { credentialsEncoder.matches(credentials.password, userCredentials.password) } returns true

        val result = credentialsUserIdSelectionImpl.select(credentials)

        result shouldBe userId
    }
}
