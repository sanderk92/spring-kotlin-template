package com.template.domain

import com.template.domain.models.user
import com.template.mappers.ApiKeyMapperImpl
import com.template.mappers.UserMapperImpl
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
internal class UserServiceTest {

    @MockK
    private lateinit var userRepository: UserRepository

    private val userMapper = UserMapperImpl(ApiKeyMapperImpl())

    private lateinit var userService: UserService

    @BeforeEach
    fun setUp() {
        userService = UserService(userRepository, userMapper)
    }

    @Test
    fun `Users can be searched for`() {
        every { userRepository.search(any()) } returns listOf(userEntity.copy(apiKeys = listOf(apiKeyEntity)))

        val query = "query"
        val result = userService.search(query)

        assertThat(result).containsExactly(user)
        verify(exactly = 1) { userRepository.search(query) }
    }

    @Test
    fun `Users can be found by id`() {
        every { userRepository.findById(any()) } returns Optional.of(userEntity.copy(apiKeys = listOf(apiKeyEntity)))

        val id = UUID.randomUUID()
        val result = userService.findById(id)

        assertThat(result).isEqualTo(user)
        verify(exactly = 1) { userRepository.findById(id) }
    }

    @Test
    fun `Users can be found missing by id`() {
        every { userRepository.findById(any()) } returns Optional.empty()

        val result = userService.findById(UUID.randomUUID())

        assertThat(result).isEqualTo(null)
    }

    @Test
    fun `Users can be found by api key`() {
        every { userRepository.findByApiKey(any()) } returns userEntity.copy(apiKeys = listOf(apiKeyEntity))

        val key = "key"
        val result = userService.findByApiKey(key)

        assertThat(result).isEqualTo(user)
        verify(exactly = 1) { userRepository.findByApiKey(key) }
    }

    @Test
    fun `Users can be found missing by api key`() {
        every { userRepository.findByApiKey(any()) } returns null

        val result = userService.findByApiKey("key")

        assertThat(result).isEqualTo(null)
    }
}
