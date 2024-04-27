package com.template.domain.model

import com.template.config.security.apikey.SecureApiKey
import com.template.config.security.user.Authority
import java.util.UUID

internal data class ApiKey(
    val id: Id,
    val name: String,
    override val user: User,
    override val hashedKey: String,
    override val authorities: List<Authority>,
) : SecureApiKey {
    data class Id(val value: UUID)
}

internal data class ApiKeyCreated(
    val id: ApiKey.Id,
    val key: String,
    val name: String,
    val authorities: List<Authority>,
)
