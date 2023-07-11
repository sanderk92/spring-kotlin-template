package com.example.security.apikey

import com.example.config.SecuritySchemes.OIDC
import com.example.security.apikey.interfaces.ApiKey
import com.example.security.apikey.interfaces.UserService
import com.example.security.user.CurrentUser
import com.example.security.user.ExtractCurrentUser
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("\${spring.security.api-key.path}")
@Tag(name = "Apikeys", description = "Manage api keys for the current user")
class ApiKeyController(
    private val apiKeyService: ApiKeyService,
    private val userService: UserService
) {
    @GetMapping
    @ExtractCurrentUser
    @Operation(summary = "Get all API keys for the current user", security = [SecurityRequirement(name = OIDC)])
    fun getApiKeys(
        @Parameter(hidden = true) currentUser: CurrentUser,
    ): ResponseEntity<List<ApiKeyView>> =
        userService.findById(currentUser.id)
            ?.apiKeys
            ?.map(ApiKey::asView)
            ?.let { apiKeys -> ResponseEntity.ok(apiKeys) }
            ?: ResponseEntity.status(NOT_FOUND).build()

    @PostMapping
    @ExtractCurrentUser
    @Operation(summary = "Create a new API key for the current user", security = [SecurityRequirement(name = OIDC)])
    fun createApiKey(
        @Parameter(hidden = true) currentUser: CurrentUser,
        @RequestBody request: ApiKeyRequest,
    ): ResponseEntity<ApiKeyEntry> {
        val unHashedApiKeyEntry = apiKeyService.create(request)
        val hashedApiKeyEntry = apiKeyService.hash(unHashedApiKeyEntry)

        return userService.addApiKey(currentUser.id, hashedApiKeyEntry)
            ?.let { ResponseEntity.ok(unHashedApiKeyEntry) }
            ?: ResponseEntity.status(NOT_FOUND).build()
    }

    @DeleteMapping("/{id}")
    @ExtractCurrentUser
    @Operation(summary = "Delete an API key of the current user", security = [SecurityRequirement(name = OIDC)])
    fun deleteApiKey(
        @Parameter(hidden = true) currentUser: CurrentUser,
        @PathVariable id: UUID,
    ): ResponseEntity<Void> =
        userService.deleteApiKey(currentUser.id, id)
            .let { ResponseEntity.ok().build() }
}

data class ApiKeyView(
    val id: UUID,
    val name: String,
    val authorities: List<String>,
)

private fun ApiKey.asView() = ApiKeyView(
    id = id,
    name = name,
    authorities = authorities,
)
