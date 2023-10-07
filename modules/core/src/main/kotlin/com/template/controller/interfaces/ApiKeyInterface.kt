package com.template.controller.interfaces

import com.template.config.SecuritySchemes
import com.template.controller.ApiKeyRequest
import com.template.controller.ApiKeyView
import com.template.controller.interfaces.ApiKeyInterface.Companion.ENDPOINT
import com.template.security.apikey.ApiKeyEntry
import com.template.security.user.CurrentUser
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import java.util.*
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@RequestMapping(ENDPOINT)
@Tag(name = "Keys", description = "Manage api keys for the current user")
interface ApiKeyInterface {

    companion object {
        const val ENDPOINT = "/keys"
    }

    @GetMapping
    @Operation(
        summary = "Get all api keys for the current user",
        description = "Not accessible by API key",
        security = [SecurityRequirement(name = SecuritySchemes.OIDC)]
    )
    fun getApiKeys(
        @Parameter(hidden = true) currentUser: CurrentUser,
    ): ResponseEntity<List<ApiKeyView>>

    @PostMapping
    @Operation(
        summary = "Create a new api key for the current user",
        description = "Not accessible by API key",
        security = [SecurityRequirement(name = SecuritySchemes.OIDC)]
    )
    fun createApiKey(
        @Parameter(hidden = true) currentUser: CurrentUser,
        @Valid @RequestBody @Parameter(description = "The details of the api key to create") request: ApiKeyRequest,
    ): ResponseEntity<ApiKeyEntry>

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete an api key of the current user",
        description = "Not accessible by API key",
        security = [SecurityRequirement(name = SecuritySchemes.OIDC)]
    )
    fun deleteApiKey(
        @Parameter(hidden = true) currentUser: CurrentUser,
        @PathVariable @Parameter(description = "The id of the api key to delete") id: UUID,
    ): ResponseEntity<Void>
}
