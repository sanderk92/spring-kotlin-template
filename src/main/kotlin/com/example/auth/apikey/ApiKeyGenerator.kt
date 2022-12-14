package com.example.auth.apikey

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Service
import java.security.SecureRandom
import java.util.*
import java.util.Collections.unmodifiableList

interface ApiKeyGenerator {
    fun generate(entry: ApiKeyEntry): ApiKey
}

@Service
class SecureRandomApiKeyGenerator(
    private val secureRandom: SecureRandom = SecureRandom()
): ApiKeyGenerator {
    private val apiKeyLength = 50
    private val characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"

    /**
     * Generate an [ApiKey] with a high entropy plain text key
     */
    override fun generate(entry: ApiKeyEntry) = ApiKey(
        id = UUID.randomUUID(),
        key = buildKey(),
        name = entry.name,
        authorities = authoritiesFrom(entry),
    )

    private fun buildKey(): String {
        val apiKeyBuilder = StringBuilder()
        for (i in 0..apiKeyLength) {
            apiKeyBuilder.append(characters[secureRandom.nextInt(characters.length - 1)])
        }
        return apiKeyBuilder.toString()
    }

    private fun authoritiesFrom(model: ApiKeyEntry): List<GrantedAuthority> {
        val authorities = ArrayList<GrantedAuthority>()
        if (model.read) {
            authorities.add(SimpleGrantedAuthority(ApiKeyAuthorities.READ_AUTHORITY))
        }
        if (model.write) {
            authorities.add(SimpleGrantedAuthority(ApiKeyAuthorities.WRITE_AUTHORITY))
        }
        if (model.delete) {
            authorities.add(SimpleGrantedAuthority(ApiKeyAuthorities.DELETE_AUTHORITY))
        }
        return unmodifiableList(authorities)
    }
}
