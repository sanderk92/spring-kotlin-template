package com.template.domain

import com.template.config.IdGenerator
import com.template.config.security.apikey.ApiKeyGenerator
import com.template.config.security.apikey.HashGenerator
import com.template.config.security.user.Authority
import com.template.config.security.apikey.SecureApiKeyService
import com.template.domain.model.ApiKey
import com.template.domain.model.ApiKeyCreated
import com.template.domain.model.User
import com.template.mappers.ApiKeyMapper
import com.template.persistence.ApiKeyRepository
import com.template.persistence.UserRepository
import com.template.persistence.entity.ApiKeyEntity
import com.template.persistence.entity.UserEntity
import java.util.UUID
import kotlin.jvm.optionals.getOrNull
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.Caching
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
internal class ApiKeyService(
    private val uuidGenerator: IdGenerator<UUID>,
    private val apiKeyGenerator: ApiKeyGenerator,
    private val hashGenerator: HashGenerator,
    private val userRepository: UserRepository,
    private val apiKeyRepository: ApiKeyRepository,
    private val apiKeyMapper: ApiKeyMapper,
) : SecureApiKeyService {
    @Transactional(readOnly = true)
    @Cacheable(value = ["apiKeyByUser"], key = "#userId.value")
    fun findByUserId(userId: User.Id): List<ApiKey> =
        apiKeyRepository.findAllByUserId(userId.value)
            .map(apiKeyMapper::toApiKey)

    @Transactional(readOnly = true)
    @Cacheable(value = ["apiKeyByKey"], key = "#hashedApiKey")
    override fun findByApiKey(hashedApiKey: String): ApiKey? =
        apiKeyRepository.findByApiKey(hashedApiKey)
            ?.let(apiKeyMapper::toApiKey)

    @Transactional
    @CacheEvict(value = ["apiKeyByUser"], key = "#userId.value")
    fun create(userId: User.Id, name: String, authorities: Set<Authority>): ApiKeyCreated? =
        userRepository.findById(userId.value).getOrNull()?.let { user ->
            val unHashedApiKey = apiKeyGenerator.generate()
            createEntity(unHashedApiKey, name, user, authorities)
                .let(apiKeyRepository::save)
                .let { apiKeyMapper.toApiKeyCreated(it, unHashedApiKey) }
        }

    @Transactional
    @Caching(
        evict = [
            CacheEvict(value = ["apiKeyByUser"], key = "#userId.value"),
            CacheEvict(value = ["apiKeyByKey"], allEntries = true),
        ],
    )
    fun delete(userId: User.Id, apiKeyId: ApiKey.Id) {
        apiKeyRepository.delete(userId.value, apiKeyId.value)
    }

    private fun createEntity(unHashedApiKey: String, name: String, user: UserEntity, authorities: Set<Authority>) =
        ApiKeyEntity(
            id = uuidGenerator(),
            hashedKey = hashGenerator.hash(unHashedApiKey),
            name = name,
            owner = user,
            authorities = authorities,
        )
}
