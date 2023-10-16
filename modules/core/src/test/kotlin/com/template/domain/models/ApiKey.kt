package com.template.domain.models

import com.template.config.security.user.Authority
import com.template.controller.interfaces.ApiKeyRequest
import com.template.domain.model.ApiKey
import com.template.domain.model.ApiKeyCreated
import java.util.*

internal val apiKeyRequest = ApiKeyRequest(
    name = "keyName",
    read = true,
    write = true,
    delete = true,
)

internal val apiKeyCreated = ApiKeyCreated(
    id = UUID(0, 0),
    name = "name",
    key = "unHashedKey",
    authorities = listOf(Authority.READ, Authority.WRITE, Authority.DELETE)
)

internal val apiKey = ApiKey(
    id = UUID(0, 0),
    name = "name",
    hashedKey = "hashedKey",
    authorities = listOf(Authority.READ, Authority.WRITE, Authority.DELETE)
)
