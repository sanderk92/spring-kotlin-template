package com.template.config.security.apikey

import java.security.SecureRandom
import org.springframework.stereotype.Service

internal interface ApiKeyGenerator {
    fun generate(): String
}

@Service
internal class SecureRandomApiKeyGenerator : ApiKeyGenerator {
    private val apiKeyLength = 50
    private val secureRandom = SecureRandom()
    private val characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"

    override fun generate(): String {
        val apiKeyBuilder = StringBuilder()
        for (i in 0..apiKeyLength) {
            apiKeyBuilder.append(characters[secureRandom.nextInt(characters.length - 1)])
        }
        return apiKeyBuilder.toString()
    }
}
