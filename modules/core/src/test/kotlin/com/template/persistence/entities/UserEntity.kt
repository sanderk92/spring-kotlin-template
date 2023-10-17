package com.template.persistence.entities

import com.template.domain.models.user
import com.template.persistence.entity.UserEntity

internal val userEntity: UserEntity = UserEntity(
    id = user.id,
    email = user.email,
    username = user.username,
    firstName = user.firstName,
    lastName = user.lastName,
    apiKeys = listOf(),
)
