package com.template.domain

import com.template.config.security.user.Authority
import com.template.config.security.user.SecureUserEntry
import com.template.config.security.user.SecureUserService
import com.template.domain.model.User
import com.template.mappers.UserMapper
import com.template.persistence.UserRepository
import com.template.persistence.entity.UserEntity
import java.util.*
import kotlin.jvm.optionals.getOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper,
) : SecureUserService {

    @Transactional(readOnly = true)
    override fun search(query: String): List<User> =
        userRepository.search(query)
            .map(userMapper::toUser)

    @Transactional(readOnly = true)
    override fun findById(userId: UUID): User? =
        userRepository.findById(userId).getOrNull()
            ?.let(userMapper::toUser)

    @Transactional(readOnly = true)
    override fun findByApiKey(apiKey: String): User? =
        userRepository.findByApiKey(apiKey)
            ?.let(userMapper::toUser)

    @Transactional
    override fun create(entry: SecureUserEntry): User =
        userRepository.save(createEntity(entry))
            .let(userMapper::toUser)

    @Transactional
    override fun update(userId: UUID, authorities: List<Authority>): User? =
        userRepository.updateById(userId, authorities.map(Authority::toString))
            ?.let(userMapper::toUser)
}

private fun createEntity(user: SecureUserEntry) = UserEntity(
    id = user.id,
    email = user.email,
    username = user.username,
    firstName = user.firstName,
    lastName = user.lastName,
    authorities = user.authorities.map(Authority::toString),
    apiKeys = listOf(),
)
