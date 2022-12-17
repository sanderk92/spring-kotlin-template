package com.example.security.apikey

import com.example.config.AuthSchemes
import com.example.security.apikey.model.ApiKeyEntity
import com.example.security.apikey.model.UserEntityService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus.FORBIDDEN
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.*

@RestController
@RequestMapping("\${spring.security.api-key.path}")
@Tag(name = "API keys", description = "Manage api keys for the current user")
class ApiKeyController(
    private val apiKeyService: ApiKeyService,
    private val userService: UserEntityService
) {
    @Operation(
        description = "Get all API keys for the current user",
        security = [SecurityRequirement(name = AuthSchemes.OAUTH2)]
    )
    @GetMapping
    fun getApiKeys(principal: Principal): ResponseEntity<List<ApiKeyView>> =
        userService.findById(UUID.fromString(principal.name))?.apiKeys
            ?.map(ApiKeyEntity::asView)
            ?.let { apiKeys -> ResponseEntity.ok(apiKeys) }
            ?: ResponseEntity.status(FORBIDDEN).build()

    @Operation(
        description = "Add a new API key to the current user",
        security = [SecurityRequirement(name = AuthSchemes.OAUTH2)]
    )
    @PostMapping
    fun createApiKey(principal: Principal, request: ApiKeyRequest): ResponseEntity<ApiKeyEntry> {
        val unHashedApiKeyEntry = apiKeyService.create(request)
        val hashedApiKeyEntry = apiKeyService.hash(unHashedApiKeyEntry)

        return userService.addApiKey(UUID.fromString(principal.name), hashedApiKeyEntry)
            ?.let { ResponseEntity.ok(unHashedApiKeyEntry) }
            ?: ResponseEntity.status(FORBIDDEN).build()
    }

    @Operation(
        description = "Delete an API key from the current user",
        security = [SecurityRequirement(name = AuthSchemes.OAUTH2)]
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("true")
    fun deleteApiKey(principal: Principal, @PathVariable id: String): ResponseEntity<Void> =
        userService.deleteApiKey(UUID.fromString(principal.name), UUID.fromString(id))
            .let { ResponseEntity.ok().build() }
}

data class ApiKeyView(
    val id: UUID,
    val name: String,
    val authorities: List<String>,
)

private fun ApiKeyEntity.asView() = ApiKeyView(
    id = id,
    name = name,
    authorities = authorities,
)
