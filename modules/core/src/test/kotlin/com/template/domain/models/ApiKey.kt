package com.template.domain.models

import com.template.controller.interfaces.ApiKeyRequest
import com.template.domain.model.ApiKey
import com.template.domain.model.ApiKeyCreated
import com.template.persistence.entities.apiKeyEntity

internal val apiKeyRequest =
    ApiKeyRequest(
        name = apiKeyEntity.name,
        read = true,
        write = true,
        delete = true,
    )

internal val apiKeyCreated =
    ApiKeyCreated(
        id = apiKeyEntity.id,
        name = apiKeyEntity.name,
        key = "unHashedKey",
        authorities = apiKeyEntity.authorities,
    )

internal val apiKey =
    ApiKey(
        id = apiKeyEntity.id,
        name = apiKeyEntity.name,
        hashedKey = apiKeyEntity.hashedKey,
        authorities = apiKeyEntity.authorities,
    )
