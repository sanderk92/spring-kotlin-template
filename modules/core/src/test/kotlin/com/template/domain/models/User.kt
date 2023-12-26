package com.template.domain.models

import com.template.domain.model.User
import com.template.persistence.entities.userEntity

const val PRINCIPAL_NAME = "ef2db5bb-b7a0-4ff3-99dc-a7d95dc1e84c"

internal val user =
    User(
        id = userEntity.id,
        email = userEntity.email,
        username = userEntity.username,
        firstName = userEntity.firstName,
        lastName = userEntity.lastName,
        apiKeys = listOf(apiKey),
    )
