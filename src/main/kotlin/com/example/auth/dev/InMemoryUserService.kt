package com.example.auth.dev

import com.example.auth.apikey.model.ApiKey
import com.example.auth.apikey.model.ApiKeyUser
import com.example.auth.apikey.model.ApiKeyUserService
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service
import java.util.*

@Service
@ConditionalOnProperty("feature.in-memory-users")
class InMemoryUserService : ApiKeyUserService {

    private val users = mutableListOf<InMemoryUser>()

    fun store(user: InMemoryUser): InMemoryUser =
        user.also(users::add)

    override fun findById(userId: String): Optional<ApiKeyUser> =
        Optional.ofNullable(users.firstOrNull { user -> user.id == userId })

    override fun findByApiKey(apiKeyId: String): Optional<ApiKeyUser> =
        Optional.ofNullable(users.firstOrNull { user -> user.apiKeys.any { it.key == apiKeyId } })

    override fun addApiKey(userId: String, apiKey: ApiKey): Optional<ApiKeyUser> =
        findById(userId).map { user ->
            val updatedUser = InMemoryUser(
                id = user.id,
                apiKeys = user.apiKeys.plus(apiKey)
            )
            users.remove(user)
            users.add(updatedUser)
            updatedUser
        }

    override fun deleteApiKey(userId: String, apiKeyId: String) {
        findById(userId).ifPresent { user ->
            val updatedUser = InMemoryUser(
                id = user.id,
                apiKeys = user.apiKeys.filterNot { it.id == apiKeyId }
            )
            users.remove(user)
            users.add(updatedUser)
        }
    }
}

