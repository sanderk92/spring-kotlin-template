package com.template.security.apikey

import com.template.controller.interfaces.ApiKeyCreateCommand
import java.security.SecureRandom
import org.springframework.stereotype.Service

@Service
class ApiKeyService(private val hashGenerator: HashGenerator) {
    private val apiKeyLength = 50
    private val secureRandom = SecureRandom()
    private val characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"

    fun create(request: ApiKeyCreateCommand) = UnHashedApiKeyEntry(
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
