package com.example.auth.apikey

import com.example.auth.apikey.model.ApiKey
import com.example.auth.apikey.model.ApiKeyRequest
import com.example.auth.apikey.model.ApiKeyUserService
import com.example.auth.apikey.model.ApiKeyView
import com.example.config.AuthSchemes
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.HttpStatus.FORBIDDEN
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/apikey")
class ApiKeyController(
    private val apiKeyService: ApiKeyService,
    private val userService: ApiKeyUserService
) {

    @Operation(
        description = "Get all API keys for the current user",
        security = [SecurityRequirement(name = AuthSchemes.OAUTH2), SecurityRequirement(name = AuthSchemes.APIKEY)]
    )
    @GetMapping
    fun getApiKeys(principal: Principal): ResponseEntity<List<ApiKeyView>> =
        userService.findById(principal.name)
            .map { user -> user.apiKeys.map(ApiKey::asView) }
            .map { apiKeys -> ResponseEntity.ok(apiKeys) }
            .orElse(ResponseEntity.status(FORBIDDEN).build())

    @Operation(
        description = "Add a new API key to the current user",
        security = [SecurityRequirement(name = AuthSchemes.OAUTH2), SecurityRequirement(name = AuthSchemes.APIKEY)]
    )
    @PostMapping
    fun createApiKey(principal: Principal, request: ApiKeyRequest): ResponseEntity<ApiKey> {
        val newApiKey = apiKeyService.createFrom(request)
        val hashedApiKey = apiKeyService.hash(newApiKey)

        return userService.addApiKey(principal.name, hashedApiKey)
            .map { ResponseEntity.ok(newApiKey) }
            .orElse(ResponseEntity.status(FORBIDDEN).build())
    }

    @Operation(
        description = "Delete an API key from the current user",
        security = [SecurityRequirement(name = AuthSchemes.OAUTH2), SecurityRequirement(name = AuthSchemes.APIKEY)]
    )
    @DeleteMapping("/{id}")
    fun deleteApiKey(principal: Principal, @PathVariable id: String): ResponseEntity<Void> =
        userService.deleteApiKey(principal.name, id)
            .let { ResponseEntity.ok().build() }
}

private fun ApiKey.asView() = ApiKeyView(
    id = id,
    name = name,
    authorities = authorities,
)
