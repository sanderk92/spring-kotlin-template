package com.template.controller

import com.template.config.security.user.Authority
import com.template.config.security.user.CurrentUser
import com.template.controller.interfaces.ApiKeyCreatedView
import com.template.controller.interfaces.ApiKeyInterface
import com.template.controller.interfaces.ApiKeyRequest
import com.template.controller.interfaces.ApiKeyView
import com.template.domain.ApiKeyService
import com.template.domain.UserService
import com.template.domain.model.ApiKey
import com.template.domain.model.ApiKeyCreated
import java.util.*
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class ApiKeyController(
    private val userService: UserService,
    private val apiKeyService: ApiKeyService,
) : ApiKeyInterface {

    override fun getApiKeys(currentUser: CurrentUser): ResponseEntity<List<ApiKeyView>> =
        userService.findById(currentUser.id)
            ?.apiKeys
            ?.map(ApiKey::toView)
            ?.let { apiKeys -> ResponseEntity.ok(apiKeys) }
            ?: ResponseEntity.status(NOT_FOUND).build()

    override fun createApiKey(currentUser: CurrentUser, request: ApiKeyRequest): ResponseEntity<ApiKeyCreatedView> =
        apiKeyService.createApiKey(currentUser.id, request.name, request.authorities())
            ?.let { ResponseEntity.ok(it.toView()) }
            ?: ResponseEntity.status(NOT_FOUND).build()

    override fun deleteApiKey(currentUser: CurrentUser, id: UUID): ResponseEntity<Void> =
        apiKeyService.deleteApiKey(currentUser.id, id)
            .let { ResponseEntity.ok().build() }
}

private fun ApiKeyRequest.authorities(): List<Authority> = listOfNotNull(
    if (read) Authority.READ else null,
    if (write) Authority.WRITE else null,
    if (delete) Authority.DELETE else null,
)

private fun ApiKey.toView() = ApiKeyView(
    id = id,
    name = name,
    authorities = authorities.map(Authority::toString),
)

private fun ApiKeyCreated.toView() = ApiKeyCreatedView(
    id = id,
    key = unHashedKey,
    name = name,
    authorities = authorities.map(Authority::toString),
)
