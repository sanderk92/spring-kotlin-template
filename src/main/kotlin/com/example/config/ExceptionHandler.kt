package com.example.config

import jakarta.validation.ConstraintViolation
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler
    fun handle(exception: ConstraintViolationException): ResponseEntity<ProblemDetail> =
        ResponseEntity.badRequest().body(
            ProblemDetail.forStatus(HttpStatus.BAD_REQUEST).also {
                it.title = "validation failed"
                it.detail = "constraint on input was violated"
                it.setProperty("errors", exception.constraintViolations.map(::asValidationError))
            }
        )

    private fun asValidationError(error: ConstraintViolation<*>) = ValidationError(
        message = error.message ?: "no message",
        field = error.propertyPath.toString(),
        value = error.invalidValue.toString(),
    )

    @ExceptionHandler
    fun handle(exception: MethodArgumentNotValidException): ResponseEntity<ProblemDetail> =
        ResponseEntity.badRequest().body(
            ProblemDetail.forStatus(HttpStatus.BAD_REQUEST).also {
                it.title = "validation failed"
                it.detail = "the argument of the "
                it.setProperty("errors", exception.bindingResult.fieldErrors.map(::asValidationError))
            }
        )

    private fun asValidationError(error: FieldError) = ValidationError(
        message = error.defaultMessage ?: "no message",
        field = error.field,
        value = error.rejectedValue.toString()
    )
}

data class ValidationError(
    val field: String,
    val message: String,
    val value: String,
)