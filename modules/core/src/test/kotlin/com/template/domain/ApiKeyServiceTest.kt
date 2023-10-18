package com.template.domain

import com.template.config.security.apikey.ApiKeyGenerator
import com.template.config.security.apikey.HashGenerator
import com.template.config.security.user.Authority.*
import com.template.domain.models.apiKeyCreated
import com.template.domain.models.user
import com.template.mappers.ApiKeyMapperImpl
import com.template.persistence.ApiKeyRepository
import com.template.persistence.UserRepository
import com.template.persistence.entities.apiKeyEntity
import com.template.persistence.entities.userEntity
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import java.util.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class ApiKeyServiceTest {

    @MockK
    private lateinit var idGenerator: () -> UUID

    @MockK
    private lateinit var apiKeyGenerator: ApiKeyGenerator

    @MockK
    private lateinit var hashGenerator: HashGenerator

    @MockK
    private lateinit var userRepository: UserRepository

    @MockK
    private lateinit var apiKeyRepository: ApiKeyRepository

    private lateinit var service: ApiKeyService

    @BeforeEach
    fun setUp() {
        service = ApiKeyService(idGenerator, apiKeyGenerator, hashGenerator, userRepository, apiKeyRepository, ApiKeyMapperImpl())
    }

    @Test
    fun `Api key can be created`() {
        every { idGenerator() } returns apiKeyEntity.id
        every { userRepository.findById(any()) } returns Optional.of(userEntity)
        every { apiKeyGenerator.generate() } returns apiKeyCreated.key
        every { hashGenerator.hash(any()) } returns apiKeyEntity.hashedKey
        every { apiKeyRepository.save(any()) } returns apiKeyEntity

        val result = service.createApiKey(user.id, apiKeyEntity.name, apiKeyEntity.authorities)

        assertThat(result).isEqualTo(apiKeyCreated)
        verify(exactly = 1) { userRepository.findById(user.id) }
        verify(exactly = 1) { apiKeyGenerator.generate() }
        verify(exactly = 1) { hashGenerator.hash(apiKeyCreated.key) }
        verify(exactly = 1) { apiKeyRepository.save(apiKeyEntity.copy(owner = userEntity)) }
    }

    @Test
    fun `Api key can be not created for non-existing user`() {
        every { userRepository.findById(any()) } returns Optional.empty()

        val result = service.createApiKey(user.id, apiKeyEntity.name, apiKeyEntity.authorities)

        assertThat(result).isEqualTo(null)
        verify(exactly = 0) { apiKeyGenerator.generate() }
        verify(exactly = 0) { hashGenerator.hash(any()) }
        verify(exactly = 0) { apiKeyRepository.save(any()) }
    }

    @Test
    fun `Api key can be deleted`() {
        every { apiKeyRepository.delete(any(), any()) } returns Unit

        service.deleteApiKey(userEntity.id, apiKeyEntity.id)

        verify(exactly = 1) { service.deleteApiKey(userEntity.id, apiKeyEntity.id) }
    }
}
