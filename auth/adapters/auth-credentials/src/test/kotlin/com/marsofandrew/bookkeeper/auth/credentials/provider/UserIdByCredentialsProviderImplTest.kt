package com.marsofandrew.bookkeeper.auth.credentials.provider

import com.marsofandrew.bookkeeper.credentials.CredentialsUserIdSelection
import com.marsofandrew.bookkeeper.credentials.credentials
import com.marsofandrew.bookkeeper.properties.id.asId
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class UserIdByCredentialsProviderImplTest {

    private val credentialsUserIdSelection = mockk<CredentialsUserIdSelection>()

    private lateinit var userIdByCredentialsProviderImpl: UserIdByCredentialsProviderImpl

    @BeforeEach
    fun setup() {
        userIdByCredentialsProviderImpl = UserIdByCredentialsProviderImpl(credentialsUserIdSelection)
    }

    @ParameterizedTest
    @ValueSource(longs = [5, 6])
    fun `getIdByCredentials returns userId by provided credentials`(userId: Long) {
        val credentials = credentials()

        every { credentialsUserIdSelection.select(credentials) } returns userId.asId()

        val result = userIdByCredentialsProviderImpl.getIdByCredentials(credentials.email, credentials.password)

        result shouldBe userId
    }

    @Test
    fun `getIdByCredentials when user is absent returns null`() {
        val credentials = credentials()

        every { credentialsUserIdSelection.select(credentials) } returns null

        val result = userIdByCredentialsProviderImpl.getIdByCredentials(credentials.email, credentials.password)

        result shouldBe null
    }
}