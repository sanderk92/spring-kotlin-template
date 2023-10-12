package com.template.domain

import com.template.config.security.apikey.HashedApiKeyEntry
import com.template.config.security.user.SecureUserEntry
import com.template.config.security.user.SecureUserService
import com.template.config.security.user.UserAuthority
import com.template.domain.model.ApiKey
import com.template.domain.model.User
import com.template.persistence.ApiKeyRepository
import com.template.persistence.UserRepository
import com.template.persistence.model.ApiKeyEntity
import com.template.persistence.model.UserEntity
import jakarta.transaction.Transactional
import java.util.*
import kotlin.jvm.optionals.getOrNull
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val apiKeyRepository: ApiKeyRepository,
) : SecureUserService {

    @Transactional
    override fun search(query: String): List<User> =
        userRepository.search(query).map(UserEntity::toModel)

    @Transactional
    override fun findById(userId: UUID): User? =
        userRepository.findById(userId).getOrNull()?.toModel()

    @Transactional
    override fun findByApiKey(apiKey: String): User? =
        userRepository.findByApiKey(apiKey)?.toModel()

    @Transactional
    override fun save(entry: SecureUserEntry): User =
        userRepository.save(entry.toEntity()).toModel()

    @Transactional
    override fun update(userId: UUID, authorities: List<UserAuthority>): User? =
        userRepository.updateById(userId, authorities.map(UserAuthority::toString))?.toModel()

    @Transactional
    override fun addApiKey(userId: UUID, entry: HashedApiKeyEntry): ApiKey? =
        userRepository.findById(userId).getOrNull()
            ?.let { userEntity -> apiKeyRepository.save(entry.toEntity(userEntity)).toModel() }

    @Transactional
    override fun deleteApiKey(userId: UUID, apiKeyId: UUID) {
        userRepository.findById(userId).getOrNull()
            ?.also { _ -> apiKeyRepository.deleteById(apiKeyId) }
    }
}

fun SecureUserEntry.toEntity() = UserEntity(
    id = id,
    email = email,
    username = username,
    firstName = firstName,
    lastName = lastName,
    authorities = authorities.map(UserAuthority::toString),
    apiKeys = listOf(),
)

fun HashedApiKeyEntry.toEntity(user: UserEntity) = ApiKeyEntity(
    id = UUID.randomUUID(),
    key = key,
    name = name,
    read = read,
    write = write,
    delete = delete,
    owner = user,
)

fun UserEntity.toModel() = User(
    id = id,
    email = email,
    username = username,
    firstName = firstName,
    lastName = lastName,
    apiKeys = apiKeys.map { it.toModel() },
    authorities = authorities.mapNotNull { UserAuthority.valueOf(it) },
)

fun ApiKeyEntity.toModel() = ApiKey(
    id = id,
    key = key,
    name = name,
    read = read,
    write = write,
    delete = delete,
)
