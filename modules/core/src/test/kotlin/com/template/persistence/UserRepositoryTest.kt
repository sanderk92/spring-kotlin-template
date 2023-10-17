package com.template.persistence

import com.template.domain.models.apiKey
import com.template.persistence.entities.apiKeyEntity
import com.template.persistence.entities.userEntity
import com.template.persistence.entity.UserEntity
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.ContextConfiguration

private val entityClass = UserEntity::class.java

@DataJpaTest
@EnableAutoConfiguration
@ContextConfiguration(classes = [UserRepository::class])
class UserRepositoryTest {

    @Autowired
    private lateinit var em: TestEntityManager

    @Autowired
    private lateinit var userRepository: UserRepository

    @Test
    fun `Can search users by different properties`() {
        val persistedUser = em.persistAndFlush(userEntity)

        val emailResult = userRepository.search(userEntity.email)
        val usernameResult = userRepository.search(userEntity.username)
        val firstNameResult = userRepository.search(userEntity.firstName)
        val lastNameResult = userRepository.search(userEntity.lastName)

        assertThat(emailResult).containsExactly(persistedUser)
        assertThat(usernameResult).containsExactly(persistedUser)
        assertThat(firstNameResult).containsExactly(persistedUser)
        assertThat(lastNameResult).containsExactly(persistedUser)
    }

    @Test
    fun `Can search users by api key`() {
        val persistedUser = em.persist(userEntity)
        val persistedApiKey = em.persist(apiKeyEntity.copy(owner = persistedUser))

        val result = userRepository.findByApiKey(apiKey.hashedKey)

        assertThat(result).isEqualTo(persistedUser.copy(apiKeys = listOf(persistedApiKey)))
    }
}

