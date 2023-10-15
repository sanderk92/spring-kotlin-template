package com.template.controller.interfaces

import com.template.controller.ValidationError
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.MediaType

private const val mediaType: String = MediaType.APPLICATION_JSON_VALUE

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
        ),
        ApiResponse(
            responseCode = "422",
            description = "Input validation error",
            content = [
                Content(
                    mediaType = mediaType,
                    schema = Schema(implementation = ValidationExceptionModel::class)
                )
            ]
        ),
        ApiResponse(
            responseCode = "404",
            description = "Not found error",
            content = [
                Content(
                    mediaType = mediaType,
                    schema = Schema(implementation = ExceptionModel::class)
                )
            ]
        ),
        ApiResponse(
            responseCode = "403",
            description = "Access denied error",
            content = [
                Content(
                    mediaType = mediaType,
                    schema = Schema(implementation = ExceptionModel::class)
                )
            ]
        ),
        ApiResponse(
            responseCode = "400",
            description = "Input formatting error",
            content = [
                Content(
                    mediaType = mediaType,
                    schema = Schema(implementation = ExceptionModel::class)
                )
            ]
        ),
    ]
)
internal interface ExceptionInterface

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
