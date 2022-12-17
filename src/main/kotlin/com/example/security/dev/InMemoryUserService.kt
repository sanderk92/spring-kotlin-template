package com.example.security.dev

import com.example.security.apikey.ApiKeyEntry
import com.example.security.apikey.model.UserEntity
import com.example.security.apikey.model.UserEntityService
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service
import java.util.*

@Service
@ConditionalOnProperty("feature.in-memory-users")
class InMemoryUserService : UserEntityService {

    private val inMemoryUsers = mutableListOf<InMemoryUser>()

    fun store(inMemoryUser: InMemoryUser): InMemoryUser =
        inMemoryUser.also(inMemoryUsers::add)

    override fun findById(userId: UUID): Optional<UserEntity> =
        Optional.ofNullable(inMemoryUsers.firstOrNull { user -> user.id == userId })

    override fun findByApiKey(apiKey: String): Optional<UserEntity> =
        Optional.ofNullable(inMemoryUsers.firstOrNull { user -> user.apiKeys.any { it.key == apiKey } })

    override fun addApiKey(userId: UUID, entry: ApiKeyEntry): Optional<UserEntity> =
        findById(userId).map { currentUser ->
            val newApiKey = ApiKey(
                id = UUID.randomUUID(),
                key = entry.key,
                authorities = entry.authorities,
                name = entry.key
            )

            val updatedInMemoryUser = InMemoryUser(
                id = currentUser.id,
                apiKeys = currentUser.apiKeys.plus(newApiKey)
            )
            inMemoryUsers.remove(currentUser)
            inMemoryUsers.add(updatedInMemoryUser)
            updatedInMemoryUser
        }

    override fun deleteApiKey(userId: UUID, apiKeyId: UUID) {
        findById(userId).ifPresent { currentUser ->
            val updatedInMemoryUser = InMemoryUser(
                id = currentUser.id,
                apiKeys = currentUser.apiKeys.filterNot { it.id == apiKeyId }
            )
            inMemoryUsers.remove(currentUser)
            inMemoryUsers.add(updatedInMemoryUser)
        }
    }
}

