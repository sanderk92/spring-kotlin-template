package com.template.controller

import com.template.config.security.user.Authority
import com.template.config.security.user.CurrentUser
import com.template.controller.interfaces.ApiKeyCreatedDto
import com.template.controller.interfaces.ApiKeyDto
import com.template.controller.interfaces.ApiKeyInterface
import com.template.controller.interfaces.ApiKeyRequest
import com.template.domain.ApiKeyService
import com.template.domain.UserService
import com.template.mappers.ApiKeyMapper
import java.util.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
internal class ApiKeyController(
    private val userService: UserService,
    private val apiKeyService: ApiKeyService,
    private val apiKeyMapper: ApiKeyMapper,
) : ApiKeyInterface {

    override fun getApiKeys(currentUser: CurrentUser): ResponseEntity<List<ApiKeyDto>> =
        userService.findById(currentUser.id)?.apiKeys
            ?.map(apiKeyMapper::toApiKeyDto)
            ?.let { apiKeys -> ResponseEntity.ok(apiKeys) }
            ?: ResponseEntity.notFound().build()

    override fun createApiKey(currentUser: CurrentUser, request: ApiKeyRequest): ResponseEntity<ApiKeyCreatedDto> =
        apiKeyService.createApiKey(currentUser.id, request.name, request.authorities())
            ?.let(apiKeyMapper::toApiKeyCreatedDto)
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()

    override fun deleteApiKey(currentUser: CurrentUser, id: UUID): ResponseEntity<Void> =
        apiKeyService.deleteApiKey(currentUser.id, id)
            .let { ResponseEntity.ok().build() }
}

private fun ApiKeyRequest.authorities(): List<Authority> = listOfNotNull(
    if (read) Authority.READ else null,
    if (write) Authority.WRITE else null,
    if (delete) Authority.DELETE else null,
)
