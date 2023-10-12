package com.template.persistence

import com.template.config.security.apikey.ApiKey
import com.template.config.security.user.UserAuthority
import com.template.domain.model.User
import com.template.persistence.model.ApiKeyEntity
import com.template.persistence.model.UserEntity
import java.util.*
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service

@Repository // TODO database impl
class UserRepository {

    private val users = mutableListOf<UserEntity>()

    fun findById(userId: UUID): UserEntity? =
        users.firstOrNull { user -> user.id == userId }

    fun findByApiKey(apiKey: String): UserEntity? =
        users.firstOrNull { user -> user.apiKeys.any { it.key == apiKey } }

    fun create(user: UserEntity) =
        users.add(user).let { user }

    fun search(query: String): List<UserEntity> {
        val usernameContaining = users.filter { it.username.contains(query) }
        val emailContaining = users.filter { it.email.contains(query) }
        val firstNameContaining = users.filter { it.firstName.contains(query) }
        val lastNameContaining = users.filter { it.lastName.contains(query) }
        return (usernameContaining + emailContaining + firstNameContaining + lastNameContaining).distinct()
    }

    fun update(userId: UUID, authorities: List<UserAuthority>): UserEntity? {
        return findById(userId)?.let { currentUser ->
            val updatedUser = UserEntity(
                id = currentUser.id,
                email = currentUser.email,
                username = currentUser.username,
                firstName = currentUser.firstName,
                lastName = currentUser.lastName,
                apiKeys = currentUser.apiKeys,
                authorities = authorities,
            )

            replace(currentUser, updatedUser)
            updatedUser
        }
    }

    fun addApiKey(userId: UUID, entry: ApiKeyEntity): ApiKey? {
        return findById(userId)?.let { currentUser ->
            val newInMemoryApiKey = com.template.domain.model.ApiKey(
                id = UUID.randomUUID(),
                key = entry.key,
                name = entry.name,
                read = entry.read,
                write = entry.write,
                delete = entry.delete,
            )

            val updatedUser = UserEntity(
                id = currentUser.id,
                email = currentUser.email,
                username = currentUser.username,
                firstName = currentUser.firstName,
                lastName = currentUser.lastName,
                apiKeys = currentUser.apiKeys.plus(newInMemoryApiKey),
                authorities = currentUser.authorities,
            )

            replace(currentUser, updatedUser)
            newInMemoryApiKey
        }

    }

    fun deleteApiKey(userId: UUID, apiKeyId: UUID) {
        findById(userId)?.also { currentUser ->
            val updatedUser = UserEntity(
                id = currentUser.id,
                email = currentUser.email,
                username = currentUser.username,
                firstName = currentUser.firstName,
                lastName = currentUser.lastName,
                apiKeys = currentUser.apiKeys.filterNot { it.id == apiKeyId },
                authorities = currentUser.authorities,
            )

            replace(currentUser, updatedUser)
        }
    }

    private fun replace(currentUser: UserEntity, updatedUser: UserEntity) {
        users.remove(currentUser)
        users.add(updatedUser)
    }
}
