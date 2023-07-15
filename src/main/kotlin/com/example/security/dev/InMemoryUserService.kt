package com.example.security.dev

import com.example.security.apikey.ApiKeyEntry
import com.example.security.user.UserService
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service
import java.util.*

@Service
@ConditionalOnProperty("feature.in-memory-users")
class InMemoryUserService : UserService {

    private val users = mutableListOf<InMemoryUser>()

    override fun findById(userId: UUID): InMemoryUser? =
        users.firstOrNull { user -> user.id == userId }

    override fun findByApiKey(apiKey: String): InMemoryUser? =
        users.firstOrNull { user -> user.apiKeys.any { it.key == apiKey } }

    override fun createIfNotExists(userId: UUID): InMemoryUser =
        findById(userId) ?: InMemoryUser(userId, emptyList())
            .also(users::add)

    override fun addApiKey(userId: UUID, entry: ApiKeyEntry): InMemoryApiKey? =
        findById(userId)?.let { currentUser ->
            val newInMemoryApiKey = InMemoryApiKey(
                id = UUID.randomUUID(),
                key = entry.key,
                name = entry.name,
                authorities = entry.authorities
            )

            val updatedUser = InMemoryUser(
                id = currentUser.id,
                apiKeys = currentUser.apiKeys.plus(newInMemoryApiKey)
            )

            replace(currentUser, updatedUser)
            newInMemoryApiKey
        }

    override fun deleteApiKey(userId: UUID, apiKeyId: UUID) {
        findById(userId)?.also { currentUser ->
            val updatedUser = InMemoryUser(
                id = currentUser.id,
                apiKeys = currentUser.apiKeys.filterNot { it.id == apiKeyId }
            )

            replace(currentUser, updatedUser)
        }
    }

    private fun replace(currentUser: InMemoryUser, updatedUser: InMemoryUser) {
        users.remove(currentUser)
        users.add(updatedUser)
    }
}

