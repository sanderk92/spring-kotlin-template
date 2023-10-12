package com.template.domain

import com.template.config.security.apikey.ApiKey
import com.template.config.security.apikey.ApiKeyEntry
import com.template.config.security.apikey.HashedApiKeyEntry
import com.template.config.security.user.UserAuthority
import com.template.config.security.user.SecureUserEntry
import com.template.config.security.user.SecureUserService
import com.template.domain.model.User
import com.template.persistence.model.UserEntity
import com.template.persistence.UserRepository
import com.template.persistence.model.ApiKeyEntity
import java.util.*
import org.springframework.stereotype.Service

@Service
class UserService(private val repository: UserRepository) : SecureUserService {

    override fun findById(userId: UUID): User? {
        return repository.findById(userId)?.toModel()
    }

    override fun findByApiKey(apiKey: String): User? {
        return repository.findByApiKey(apiKey)?.toModel()
    }

    override fun create(entry: SecureUserEntry): User {
        return repository.create(entry.toEntity()).toModel()
    }

    override fun search(query: String): List<User> {
        return repository.search(query).map(UserEntity::toModel)
    }

    override fun update(userId: UUID, authorities: List<UserAuthority>): User? {
        return repository.update(userId, authorities)?.toModel()
    }

    override fun addApiKey(userId: UUID, entry: HashedApiKeyEntry): ApiKey? {
        return repository.addApiKey(userId, entry.toEntity())
    }

    override fun deleteApiKey(userId: UUID, apiKeyId: UUID) {
        return repository.deleteApiKey(userId, apiKeyId)
    }
}

fun SecureUserEntry.toEntity() = UserEntity(
    id = id,
    email = email,
    username = username,
    firstName = firstName,
    lastName = lastName,
    authorities = authorities,
    apiKeys = listOf(),
)

fun ApiKeyEntry.toEntity() = ApiKeyEntity(
    id = UUID.randomUUID(),
    key = key,
    name = name,
    read = read,
    write = write,
    delete = delete,
)

fun UserEntity.toModel() = User(
    id = id,
    email = email,
    username = username,
    firstName = firstName,
    lastName = lastName,
    apiKeys = apiKeys,
    authorities = authorities,
)
