package com.template.controller.objects

import com.template.config.security.user.Authority.*
import com.template.controller.interfaces.ApiKeyRequest
import com.template.domain.model.ApiKey
import com.template.domain.model.ApiKeyCreated
import com.template.domain.model.User
import java.util.*

const val PRINCIPAL_NAME = "ef2db5bb-b7a0-4ff3-99dc-a7d95dc1e84c"

val apiKeyRequest = ApiKeyRequest(
    name = "keyName",
    read = true,
    write = true,
    delete = true,
)

val apiKeyCreated = ApiKeyCreated(
    id = UUID.randomUUID(),
    name = "name",
    unHashedKey = "unHashedKey",
    authorities = listOf(READ, WRITE, DELETE)
)


val apiKey = ApiKey(
    id = UUID.randomUUID(),
    name = "name",
    hashedKey = "hashedKey",
    authorities = listOf(READ, WRITE, DELETE)
)

val user = User(
    id = UUID.fromString(PRINCIPAL_NAME),
    email = "email",
    username = "username",
    firstName = "firstName",
    lastName = "lastName",
    apiKeys = listOf(apiKey),
    authorities = listOf(READ, WRITE, DELETE, ADMIN),
)
