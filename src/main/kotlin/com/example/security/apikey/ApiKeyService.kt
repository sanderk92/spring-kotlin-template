package com.example.security.apikey

import com.example.security.apikey.model.ApiKeyAuthorities
import org.springframework.stereotype.Service
import java.security.SecureRandom
import java.util.Collections.unmodifiableList

@Service
class ApiKeyService(
    @Suppress("SpringJavaInjectionPointsAutowiringInspection")
    private val secureRandom: SecureRandom = SecureRandom(),
    private val hashGenerator: HashGenerator,
) {
    private val apiKeyLength = 50
    private val characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"

    fun create(request: ApiKeyRequest) = ApiKeyEntry(
        name = request.name,
        key = UnHashedApiKeyString(buildKey()),
        authorities = authoritiesFrom(request)
    )

    fun hash(entry: ApiKeyEntry) = entry.copy(
        key = HashedApiKeyString(hashGenerator.hash(entry.key.value))
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
            authorities.add(ApiKeyAuthorities.READ)
        }
        if (model.write) {
            authorities.add(ApiKeyAuthorities.WRITE)
        }
        if (model.delete) {
            authorities.add(ApiKeyAuthorities.DELETE)
        }
        return unmodifiableList(authorities)
    }
}

data class ApiKeyRequest(
    val name: String,
    val read: Boolean,
    val write: Boolean,
    val delete: Boolean,
)

data class ApiKeyEntry(
    val key: ApiKeyString,
    val name: String,
    val authorities: List<String>,
)

sealed interface ApiKeyString {
    val value: String
}

data class HashedApiKeyString(override val value: String) : ApiKeyString
data class UnHashedApiKeyString(override val value: String) : ApiKeyString
