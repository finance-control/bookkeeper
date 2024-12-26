package com.marsofandrew.bookkeeper.user.controller

import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.user.UserLogin
import com.marsofandrew.bookkeeper.user.UserRegistration
import com.marsofandrew.bookkeeper.user.UserSelection
import com.marsofandrew.bookkeeper.user.controller.dto.RegistrationDataDto
import com.marsofandrew.bookkeeper.user.controller.dto.UserDto
import com.marsofandrew.bookkeeper.user.controller.dto.UserIdDto
import com.marsofandrew.bookkeeper.user.controller.dto.UserIdTokenDto
import com.marsofandrew.bookkeeper.user.controller.dto.toUnregisteredUser
import com.marsofandrew.bookkeeper.user.controller.dto.toUserDto
import com.marsofandrew.bookkeeper.controller.DEFAULT_CLIENT_ID
import com.marsofandrew.bookkeeper.controller.getRequestClientId
import com.marsofandrew.bookkeeper.controller.getRequestIpAddress
import com.marsofandrew.bookkeeper.userContext.UserId
import io.swagger.v3.oas.annotations.Parameter
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/users")
internal class UsersController(
    private val userRegistration: UserRegistration,
    private val userLogin: UserLogin,
    private val userSelection: UserSelection
) {

    @PostMapping("/registration")
    @ResponseStatus(code = HttpStatus.CREATED)
    fun register(
        @RequestBody body: RegistrationDataDto
    ): UserIdDto {
        val userId = userRegistration.register(body.toUnregisteredUser())
        return UserIdDto(id = userId.value)
    }

    @GetMapping("/signing")
    fun login(
        @Parameter(hidden = true) @UserId userId: Long,
        @RequestParam("token_ttl", required = false, defaultValue = "PT6H") duration: Duration
    ): UserIdTokenDto {
        val user = userLogin.login(
            id = userId.asId(),
            clientId = getRequestClientId() ?: DEFAULT_CLIENT_ID,
            ipAddress = getRequestIpAddress(),
            ttl = duration
        )
        return UserIdTokenDto(
            id = user.user.id.value,
            token = user.token,
            tokenExpiresAt = LocalDateTime.ofInstant(user.tokenExpiresAt, ZoneId.of("Z")).toString()
        )
    }

    @GetMapping("/current")
    fun select(
        @Parameter(hidden = true) @UserId userId: Long
    ): UserDto {
        return userSelection.select(userId.asId()).toUserDto()
    }
}
