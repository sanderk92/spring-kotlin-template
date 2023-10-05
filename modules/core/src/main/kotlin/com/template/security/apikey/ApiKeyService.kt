package com.template.security.apikey

import com.template.controller.ApiKeyRequest
import java.security.SecureRandom
import org.springframework.stereotype.Service

@Service
class ApiKeyService(private val hashGenerator: HashGenerator) {
    private val apiKeyLength = 50
    private val secureRandom = SecureRandom()
    private val characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"

    fun create(request: ApiKeyRequest) = UnHashedApiKeyEntry(
        key = buildKey(),
        name = request.name,
        read = request.read,
        write = request.write,
        delete = request.delete,
    )

    fun hash(entry: UnHashedApiKeyEntry) = HashedApiKeyEntry(
        key = hashGenerator.hash(entry.key),
        name = entry.name,
        read = entry.read,
        write = entry.write,
        delete = entry.delete,
    )

    private fun buildKey(): String {
        val apiKeyBuilder = StringBuilder()
        for (i in 0..apiKeyLength) {
            apiKeyBuilder.append(characters[secureRandom.nextInt(characters.length - 1)])
        }
        return apiKeyBuilder.toString()
    }
}

sealed interface ApiKeyEntry {
    val key: String
    val name: String
    val read: Boolean
    val write: Boolean
    val delete: Boolean
}

data class HashedApiKeyEntry(
    override val key: String,
    override val name: String,
    override val read: Boolean,
    override val write: Boolean,
    override val delete: Boolean,
) : ApiKeyEntry

data class UnHashedApiKeyEntry(
    override val key: String,
    override val name: String,
    override val read: Boolean,
    override val write: Boolean,
    override val delete: Boolean,
) : ApiKeyEntry
