package com.template.controller.objects

import com.template.controller.interfaces.ApiKeyRequest
import com.template.config.security.apikey.ApiKey
import com.template.config.security.apikey.HashedApiKeyEntry
import com.template.config.security.apikey.UnHashedApiKeyEntry
import com.template.config.security.user.User
import com.template.config.security.user.UserAuthority.*
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

val apiKey = object : ApiKey {
    override val id = UUID.randomUUID()
    override val key = "key"
    override val name = "name"
    override val read = true
    override val write = true
    override val delete = true
}

val user = object : User {
    override val id = UUID.fromString(PRINCIPAL_NAME)
    override val email = "email"
    override val username = "username"
    override val firstName = "firstName"
    override val lastName = "lastName"
    override val apiKeys = listOf(apiKey)
    override val authorities = listOf(READ, WRITE, DELETE, ADMIN)
}
