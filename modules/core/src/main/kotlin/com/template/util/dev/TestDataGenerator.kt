package com.template.util.dev

import com.template.config.security.user.Authority.ADMIN
import com.template.config.security.user.Authority.DELETE
import com.template.config.security.user.Authority.READ
import com.template.config.security.user.Authority.WRITE
import com.template.config.security.user.SecureUserEntry
import com.template.domain.ApiKeyService
import com.template.domain.UserService
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.annotation.PostConstruct
import java.util.UUID
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

private val log = KotlinLogging.logger {}

@Service
@Profile(value = ["!prd", "!acc"])
@ConditionalOnProperty("feature.dev.generated-test-data", havingValue = "true")
internal class TestDataGenerator(
    private val userService: UserService,
    private val apiKeyService: ApiKeyService,
    @Value("\${feature.dev.generated-user-id}") private val userId: String,
) {
    @PostConstruct
    fun generateDevData() {
        val user = userService.create(
            SecureUserEntry(
                id = UUID.fromString(userId),
                email = "dev@dev.dev",
                username = "dev-username",
                firstName = "dev-firstname",
                lastName = "dev-lastname",
            ),
        )

        val key = apiKeyService.create(
            userId = user.id,
            name = "dev-key",
            authorities = setOf(READ, WRITE, DELETE, ADMIN),
        )

        log.info { "Generated test data for user '${user.id}'" }
        log.info { "Generated api key '${key?.key}'" }
    }
}
