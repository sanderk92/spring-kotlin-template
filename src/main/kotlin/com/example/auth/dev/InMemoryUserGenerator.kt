package com.example.auth.dev

import com.example.auth.apikey.ApiKeyService
import com.example.auth.apikey.HashGenerator
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
    private val apiKeyGenerator: ApiKeyService,
    private val hashGenerator: HashGenerator,
) {
    @PostConstruct
    fun generate() {
        val apiKey = ApiKeyRequest(
            name = "development key",
            read = true,
            write = true,
            delete = true
        ).let(apiKeyGenerator::createFrom)

        val user = InMemoryUser(
            id = UUID.randomUUID().toString(),
            apiKeys = listOf(
                apiKey.copy(
                    key = hashGenerator.hash(TEST_API_KEY)
                )
            ),
        )

        inMemoryUserRepository.store(user)

        println("GENERATED TEST API KEY: '$TEST_API_KEY' FOR USER '$user'")
    }
}
