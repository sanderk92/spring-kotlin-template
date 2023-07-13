package com.example.config

import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.time.Instant

@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler
    fun handle(exception: MethodArgumentNotValidException): ResponseEntity<ValidationErrorResponse> {
        return ResponseEntity.badRequest().body(
            ValidationErrorResponse(
                time = Instant.now(),
                status = 400,
                errors = exception.bindingResult.fieldErrors.map(::asValidationError)
            )
        )
    }

    private fun asValidationError(it: FieldError) = ValidationError(
        description = it.defaultMessage ?: "no description",
        field = "${it.objectName}.${it.field}",
    )
}

data class ValidationErrorResponse(
    val time: Instant,
    val status: Int,
    val errors: List<ValidationError>,
)

data class ValidationError(
    val description: String,
    val field: String,
)