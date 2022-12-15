package com.example.auth.apikey

import com.example.auth.apikey.model.ApiKey
import com.example.auth.apikey.model.ApiKeyAuthorities
import com.example.auth.apikey.model.ApiKeyRequest
import org.springframework.stereotype.Service
import java.security.SecureRandom
import java.util.*
import java.util.Collections.unmodifiableList

interface ApiKeyService {
    fun createFrom(entry: ApiKeyRequest): ApiKey
    fun hash(apiKey: ApiKey): ApiKey
}

@Service
class SecureRandomApiKeyService(
    private val secureRandom: SecureRandom,
    private val hashGenerator: HashGenerator,
): ApiKeyService {
    private val apiKeyLength = 50
    private val characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"

    /**
     * Generate an [ApiKey] with a high entropy plain text key value.
     */
    override fun createFrom(entry: ApiKeyRequest): ApiKey = ApiKey(
        id = UUID.randomUUID().toString(),
        key = buildKey(),
        name = entry.name,
        authorities = authoritiesFrom(entry),
    )

    /**
     * Hash the key value of the given [ApiKey].
     */
    override fun hash(apiKey: ApiKey): ApiKey = apiKey.copy(
        key = hashGenerator.hash(apiKey.key)
    )

    private fun buildKey(): String {
        val apiKeyBuilder = StringBuilder()
        for (i in 0..apiKeyLength) {
            apiKeyBuilder.append(characters[secureRandom.nextInt(characters.length - 1)])
        }
        return apiKeyBuilder.toString()
    }

    private fun authoritiesFrom(model: ApiKeyRequest): List<String> {
        val authorities = mutableListOf<String>()
        if (model.read) {
            authorities.add(ApiKeyAuthorities.READ_AUTHORITY)
        }
        if (model.write) {
            authorities.add(ApiKeyAuthorities.WRITE_AUTHORITY)
        }
        if (model.delete) {
            authorities.add(ApiKeyAuthorities.DELETE_AUTHORITY)
        }
        return unmodifiableList(authorities)
    }
}
