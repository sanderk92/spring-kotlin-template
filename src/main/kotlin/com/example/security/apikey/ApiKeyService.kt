package com.example.security.apikey

import jakarta.validation.constraints.NotBlank
import org.springframework.stereotype.Service
import java.security.SecureRandom
import java.util.Collections.unmodifiableList

@Service
class ApiKeyService(
    private val secureRandom: SecureRandom,
    private val hashGenerator: HashGenerator,
) {
    private val apiKeyLength = 50
    private val characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"

    fun create(request: ApiKeyRequest) = UnHashedApiKeyEntry(
        name = request.name,
        authorities = authoritiesFrom(request),
        key = buildKey()
    )

    fun hash(entry: UnHashedApiKeyEntry) = HashedApiKeyEntry(
        name = entry.name,
        authorities = entry.authorities,
        key = hashGenerator.hash(entry.key),
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
    @field:NotBlank
    val name: String,
    val read: Boolean,
    val write: Boolean,
    val delete: Boolean,
)

sealed interface ApiKeyEntry {
    val key: String
    val name: String
    val authorities: List<String>
}

data class HashedApiKeyEntry(
    override val key: String,
    override val name: String,
    override val authorities: List<String>,
) : ApiKeyEntry

data class UnHashedApiKeyEntry(
    override val key: String,
    override val name: String,
    override val authorities: List<String>,
) : ApiKeyEntry
