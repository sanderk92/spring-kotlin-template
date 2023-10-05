package com.template.security.dev

import com.template.controller.ApiKeyRequest
import com.template.security.apikey.ApiKeyService
import com.template.security.user.UserAuthority.*
import jakarta.annotation.PostConstruct
import java.util.*
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component

private const val DEV_API_KEY = "dev-api-key"

@Component
@ConditionalOnProperty("feature.users.generate")
class InMemoryUserGenerator(
    private val inMemoryUserRepository: InMemoryUserService,
    private val apiKeyService: ApiKeyService,
) {
    @PostConstruct
    fun generate() {
        val user = inMemoryUserRepository.create(
            userId = UUID.randomUUID(),
            email = "dev@user.com",
            firstName = "dev",
            lastName = "user",
            authorities = listOf(READ, WRITE, DELETE, ADMIN)
        )

        val request = ApiKeyRequest(
            name = "development key",
            read = true,
            write = true,
            delete = true
        )

        val hashedApiKey = request
            .let(apiKeyService::create)
            .copy(key = DEV_API_KEY)
            .let(apiKeyService::hash)

        inMemoryUserRepository.addApiKey(user.id, hashedApiKey)

        println("GENERATED DEV API KEY: '$DEV_API_KEY'")
    }
}
