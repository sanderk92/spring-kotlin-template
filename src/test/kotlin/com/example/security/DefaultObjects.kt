package com.example.security

import com.example.controller.ApiKeyRequest
import com.example.security.apikey.UnHashedApiKeyEntry
import com.example.security.apikey.ApiKey
import com.example.security.apikey.HashedApiKeyEntry
import com.example.security.user.User
import com.example.security.user.UserAuthority.*
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
    name = "apikeyName",
    read = true,
    write = true,
    delete = true,
)

val hashedApiKeyEntry = HashedApiKeyEntry(
    key = "apikey",
    name = "apikeyName",
    read = true,
    write = true,
    delete = true,
)

val apiKey = object : ApiKey {
    override val id = UUID.randomUUID()
    override val key = "key"
    override val name= "name"
    override val read = true
    override val write = true
    override val delete = true
}

val user = object : User {
    override val id = UUID.fromString(PRINCIPAL_NAME)
    override val email = "email"
    override val firstName = "firstName"
    override val lastName = "lastName"
    override val apiKeys = listOf(apiKey)
    override val authorities = listOf(READ, WRITE, DELETE, ADMIN)
}