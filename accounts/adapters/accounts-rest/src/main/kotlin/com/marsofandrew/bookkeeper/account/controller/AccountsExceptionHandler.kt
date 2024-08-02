package com.marsofandrew.bookkeeper.account.controller

import com.marsofandrew.bookkeeper.account.exception.AccountIllegalStateException
import com.marsofandrew.bookkeeper.controller.dto.ErrorMessages
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
internal class AccountsExceptionHandler {

    @ExceptionHandler(AccountIllegalStateException::class)
    fun onAccountIllegalStateException(exception: AccountIllegalStateException): ResponseEntity<*> {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(ErrorMessages(listOf(exception.message ?: "")))
    }
}