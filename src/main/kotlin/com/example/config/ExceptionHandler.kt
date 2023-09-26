package com.example.config

import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.ConstraintViolation
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ApiResponses
    fun handle(exception: ConstraintViolationException): ProblemDetail =
        ProblemDetail.forStatus(HttpStatus.BAD_REQUEST).also {
            it.title = "validation failed"
            it.detail = "constraint on input was violated"
            it.setProperty("errors", exception.constraintViolations.map(::validationErrors))
        }

    private fun validationErrors(error: ConstraintViolation<*>) = ValidationError(
        message = error.message ?: "no message",
        field = error.propertyPath.toString(),
        value = error.invalidValue.toString(),
    )

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handle(exception: MethodArgumentNotValidException): ProblemDetail =
        ProblemDetail.forStatus(HttpStatus.BAD_REQUEST).also {
            it.title = "validation failed"
            it.detail = "the argument of the "
            it.setProperty("errors", fieldErrors(exception) + globalErrors(exception))
        }

    private fun fieldErrors(exception: MethodArgumentNotValidException) =
        exception.bindingResult.fieldErrors.map { error ->
            ValidationError(
                message = error.defaultMessage ?: "no message",
                field = error.field,
                value = error.rejectedValue.toString(),
            )

        }

    private fun globalErrors(exception: MethodArgumentNotValidException) =
        exception.bindingResult.globalErrors.map { error ->
            ValidationError(
                message = error.defaultMessage ?: "no message",
                field = error.objectName,
                value = null,
            )
        }
}

data class ValidationError(
    val field: String,
    val message: String,
    val value: String?,
)
