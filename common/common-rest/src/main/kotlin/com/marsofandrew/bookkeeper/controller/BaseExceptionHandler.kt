package com.marsofandrew.bookkeeper.controller

import com.marsofandrew.bookkeeper.base.exception.DomainModelNotFoundException
import com.marsofandrew.bookkeeper.controller.dto.ErrorMessages
import com.marsofandrew.bookkeeper.properties.exception.ValidationException
import org.apache.logging.log4j.LogManager
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

// Open for test purposes
@ControllerAdvice
class BaseExceptionHandler {

    private val logger = LogManager.getLogger()

    @ExceptionHandler(Exception::class)
    fun onInternalError(exception: Exception): ResponseEntity<*> {
        logger.error("InternalError: ${exception.message}", exception)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorMessages(listOf(exception.message ?: "")))
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun onHttpMessageNotReadableException(exception: HttpMessageNotReadableException): ResponseEntity<*> {
        logger.error("Invalid input: ${exception.message}", exception)

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorMessages(listOf(exception.message ?: "")))
    }

    @ExceptionHandler(ValidationException::class)
    fun onValidationError(exception: ValidationException): ResponseEntity<*> {
        logger.error("ValidationException: ${exception.message}", exception)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorMessages(listOf(exception.message ?: "")))
    }

    @ExceptionHandler(DomainModelNotFoundException::class)
    fun onDomainModelNotFoundError(exception: DomainModelNotFoundException): ResponseEntity<*> {
        logger.error("DomainModelNotFoundException: ${exception.message}", exception)
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ErrorMessages(listOf(exception.message ?: "")))
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDeniedException(ex: AccessDeniedException): ResponseEntity<*> {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body<String>("Access denied: " + ex.message)
    }
}