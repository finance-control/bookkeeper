package com.marsofandrew.bookkeeper.user.controller

import com.marsofandrew.bookkeeper.user.UserRegistration
import com.marsofandrew.bookkeeper.user.controller.dto.RegistrationDataDto
import com.marsofandrew.bookkeeper.user.controller.dto.UserIdDto
import com.marsofandrew.bookkeeper.user.controller.dto.toUnregisteredUser
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/users")
internal class UsersController(
    private val userRegistration: UserRegistration
) {

    @PostMapping("/registration")
    @ResponseStatus(code = HttpStatus.CREATED)
    fun register(
        @RequestBody body: RegistrationDataDto
    ): UserIdDto {
        val userId = userRegistration.register(body.toUnregisteredUser())
        return UserIdDto(id = userId.value)
    }
}