package com.example.security.dev

import com.example.security.apikey.ApiKeyRequest
import com.example.security.apikey.ApiKeyService
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
        val request = ApiKeyRequest(
            name = "development key",
            read = true,
            write = true,
            delete = true
        )

        val unHashedEntry = apiKeyService.create(request).copy(key = TEST_API_KEY)
        val hashedEntry = apiKeyService.hash(unHashedEntry)

        val apiKey = ApiKey(
            id = UUID.randomUUID(),
            key = hashedEntry.key,
            name = hashedEntry.name,
            authorities = hashedEntry.authorities,
        )

        val inMemoryUser = InMemoryUser(
            id = UUID.randomUUID(),
            apiKeys = listOf(apiKey),
        )

        inMemoryUserRepository.store(inMemoryUser)

        println("GENERATED TEST API KEY: '$TEST_API_KEY' FOR USER '$inMemoryUser'")
    }
}
