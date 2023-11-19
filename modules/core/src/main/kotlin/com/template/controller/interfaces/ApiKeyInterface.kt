package com.template.controller.interfaces

import com.template.config.SecuritySchemes
import com.template.config.security.user.CurrentUser
import com.template.controller.interfaces.ApiKeyInterface.Companion.ENDPOINT
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import java.util.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RequestMapping(ENDPOINT)
@Tag(name = "Keys", description = "Manage api keys for the current user")
internal interface ApiKeyInterface {

    companion object {
        const val ENDPOINT = "/v1/keys"
    }

    @GetMapping
    @Operation(
        summary = "Get all api keys for the current user",
        description = "Not accessible by API key",
        security = [SecurityRequirement(name = SecuritySchemes.OIDC)]
    )
    fun getApiKeys(
        @Parameter(hidden = true) currentUser: CurrentUser,
    ): ResponseEntity<List<ApiKeyDto>>

    @PostMapping
    @Operation(
        summary = "Create a new api key for the current user",
        description = "Not accessible by API key",
        security = [SecurityRequirement(name = SecuritySchemes.OIDC)]
    )
    fun createApiKey(
        @Parameter(hidden = true) currentUser: CurrentUser,
        @Parameter(description = "The api key to create") @Valid @RequestBody
        request: ApiKeyRequest,
    ): ResponseEntity<ApiKeyCreatedDto>

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete an api key of the current user",
        description = "Not accessible by API key",
        security = [SecurityRequirement(name = SecuritySchemes.OIDC)]
    )
    fun deleteApiKey(
        @Parameter(hidden = true) currentUser: CurrentUser,
        @Parameter(description = "The id of the api key to delete") @PathVariable
        id: UUID,
    ): ResponseEntity<Void>
}

internal data class ApiKeyRequest(
    @field:NotBlank
    val name: String,
    val read: Boolean,
    val write: Boolean,
    val delete: Boolean,
)

internal data class ApiKeyDto(
    val id: UUID,
    val name: String,
    val authorities: List<String>,
)

internal data class ApiKeyCreatedDto(
    val id: UUID,
    val key: String,
    val name: String,
    val authorities: List<String>,
)
