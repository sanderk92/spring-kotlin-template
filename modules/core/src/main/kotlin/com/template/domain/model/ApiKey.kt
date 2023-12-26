package com.template.domain.model

import com.template.config.security.apikey.SecureApiKey
import com.template.config.security.user.Authority
import java.util.UUID

internal data class ApiKey(
    val id: UUID,
    val name: String,
    override val hashedKey: String,
    override val authorities: List<Authority>,
) : SecureApiKey

internal data class ApiKeyCreated(
    val id: UUID,
    val key: String,
    val name: String,
    val authorities: List<Authority>,
)
