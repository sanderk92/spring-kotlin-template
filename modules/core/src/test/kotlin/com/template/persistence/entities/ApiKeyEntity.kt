package com.template.persistence.entities

import com.template.config.security.user.Authority.*
import com.template.persistence.entity.ApiKeyEntity
import java.util.*

internal val apiKeyEntity: ApiKeyEntity = ApiKeyEntity(
    id = UUID.randomUUID(),
    name = "name",
    hashedKey = "hashedKey",
    owner = userEntity,
    authorities = listOf(READ, WRITE, DELETE, ADMIN)
)
