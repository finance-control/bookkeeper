package com.marsofandrew.bookkeeper.user.controller

import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.user.UserLogin
import com.marsofandrew.bookkeeper.user.UserRegistration
import com.marsofandrew.bookkeeper.user.UserSelection
import com.marsofandrew.bookkeeper.user.controller.dto.*
import com.marsofandrew.bookkeeper.userContext.UserId
import com.marsofandrew.bookkeeper.userContext.getRequestClientId
import com.marsofandrew.bookkeeper.userContext.getRequestIpAddress
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.headers.Header
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

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
    ): UserIdTokenDto {
        val user = userLogin.login(userId.asId(), getRequestClientId(), getRequestIpAddress())
        return UserIdTokenDto(id = user.id.value)
    }

    @GetMapping("/current")
    fun select(
        @Parameter(hidden = true) @UserId userId: Long
    ): UserDto {
        return userSelection.select(userId.asId()).toUserDto()
    }
}
