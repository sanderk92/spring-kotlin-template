package com.example.auth.dev

import com.example.auth.apikey.ApiKeyService
import com.example.auth.apikey.model.ApiKeyRequest
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import java.util.*
import javax.annotation.PostConstruct

private const val TEST_API_KEY = "dev-api-key"

@Component
@ConditionalOnProperty("feature.in-memory-users")
class InMemoryUserGenerator(
    private val inMemoryUserRepository: InMemoryUserService,
    private val apiKeyService: ApiKeyService,
) {
    @PostConstruct
    fun generate() {
        val apiKeyRequest = ApiKeyRequest(
            name = "development key",
            read = true,
            write = true,
            delete = true
        )

        val devApiKey = apiKeyService.createFrom(apiKeyRequest).copy(
            key = TEST_API_KEY
        )

        val hashedDevApiKey = apiKeyService.hash(devApiKey)

        val user = InMemoryUser(
            id = UUID.randomUUID().toString(),
            apiKeys = listOf(hashedDevApiKey),
        )

        inMemoryUserRepository.store(user)

        println("GENERATED TEST API KEY: '$TEST_API_KEY' FOR USER '$user'")
    }
}
