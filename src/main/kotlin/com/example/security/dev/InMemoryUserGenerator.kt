package com.example.security.dev

import com.example.security.apikey.ApiKeyRequest
import com.example.security.apikey.ApiKeyService
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import java.util.*
import javax.annotation.PostConstruct

private const val DEV_API_KEY = "dev-api-key"

@Component
@ConditionalOnProperty("feature.in-memory-users")
class InMemoryUserGenerator(
    private val inMemoryUserRepository: InMemoryUserService,
    private val apiKeyService: ApiKeyService,
) {
    @PostConstruct
    fun generate() {
        val userId = UUID.randomUUID()

        val request = ApiKeyRequest(
            name = "development key",
            read = true,
            write = true,
            delete = true
        )

        val hashedEntry = request
            .let(apiKeyService::create)
            .copy(key = DEV_API_KEY)
            .let(apiKeyService::hash)

        inMemoryUserRepository.createIfNotExists(userId)
        inMemoryUserRepository.addApiKey(userId, hashedEntry)

        println("GENERATED DEV API KEY: '$DEV_API_KEY'")
    }
}
