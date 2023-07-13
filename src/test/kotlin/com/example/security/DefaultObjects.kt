package com.example.security

import com.example.security.apikey.ApiKeyAuthorities.DELETE
import com.example.security.apikey.ApiKeyAuthorities.READ
import com.example.security.apikey.ApiKeyAuthorities.WRITE
import com.example.security.apikey.ApiKeyRequest
import com.example.security.apikey.UnHashedApiKeyEntry
import com.example.security.apikey.ApiKey
import com.example.security.apikey.HashedApiKeyEntry
import com.example.security.user.User
import java.util.*

const val PRINCIPAL_NAME = "ef2db5bb-b7a0-4ff3-99dc-a7d95dc1e84c"

val apiKeyRequest = ApiKeyRequest(
    name = "apikey name",
    read = true,
    write = true,
    delete = true,
)

val unHashedApiKeyEntry = UnHashedApiKeyEntry(
    key = "apikey",
    name = "apikey name",
    authorities = emptyList()
)

val hashedApiKeyEntry = HashedApiKeyEntry(
    key = "apikey",
    name = "apikey name",
    authorities = emptyList()
)

val apiKey = object : ApiKey {
    override val id = UUID.randomUUID()
    override val key = "key"
    override val name= "name"
    override val authorities = listOf(READ, WRITE, DELETE)
}

val user = object : User {
    override val id = UUID.fromString(PRINCIPAL_NAME)
    override val apiKeys = listOf(apiKey)
}