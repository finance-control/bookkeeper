package com.marsofandrew.bookkeeper.user.controller

import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.user.UserLogin
import com.marsofandrew.bookkeeper.user.UserRegistration
import com.marsofandrew.bookkeeper.user.controller.dto.RegistrationDataDto
import com.marsofandrew.bookkeeper.user.controller.dto.UserIdDto
import com.marsofandrew.bookkeeper.user.controller.dto.toUnregisteredUser
import com.marsofandrew.bookkeeper.userContext.UserId
import io.swagger.annotations.ApiParam
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/users")
internal class UsersController(
    private val userRegistration: UserRegistration,
    private val userLogin: UserLogin
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
        @ApiParam(hidden = true) @UserId userId: Long
    ): UserIdDto {
        val user = userLogin.login(userId.asId())
        return UserIdDto(id = user.id.value)
    }
}
