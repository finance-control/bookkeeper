package com.marsofandrew.bookkeeper.user.controller.exception

import com.marsofandrew.bookkeeper.user.exception.UserEmailIsAlreadyInUseException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
internal class UsersExceptionHandler {

    @ExceptionHandler(UserEmailIsAlreadyInUseException::class)
    fun onUserEmailIsAlreadyInUseException(exception: UserEmailIsAlreadyInUseException): ResponseEntity<*> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(IncorrectEmailUserMessages(listOf(exception.message ?: ""), exception.email.value))
    }
}