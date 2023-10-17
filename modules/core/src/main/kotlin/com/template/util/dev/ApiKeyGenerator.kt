package com.template.util.dev

import com.template.config.security.user.Authority.*
import com.template.config.security.user.SecureUserEntry
import com.template.domain.ApiKeyService
import com.template.domain.UserService
import jakarta.annotation.PostConstruct
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import java.util.*

@Service
@Profile(value = ["!prd", "!acc"])
@ConditionalOnProperty("feature.dev.generate-user", havingValue = "true")
internal class ApiKeyGenerator(
    private val userService: UserService,
    private val apiKeyService: ApiKeyService,
) {
    @PostConstruct
    fun generateDevApiKey() {
        val user = userService.create(
            SecureUserEntry(
                id = UUID.randomUUID(),
                email = "dev@dev.nl",
                username = "dev",
                firstName = "dev",
                lastName = "dev",
            ),
        )

        val key = apiKeyService.createApiKey(
            userId = user.id,
            name = "dev-key",
            authorities = listOf(READ, WRITE, DELETE, ADMIN),
        )

        println()
        println("API KEY GENERATED FOR DEV: ${key?.key}")
        println()
    }
}
