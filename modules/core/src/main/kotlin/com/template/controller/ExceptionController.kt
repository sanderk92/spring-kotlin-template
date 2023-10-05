package com.template.controller

import com.template.controller.interfaces.ExceptionInterface
import jakarta.validation.ConstraintViolation
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class ExceptionController : ExceptionInterface {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    override fun handle(exception: Throwable): ProblemDetail {
        exception.printStackTrace()
        return ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR).also {
            it.title = "internal server error"
            it.detail = "an unexpected error occurred"
        }
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    override fun handle(exception: ConstraintViolationException): ProblemDetail =
        ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY).also {
            it.title = "validation failed"
            it.detail = "constraint on input was violated"
            it.setProperty("errors", exception.constraintViolations.map(::constraintError))
        }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    override fun handle(exception: MethodArgumentNotValidException): ProblemDetail =
        ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY).also {
            it.title = "validation failed"
            it.detail = "constraint on input was violated"
            it.setProperty("errors", fieldErrors(exception) + globalErrors(exception))
        }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    override fun handle(exception: AccessDeniedException): ProblemDetail =
        ProblemDetail.forStatus(HttpStatus.FORBIDDEN).also {
            it.title = "access denied"
            it.detail = "the current user has no access to this resource"
        }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    override fun handle(exception: HttpMessageNotReadableException): ProblemDetail =
        ProblemDetail.forStatus(HttpStatus.BAD_REQUEST).also {
            it.title = "invalid request"
            it.detail = "the body of the request was malformed or missing data"
        }

    private fun constraintError(error: ConstraintViolation<*>) = ValidationError(
        message = error.message ?: "no message",
        field = error.propertyPath.toString(),
        value = error.invalidValue.toString(),
    )

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
