package com.marsofandrew.bookkeeper.controller

import com.marsofandrew.bookkeeper.controller.dto.ErrorMessages
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
internal class BaseExceptionHandler {

    @ExceptionHandler(Exception::class)
    fun onInternalError(exception: Exception): ResponseEntity<*> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorMessages(listOf(exception.message ?: "")))
    }
}