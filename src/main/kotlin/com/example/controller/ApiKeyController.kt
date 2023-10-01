package com.example.controller

import com.example.controller.docs.ApiKeyInterface
import com.example.security.apikey.ApiKey
import com.example.security.apikey.ApiKeyEntry
import com.example.security.apikey.ApiKeyService
import com.example.security.user.CurrentUser
import com.example.security.user.ExtractCurrentUser
import com.example.security.user.UserService
import jakarta.validation.constraints.NotBlank
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("\${spring.security.api-key.path}")
class ApiKeyController(
    private val apiKeyService: ApiKeyService,
    private val userService: UserService
) : ApiKeyInterface {

    @GetMapping
    @ExtractCurrentUser
    override fun getApiKeys(currentUser: CurrentUser): ResponseEntity<List<ApiKeyView>> =
        userService.findById(currentUser.id)
            ?.apiKeys
            ?.map(ApiKey::asView)
            ?.let { apiKeys -> ResponseEntity.ok(apiKeys) }
            ?: ResponseEntity.status(NOT_FOUND).build()

    @PostMapping
    @ExtractCurrentUser
    override fun createApiKey(currentUser: CurrentUser, @RequestBody request: ApiKeyRequest): ResponseEntity<ApiKeyEntry> {
        val unHashedApiKey = apiKeyService.create(request)
        val hashedApiKey = apiKeyService.hash(unHashedApiKey)

        return userService.addApiKey(currentUser.id, hashedApiKey)
            ?.let { ResponseEntity.ok(unHashedApiKey) }
            ?: ResponseEntity.status(NOT_FOUND).build()
    }

    @DeleteMapping("/{id}")
    @ExtractCurrentUser
    override fun deleteApiKey(currentUser: CurrentUser, @PathVariable id: UUID): ResponseEntity<Void> =
        userService.deleteApiKey(currentUser.id, id)
            .let { ResponseEntity.ok().build() }
}

data class ApiKeyRequest(
    @field:NotBlank
    val name: String,
    val read: Boolean,
    val write: Boolean,
    val delete: Boolean,
)

data class ApiKeyView(
    val id: UUID,
    val name: String,
    val read: Boolean,
    val write: Boolean,
    val delete: Boolean,
)

private fun ApiKey.asView() = ApiKeyView(
    id = id,
    name = name,
    read = read,
    write = write,
    delete = delete,
)
