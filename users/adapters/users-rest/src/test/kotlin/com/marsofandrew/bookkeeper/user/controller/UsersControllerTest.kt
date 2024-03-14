package com.marsofandrew.bookkeeper.user.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.marsofandrew.bookkeeper.controller.BaseExceptionHandler
import com.marsofandrew.bookkeeper.properties.email.Email
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.user.User
import com.marsofandrew.bookkeeper.user.UserRegistration
import com.marsofandrew.bookkeeper.user.controller.dto.RegistrationDataDto
import com.marsofandrew.bookkeeper.user.controller.dto.toUnregisteredUser
import com.marsofandrew.bookkeeper.user.controller.exception.UsersExceptionHandler
import com.marsofandrew.bookkeeper.user.exception.UserEmailIsAlreadyInUseException
import com.marsofandrew.bookkeeper.userContext.AuthArgumentContextConfiguration
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
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@WebMvcTest
@ContextConfiguration(
    classes = [
        UsersControllerTest.TestContextConfiguration::class,
        AuthArgumentContextConfiguration::class,
        UsersExceptionHandler::class,
        BaseExceptionHandler::class
    ]
)
internal class UsersControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var userRegistration: UserRegistration

    @BeforeEach
    fun setup() {
        clearAllMocks()
    }

    @Test
    fun `register when email is already in use then returns HTTP CODE 400`() {
        val registrationDataDto = RegistrationDataDto(
            name = "name",
            surname = "surname",
            email = "myTestEmail@Google.com",
            password = "my password"
        )

        every { userRegistration.register(registrationDataDto.toUnregisteredUser()) } throws UserEmailIsAlreadyInUseException(
            Email("yy@uu.com"),
            RuntimeException()
        )
        mockMvc.post("/api/v1/users/registration") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(registrationDataDto)
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun `register when all values are correct then returns created user id`() {
        val registrationDataDto = RegistrationDataDto(
            name = "name",
            surname = "surname",
            email = "myTestEmail@Google.com",
            password = "my password"
        )

        val userId = 5456.asId<User>()

        every { userRegistration.register(registrationDataDto.toUnregisteredUser()) } returns userId
        mockMvc.post("/api/v1/users/registration") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(registrationDataDto)
        }.andExpect {
            status { isCreated() }
            jsonPath("id") { value(userId.value) }
        }
    }


    @ContextConfiguration
    class TestContextConfiguration {

        @Primary
        @Bean
        fun userRegistration(): UserRegistration = mockk()

        @Primary
        @Bean
        fun transfersController(
            userRegistration: UserRegistration
        ) = UsersController(userRegistration)
    }
}