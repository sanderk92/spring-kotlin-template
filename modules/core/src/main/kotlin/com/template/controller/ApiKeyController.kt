package com.template.controller

import com.template.config.security.user.Authority
import com.template.config.security.user.CurrentUser
import com.template.controller.interfaces.ApiKeyCreatedDto
import com.template.controller.interfaces.ApiKeyDto
import com.template.controller.interfaces.ApiKeyInterface
import com.template.controller.interfaces.ApiKeyRequest
import com.template.domain.ApiKeyService
import com.template.domain.model.ApiKey
import com.template.domain.model.User
import com.template.mappers.ApiKeyMapper
import java.util.UUID
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
internal class ApiKeyController(
    private val apiKeyService: ApiKeyService,
    private val apiKeyMapper: ApiKeyMapper,
) : ApiKeyInterface {
    override fun retrieveApiKeys(currentUser: CurrentUser): ResponseEntity<List<ApiKeyDto>> =
        apiKeyService.findByUserId(User.Id(currentUser.id))
            .map(apiKeyMapper::toApiKeyDto)
            .let { apiKeys -> ResponseEntity.ok(apiKeys) }

    override fun createApiKey(currentUser: CurrentUser, request: ApiKeyRequest): ResponseEntity<ApiKeyCreatedDto> =
        apiKeyService.create(User.Id(currentUser.id), request.name, request.authorities())
            ?.let(apiKeyMapper::toApiKeyCreatedDto)
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()

    override fun deleteApiKey(currentUser: CurrentUser, id: UUID): ResponseEntity<Void> =
        apiKeyService.delete(User.Id(currentUser.id), ApiKey.Id(id))
            .let { ResponseEntity.noContent().build() }
}

private fun ApiKeyRequest.authorities(): Set<Authority> =
    setOfNotNull(
        if (read) Authority.READ else null,
        if (write) Authority.WRITE else null,
        if (delete) Authority.DELETE else null,
    )
