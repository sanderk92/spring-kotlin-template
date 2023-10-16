package com.template.persistence.objects

import com.template.domain.objects.PRINCIPAL_NAME
import com.template.persistence.entity.UserEntity
import java.util.*

internal val userEntity: UserEntity = UserEntity(
    id = UUID.fromString(PRINCIPAL_NAME),
    email = "email",
    username = "username",
    firstName = "firstName",
    lastName = "lastName",
    apiKeys = listOf(),
)
