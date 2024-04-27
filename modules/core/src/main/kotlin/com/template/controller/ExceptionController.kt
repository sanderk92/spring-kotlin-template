package com.template.controller

import com.template.controller.interfaces.ExceptionInterface
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.validation.ConstraintViolation
import jakarta.validation.ConstraintViolationException
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.multipart.MaxUploadSizeExceededException

val log = KotlinLogging.logger {}

@ControllerAdvice
internal class ExceptionController(
    @Value("\${spring.servlet.multipart.max-file-size}") private val maxMultipartSize: String,
) : ExceptionInterface {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handle(exception: Throwable): ProblemDetail {
        log.error(exception) { "Unexpected exception" }
        return ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR).apply {
            title = "internal server error"
            detail = "an unexpected error occurred"
        }
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    fun handle(exception: ConstraintViolationException): ProblemDetail =
        ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY).apply {
            title = "validation failed"
            detail = "constraint on input was violated"
            setProperty("errors", exception.constraintViolations.map(::constraintError))
        }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    fun handle(exception: MethodArgumentNotValidException): ProblemDetail =
        ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY).apply {
            title = "validation failed"
            detail = "constraint on input was violated"
            setProperty("errors", fieldErrors(exception) + globalErrors(exception))
        }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    fun handle(exception: AccessDeniedException): ProblemDetail =
        ProblemDetail.forStatus(HttpStatus.FORBIDDEN).apply {
            title = "access denied"
            detail = exception.message
        }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handle(exception: NoSuchElementException): ProblemDetail =
        ProblemDetail.forStatus(HttpStatus.NOT_FOUND).apply {
            title = "resource not found"
            detail = exception.message
        }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handle(exception: HttpMessageNotReadableException): ProblemDetail =
        ProblemDetail.forStatus(HttpStatus.BAD_REQUEST).apply {
            title = "invalid request"
            detail = "the body of the request was malformed or missing data"
        }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handle(exception: MaxUploadSizeExceededException): ProblemDetail =
        ProblemDetail.forStatus(HttpStatus.BAD_REQUEST).apply {
            title = "invalid request"
            detail = "multipart exceeds the maximum file size of $maxMultipartSize"
        }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handle(exception: IllegalStateException): ProblemDetail {
        log.error(exception) { "Invalid state" }
        return ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR).apply {
            title = "illegal state exception"
            detail = exception.message
        }
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handle(exception: IllegalArgumentException): ProblemDetail {
        return ProblemDetail.forStatus(HttpStatus.BAD_REQUEST).apply {
            title = "illegal argument exception"
            detail = exception.message
        }
    }

    private fun constraintError(error: ConstraintViolation<*>) =
        ValidationError(
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
