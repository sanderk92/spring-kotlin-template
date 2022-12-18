package com.example.security.apikey

import com.example.config.SecuritySchemes
import com.example.security.apikey.model.ApiKey
import com.example.security.apikey.model.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("\${spring.security.api-key.path}")
@Tag(name = "API keys", description = "Manage api keys for the current user")
class ApiKeyController(
    private val apiKeyService: ApiKeyService,
    private val userService: UserService
) {
    @Operation(
        description = "Get all API keys for the current user",
        security = [SecurityRequirement(name = SecuritySchemes.OIDC)]
    )
    @GetMapping
    fun getApiKeys(authentication: Authentication): ResponseEntity<List<ApiKeyView>> =
        userService.findById(UUID.fromString(authentication.name))
            ?.apiKeys
            ?.map(ApiKey::asView)
            ?.let { apiKeys -> ResponseEntity.ok(apiKeys) }
            ?: ResponseEntity.status(NOT_FOUND).build()

    @Operation(
        description = "Add a new API key to the current user",
        security = [SecurityRequirement(name = SecuritySchemes.OIDC)]
    )
    @PostMapping
    fun createApiKey(authentication: Authentication, request: ApiKeyRequest): ResponseEntity<ApiKeyEntry> {
        val unHashedApiKeyEntry = apiKeyService.create(request)
        val hashedApiKeyEntry = apiKeyService.hash(unHashedApiKeyEntry)

        return userService.addApiKey(UUID.fromString(authentication.name), hashedApiKeyEntry)
            ?.let { ResponseEntity.ok(unHashedApiKeyEntry) }
            ?: ResponseEntity.status(NOT_FOUND).build()
    }

    @Operation(
        description = "Delete an API key from the current user",
        security = [SecurityRequirement(name = SecuritySchemes.OIDC)]
    )
    @DeleteMapping("/{id}")
    fun deleteApiKey(authentication: Authentication, @PathVariable id: String): ResponseEntity<Void> =
        userService.deleteApiKey(UUID.fromString(authentication.name), UUID.fromString(id))
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
