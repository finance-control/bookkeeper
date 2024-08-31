package com.marsofandrew.bookkeeper.credentials.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.marsofandrew.bookkeeper.credentials.CredentialsModification
import com.marsofandrew.bookkeeper.credentials.controller.dto.CredentialsModificationDto
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.userContext.AuthArgumentContextConfiguration
import com.marsofandrew.bookkeeper.userContext.UserIdToken
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.http.MediaType
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.patch

@WebMvcTest
@ContextConfiguration(
    classes = [
        CredentialsControllerTest.TestContextConfiguration::class,
        AuthArgumentContextConfiguration::class
    ]
)
internal class CredentialsControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setup() {
        clearAllMocks()
        SecurityContextHolder.getContext().authentication = UserIdToken(providedUserId)
    }

    @Test
    fun `patch modifies password when password is passed`() {
        val password = "newPass word";

        every { credentialsModification.modify(providedUserId.asId(), password) } returns Unit

        mockMvc.patch("/api/v1/credentials/current") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(
                CredentialsModificationDto(
                    password = password,
                )
            )
        }.andExpect {
            status { isNoContent() }
        }
    }

    @ContextConfiguration
    class TestContextConfiguration {

        @Primary
        @Bean
        fun credentialsController() = CredentialsController(credentialsModification)
    }

    private companion object {
        const val providedUserId = 5L

        val credentialsModification = mockk<CredentialsModification>()
    }
}

