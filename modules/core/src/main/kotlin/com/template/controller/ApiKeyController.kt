package com.template.controller

import com.template.config.security.apikey.ApiKey
import com.template.config.security.apikey.ApiKeyEntry
import com.template.config.security.apikey.ApiKeyService
import com.template.config.security.user.CurrentUser
import com.template.config.security.user.UserService
import com.template.controller.interfaces.ApiKeyRequest
import com.template.controller.interfaces.ApiKeyInterface
import com.template.controller.interfaces.ApiKeyView
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

private fun ApiKey.asView() = ApiKeyView(
    id = id,
    name = name,
    read = read,
    write = write,
    delete = delete,
)
