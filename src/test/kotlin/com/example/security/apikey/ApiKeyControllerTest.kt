package com.example.security.apikey

import com.example.PRINCIPAL_NAME
import com.example.config.EnableGlobalMethodSecurity
import com.example.security.apikey.model.ApiKeyAuthorities.READ
import com.example.security.apikey.model.ApiKeyEntity
import com.example.security.apikey.model.UserEntity
import com.example.security.apikey.model.UserEntityService
import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import java.util.*

private val apiKey = StubApiKey(
    id = UUID.randomUUID(),
    key = "key",
    name = "name",
    authorities = listOf(READ)
)

private val user = StubUserEntity(
    id = UUID.fromString(PRINCIPAL_NAME),
    apiKeys = listOf(apiKey),
)

@WebMvcTest
@Import(value = [ApiKeyController::class, EnableGlobalMethodSecurity::class])
class ApiKeyControllerTest {

    @TestConfiguration
    class TestConfig {

        @Bean
        fun apiKeyService() = mockk<ApiKeyService>()

        @Bean
        fun userService() = mockk<UserEntityService>()
    }

    @Autowired
    private lateinit var mvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var apiKeyService: ApiKeyService

    @Autowired
    private lateinit var userService: UserEntityService

    @Test
    @WithAnonymousUser
    fun `Unauthenticated user cannot retrieve api keys`() {
        mvc.get("/apikey") {
        }.andExpect {
            status { isUnauthorized() }
        }
    }

    @Test
    @WithMockUser(username = PRINCIPAL_NAME)
    fun `Authenticated user can retrieve api keys`() {
        every { userService.findById(user.id) } returns user

        mvc.get("/apikey") {
        }.andExpect {
            status { isOk() }
            content { equalTo(objectMapper.writeValueAsString(user)) }
        }
    }
}

data class StubUserEntity(
    override val id: UUID,
    override val apiKeys: List<ApiKeyEntity>
): UserEntity

data class StubApiKey(
    override val id: UUID,
    override val key: String,
    override val name: String,
    override val authorities: List<String>,

): ApiKeyEntity