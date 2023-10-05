package com.template.controller.docs

import com.template.config.SecuritySchemes
import com.template.controller.ApiKeyRequest
import com.template.controller.ApiKeyView
import com.template.security.apikey.ApiKeyEntry
import com.template.security.user.CurrentUser
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import java.util.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable

@Tag(name = "Keys", description = "Manage api keys for the current user")
interface ApiKeyInterface {

    @Operation(
        summary = "Get all api keys for the current user",
        description = "Not accessible by API key",
        security = [SecurityRequirement(name = SecuritySchemes.OIDC)]
    )
    fun getApiKeys(
        @Parameter(hidden = true) currentUser: CurrentUser,
    ): ResponseEntity<List<ApiKeyView>>

    @Operation(
        summary = "Create a new api key for the current user",
        description = "Not accessible by API key",
        security = [SecurityRequirement(name = SecuritySchemes.OIDC)]
    )
    fun createApiKey(
        @Parameter(hidden = true) currentUser: CurrentUser,
        @Valid @Parameter(description = "The details of the api key to create")
        request: ApiKeyRequest,
    ): ResponseEntity<ApiKeyEntry>

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
