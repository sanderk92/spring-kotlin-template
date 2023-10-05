package com.template.controller

import com.template.controller.interfaces.ApiKeyInterface
import com.template.security.apikey.ApiKey
import com.template.security.apikey.ApiKeyEntry
import com.template.security.apikey.ApiKeyService
import com.template.security.user.CurrentUser
import com.template.security.user.UserService
import jakarta.validation.constraints.NotBlank
import java.util.*
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class ApiKeyController(
    private val apiKeyService: ApiKeyService,
    private val userService: UserService
) : ApiKeyInterface {

    override fun getApiKeys(currentUser: CurrentUser): ResponseEntity<List<ApiKeyView>> =
        userService.findById(currentUser.id)
            ?.apiKeys
            ?.map(ApiKey::asView)
            ?.let { apiKeys -> ResponseEntity.ok(apiKeys) }
            ?: ResponseEntity.status(NOT_FOUND).build()

    override fun createApiKey(currentUser: CurrentUser, request: ApiKeyRequest): ResponseEntity<ApiKeyEntry> {
        val unHashedApiKey = apiKeyService.create(request)
        val hashedApiKey = apiKeyService.hash(unHashedApiKey)

        return userService.addApiKey(currentUser.id, hashedApiKey)
            ?.let { ResponseEntity.ok(unHashedApiKey) }
            ?: ResponseEntity.status(NOT_FOUND).build()
    }

    override fun deleteApiKey(currentUser: CurrentUser, id: UUID): ResponseEntity<Void> =
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
