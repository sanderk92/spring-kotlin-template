package com.template.persistence.entities

import com.template.domain.models.PRINCIPAL_NAME
import com.template.persistence.entity.UserEntity
import java.util.UUID

internal val userEntity: UserEntity =
    UserEntity(
        id = UUID.fromString(PRINCIPAL_NAME),
        email = "email",
        username = "username",
        firstName = "firstName",
        lastName = "lastName",
        apiKeys = listOf(),
    )
