package com.template.domain

import com.template.config.security.user.Authority
import com.template.config.security.user.SecureUserEntry
import com.template.config.security.user.SecureUserService
import com.template.domain.model.ApiKey
import com.template.domain.model.User
import com.template.persistence.UserRepository
import com.template.persistence.model.ApiKeyEntity
import com.template.persistence.model.UserEntity
import java.util.*
import kotlin.jvm.optionals.getOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
) : SecureUserService {

    @Transactional(readOnly = true)
    override fun search(query: String): List<User> =
        userRepository.search(query).map(UserEntity::toModel)

    @Transactional(readOnly = true)
    override fun findById(userId: UUID): User? =
        userRepository.findById(userId).getOrNull()?.toModel()

    @Transactional(readOnly = true)
    override fun findByApiKey(apiKey: String): User? =
        userRepository.findByApiKey(apiKey)?.toModel()

    @Transactional
    override fun create(entry: SecureUserEntry): User =
        userRepository.save(entry.toEntity()).toModel()

    @Transactional
    override fun update(userId: UUID, authorities: List<Authority>): User? =
        userRepository.updateById(userId, authorities.map(Authority::toString))?.toModel()
}

private fun SecureUserEntry.toEntity() = UserEntity(
    id = id,
    email = email,
    username = username,
    firstName = firstName,
    lastName = lastName,
    authorities = authorities.map(Authority::toString),
    apiKeys = listOf(),
)

private fun UserEntity.toModel() = User(
    id = id,
    email = email,
    username = username,
    firstName = firstName,
    lastName = lastName,
    apiKeys = apiKeys.map(ApiKeyEntity::toModel),
    authorities = authorities.mapNotNull(Authority.Companion::valueOf),
)

private fun ApiKeyEntity.toModel() = ApiKey(
    id = id,
    hashedKey = key,
    name = name,
    authorities = authorities.mapNotNull(Authority.Companion::valueOf),
)
