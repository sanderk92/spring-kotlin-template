package com.template.domain.model

import com.template.config.security.apikey.SecureApiKey
import com.template.config.security.user.Authority
import java.util.*

data class ApiKey(
    val id: UUID,
    val name: String,
    override val hashedKey: String,
    override val authorities: List<Authority>
) : SecureApiKey

data class ApiKeyCreated(
    val id: UUID,
    val name: String,
    val unHashedKey: String,
    val authorities: List<Authority>,
)
