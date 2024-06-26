package com.template.domain

import com.template.config.security.user.SecureUserEntry
import com.template.config.security.user.SecureUserService
import com.template.domain.model.User
import com.template.mappers.UserMapper
import com.template.persistence.UserRepository
import com.template.persistence.entity.UserEntity
import java.util.UUID
import kotlin.jvm.optionals.getOrNull
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
internal class UserService(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper,
) : SecureUserService {
    @Transactional(readOnly = true)
    fun search(query: String): List<User> =
        userRepository.search(query)
            .map(userMapper::toUser)

    @Transactional(readOnly = true)
    @Cacheable(value = ["userById"], key = "#userId.value")
    fun findById(userId: User.Id): User? =
        userRepository.findById(userId.value).getOrNull()
            ?.let(userMapper::toUser)

    @Transactional(readOnly = true)
    @Cacheable(value = ["userById"], key = "#userId")
    override fun findById(userId: UUID): User? =
        userRepository.findById(userId).getOrNull()
            ?.let(userMapper::toUser)

    @Transactional
    @CacheEvict(value = ["userById"], key = "#entry.id")
    override fun create(entry: SecureUserEntry): User =
        userRepository.save(createEntity(entry))
            .let(userMapper::toUser)

    private fun createEntity(user: SecureUserEntry) =
        UserEntity(
            id = user.id,
            email = user.email,
            username = user.username,
            firstName = user.firstName,
            lastName = user.lastName,
            apiKeys = emptyList(),
        )
}
