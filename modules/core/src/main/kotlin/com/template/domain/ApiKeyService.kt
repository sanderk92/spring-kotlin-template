package com.template.domain

import com.template.config.security.apikey.ApiKeyGenerator
import com.template.config.security.apikey.HashGenerator
import com.template.config.security.user.Authority
import com.template.domain.model.ApiKeyCreated
import com.template.mappers.ApiKeyMapper
import com.template.persistence.ApiKeyRepository
import com.template.persistence.UserRepository
import com.template.persistence.entity.ApiKeyEntity
import java.util.*
import kotlin.jvm.optionals.getOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
internal class ApiKeyService(
    private val apiKeyGenerator: ApiKeyGenerator,
    private val hashGenerator: HashGenerator,
    private val userRepository: UserRepository,
    private val apiKeyRepository: ApiKeyRepository,
    private val apiKeyMapper: ApiKeyMapper,
) {
    @Transactional
    fun createApiKey(userId: UUID, name: String, authorities: List<Authority>): ApiKeyCreated? {
        val unHashedApiKey = apiKeyGenerator.generate()
        val hashedApiKey = hashGenerator.hash(unHashedApiKey)

        return userRepository.findById(userId).getOrNull()
            ?.let { user -> ApiKeyEntity(hashedApiKey, name, user, authorities) }
            ?.let(apiKeyRepository::save)
            ?.let { entity -> apiKeyMapper.toApiKeyCreated(entity, unHashedApiKey) }
    }

    @Transactional
    fun deleteApiKey(userId: UUID, apiKeyId: UUID) =
        apiKeyRepository.delete(userId, apiKeyId)
}
