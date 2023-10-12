package com.template.controller.objects

import com.template.config.security.apikey.HashedApiKeyEntry
import com.template.config.security.apikey.UnHashedApiKeyEntry
import com.template.config.security.user.UserAuthority.*
import com.template.controller.interfaces.ApiKeyRequest
import com.template.domain.model.ApiKey
import com.template.domain.model.User
import java.util.*

const val PRINCIPAL_NAME = "ef2db5bb-b7a0-4ff3-99dc-a7d95dc1e84c"

val apiKeyRequest = ApiKeyRequest(
    name = "keyName",
    read = true,
    write = true,
    delete = true,
)

val unHashedApiKeyEntry = UnHashedApiKeyEntry(
    key = "unHashedKey",
    name = "keyName",
    read = true,
    write = true,
    delete = true,
)

val hashedApiKeyEntry = HashedApiKeyEntry(
    key = "hashedKey",
    name = "keyName",
    read = true,
    write = true,
    delete = true,
)

val apiKey = ApiKey(
    id = UUID.randomUUID(),
    key = "key",
    name = "name",
    read = true,
    write = true,
    delete = true,
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
