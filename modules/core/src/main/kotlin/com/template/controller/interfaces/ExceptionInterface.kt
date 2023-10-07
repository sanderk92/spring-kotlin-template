package com.template.controller.interfaces

import com.template.controller.ValidationError
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.ConstraintViolationException
import org.springframework.http.MediaType
import org.springframework.http.ProblemDetail
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.bind.MethodArgumentNotValidException

interface ExceptionInterface {

    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = [
                    Content(
                        mediaType = mediaType,
                        schema = Schema(implementation = ExceptionModel::class)
                    )
                ]
            )
        ]
    )
    fun handle(exception: Throwable): ProblemDetail

    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "422",
                description = "Input validation failed",
                content = [
                    Content(
                        mediaType = mediaType,
                        schema = Schema(implementation = ValidationExceptionModel::class)
                    )
                ]
            )
        ]
    )
    fun handle(exception: ConstraintViolationException): ProblemDetail

    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "422",
                description = "Input validation failed",
                content = [
                    Content(
                        mediaType = mediaType,
                        schema = Schema(implementation = ValidationExceptionModel::class)
                    )
                ]
            )
        ]
    )
    fun handle(exception: MethodArgumentNotValidException): ProblemDetail

    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "403",
                description = "Access denied",
                content = [
                    Content(
                        mediaType = mediaType,
                        schema = Schema(implementation = ExceptionModel::class)
                    )
                ]
            )
        ]
    )
    fun handle(exception: AccessDeniedException): ProblemDetail

    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "400",
                description = "Input parsing failed",
                content = [
                    Content(
                        mediaType = mediaType,
                        schema = Schema(implementation = ExceptionModel::class)
                    )
                ]
            )
        ]
    )
    fun handle(exception: HttpMessageNotReadableException): ProblemDetail
}

private const val mediaType: String = MediaType.APPLICATION_JSON_VALUE

private interface ExceptionModel {
    val type: String
    val title: String
    val status: Int
    val detail: String
    val instance: String
}

private interface ValidationExceptionModel : ExceptionModel {
    val errors: List<ValidationError>
}
