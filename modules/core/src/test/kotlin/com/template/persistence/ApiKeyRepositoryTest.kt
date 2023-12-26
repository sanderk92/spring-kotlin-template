package com.template.persistence

import com.template.persistence.entities.apiKeyEntity
import com.template.persistence.entities.userEntity
import com.template.persistence.entity.ApiKeyEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.ContextConfiguration

private val entityClass = ApiKeyEntity::class.java

@DataJpaTest
@EnableAutoConfiguration
@ContextConfiguration(classes = [ApiKeyRepository::class])
internal class ApiKeyRepositoryTest {
    @Autowired
    private lateinit var em: TestEntityManager

    @Autowired
    private lateinit var apiKeyRepository: ApiKeyRepository

    @Test
    fun `Can delete by user and api key`() {
        val persistedUser = em.persist(userEntity)
        val persistedApiKey = em.persist(apiKeyEntity.copy(owner = persistedUser))

        assertThat(em.find(entityClass, persistedApiKey.id)).isEqualTo(persistedApiKey)

        apiKeyRepository.delete(persistedUser.id, persistedApiKey.id)

        em.flush()
        em.clear()
        assertThat(em.find(entityClass, persistedApiKey.id)).isNull()
    }
}
