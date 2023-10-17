package com.template.persistence.entities

import com.template.domain.models.apiKey
import com.template.persistence.entity.ApiKeyEntity

internal val apiKeyEntity: ApiKeyEntity = ApiKeyEntity(
    name = apiKey.name,
    hashedKey = apiKey.hashedKey,
    owner = userEntity,
    authorities = apiKey.authorities
)
