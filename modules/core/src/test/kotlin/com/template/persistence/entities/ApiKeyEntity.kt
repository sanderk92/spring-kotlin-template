package com.template.persistence.entities

import com.template.config.security.user.Authority.ADMIN
import com.template.config.security.user.Authority.DELETE
import com.template.config.security.user.Authority.READ
import com.template.config.security.user.Authority.WRITE
import com.template.persistence.entity.ApiKeyEntity
import java.util.UUID

internal val apiKeyEntity: ApiKeyEntity =
    ApiKeyEntity(
        id = UUID.randomUUID(),
        name = "name",
        hashedKey = "hashedKey",
        owner = userEntity,
        authorities = listOf(READ, WRITE, DELETE, ADMIN),
    )
