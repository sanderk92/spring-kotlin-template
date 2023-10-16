package com.template.persistence.objects

import com.template.config.security.user.Authority.*
import com.template.persistence.entity.ApiKeyEntity

internal val apiKeyEntity: ApiKeyEntity = ApiKeyEntity(
    name = "name",
    hashedKey = "hashedKey",
    owner = null,
    authorities = listOf(READ, WRITE, DELETE)
)
