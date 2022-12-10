package com.example.auth.dev

import com.example.auth.apikey.ApiKeyEntry
import com.example.auth.apikey.ApiKeyGenerator
import com.example.auth.apikey.HashGenerator
import com.example.auth.apikey.Sha256HashGenerator
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import java.util.*
import javax.annotation.PostConstruct

private const val TEST_API_KEY = "dev-api-key"

@Component
@ConditionalOnProperty("feature.in-memory-users")
class InMemoryUserGenerator(
    private val inMemoryUserRepository: InMemoryUserRepository,
    private val apiKeyGenerator: ApiKeyGenerator,
    private val hashGenerator: HashGenerator,
) {
    @PostConstruct
    fun generate() {
        val apiKeyEntry = ApiKeyEntry(
            name = "Test",
            read = true,
            write = true,
            delete = true
        )

        val apiKey = apiKeyGenerator.generate(apiKeyEntry).copy(
            key = hashGenerator.hash(TEST_API_KEY)
        )

        val user = InMemoryUser(
            id = UUID.randomUUID(),
            subject = UUID.randomUUID().toString(),
            apiKey = listOf(apiKey),
        )

        inMemoryUserRepository.add(user)

        println("GENERATED TEST API KEY: '$TEST_API_KEY' FOR USER '$user'")
    }
}
