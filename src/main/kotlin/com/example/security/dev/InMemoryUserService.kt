package com.example.security.dev

import com.example.security.apikey.ApiKeyEntry
import com.example.security.user.User
import com.example.security.user.UserAuthority
import com.example.security.user.UserService
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service
import java.util.*

@Service
@ConditionalOnProperty("feature.users.in-memory")
class InMemoryUserService : UserService {

    private val users = mutableListOf<InMemoryUser>()

    override fun findById(userId: UUID): InMemoryUser? =
        users.firstOrNull { user -> user.id == userId }

    override fun findByApiKey(apiKey: String): InMemoryUser? =
        users.firstOrNull { user -> user.apiKeys.any { it.key == apiKey } }

    override fun create(userId: UUID, email: String, firstName: String, lastName: String): InMemoryUser =
        InMemoryUser(userId, email, firstName, lastName, emptyList(), emptyList()).also(users::add)

    override fun search(query: String): List<User> {
        val emailContaining = users.filter { it.email.contains(query) }
        val firstNameContaining = users.filter { it.firstName.contains(query) }
        val lastNameContaining = users.filter { it.lastName.contains(query) }
        return emailContaining.plus(firstNameContaining).plus(lastNameContaining).distinct()
    }

    override fun update(userId: UUID, authorities: List<UserAuthority>): InMemoryUser? =
        findById(userId)?.let { currentUser ->
            val updatedUser = InMemoryUser(
                id = currentUser.id,
                email = currentUser.email,
                firstName = currentUser.firstName,
                lastName = currentUser.lastName,
                apiKeys = currentUser.apiKeys,
                authorities = authorities,
            )

            replace(currentUser, updatedUser)
            updatedUser
        }

    override fun addApiKey(userId: UUID, entry: ApiKeyEntry): InMemoryApiKey? =
        findById(userId)?.let { currentUser ->
            val newInMemoryApiKey = InMemoryApiKey(
                id = UUID.randomUUID(),
                key = entry.key,
                name = entry.name,
                read = entry.read,
                write = entry.write,
                delete = entry.delete,
            )

            val updatedUser = InMemoryUser(
                id = currentUser.id,
                email = currentUser.email,
                firstName = currentUser.firstName,
                lastName = currentUser.lastName,
                apiKeys = currentUser.apiKeys.plus(newInMemoryApiKey),
                authorities = listOf(),

            )

            replace(currentUser, updatedUser)
            newInMemoryApiKey
        }

    override fun deleteApiKey(userId: UUID, apiKeyId: UUID) {
        findById(userId)?.also { currentUser ->
            val updatedUser = InMemoryUser(
                id = currentUser.id,
                email = currentUser.email,
                firstName = currentUser.firstName,
                lastName = currentUser.lastName,
                apiKeys = currentUser.apiKeys.filterNot { it.id == apiKeyId },
                authorities = listOf(),
            )

            replace(currentUser, updatedUser)
        }
    }

    private fun replace(currentUser: InMemoryUser, updatedUser: InMemoryUser) {
        users.remove(currentUser)
        users.add(updatedUser)
    }
}

