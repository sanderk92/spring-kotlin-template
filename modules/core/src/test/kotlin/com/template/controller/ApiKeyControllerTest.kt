package com.template.controller

import com.template.config.security.user.Authority
import com.template.config.security.user.Authority.DELETE
import com.template.config.security.user.Authority.READ
import com.template.config.security.user.Authority.WRITE
import com.template.controller.interfaces.ApiKeyInterface.Companion.ENDPOINT
import com.template.domain.ApiKeyService
import com.template.domain.UserService
import com.template.domain.models.PRINCIPAL_NAME
import com.template.domain.models.apiKey
import com.template.domain.models.apiKeyCreated
import com.template.domain.models.apiKeyRequest
import com.template.domain.models.user
import com.template.mappers.ApiKeyMapperImpl
import com.template.mappers.UserMapperImpl
import com.template.util.EnableAspectOrientedProgramming
import com.template.util.EnableGlobalMethodSecurity
import com.template.util.asJson
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.util.UUID
import org.apache.catalina.security.SecurityConfig
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@WebMvcTest
@Import(
    value = [
        ApiKeyController::class,
        SecurityConfig::class,
        EnableGlobalMethodSecurity::class,
        EnableAspectOrientedProgramming::class,
    ],
)
internal class ApiKeyControllerTest {
    @TestConfiguration
    class TestConfig {
        @Bean
        fun apiKeyService() = mockk<ApiKeyService>()

        @Bean
        fun userService() = mockk<UserService>()

        @Bean
        fun apiKeyMapper() = ApiKeyMapperImpl()

        @Bean
        fun userMapper() = UserMapperImpl(apiKeyMapper())
    }

    @Autowired
    private lateinit var mvc: MockMvc

    @Autowired
    private lateinit var apiKeyService: ApiKeyService

    @Autowired
    private lateinit var userService: UserService

    /*
     Contract tests
     */

    @Test
    @WithMockUser(username = PRINCIPAL_NAME)
    fun `Api keys can be retrieved`() {
        every { userService.findById(user.id) } returns user

        mvc.get(ENDPOINT) {
        }.andExpect {
            status { isOk() }
            jsonPath("$.size()", equalTo(1))
            jsonPath("$[0].id", equalTo(apiKey.id.toString()))
            jsonPath("$[0].name", equalTo(apiKey.name))
            jsonPath("$[0].authorities", equalTo(apiKey.authorities.map(Authority::toString)))
        }
    }

    @Test
    @WithMockUser(username = PRINCIPAL_NAME)
    fun `Api keys for non-existing user cannot be retrieved`() {
        every { userService.findById(user.id) } returns null

        mvc.get(ENDPOINT) {
        }.andExpect {
            status { isNotFound() }
        }
    }

    @Test
    @WithMockUser(username = PRINCIPAL_NAME)
    fun `Api keys can be created`() {
        every {
            apiKeyService.createApiKey(
                UUID.fromString(PRINCIPAL_NAME),
                apiKeyRequest.name,
                listOf(READ, WRITE, DELETE),
            )
        } returns apiKeyCreated

        val payload =
            mapOf(
                "name" to apiKeyRequest.name,
                "read" to apiKeyRequest.read,
                "write" to apiKeyRequest.write,
                "delete" to apiKeyRequest.delete,
            ).asJson()

        mvc.post(ENDPOINT) {
            with(csrf())
            contentType = MediaType.APPLICATION_JSON
            content = payload
        }.andExpect {
            status { isOk() }
            jsonPath("$.key", equalTo(apiKeyCreated.key))
            jsonPath("$.name", equalTo(apiKeyCreated.name))
            jsonPath("$.authorities", equalTo(apiKeyCreated.authorities.map(Authority::toString)))
        }
    }

    @Test
    @WithMockUser(username = PRINCIPAL_NAME)
    fun `Api keys for non-existing users cannot be created`() {
        every { apiKeyService.createApiKey(UUID.fromString(PRINCIPAL_NAME), apiKeyRequest.name, listOf(READ, WRITE, DELETE)) } returns null

        val payload =
            mapOf(
                "name" to apiKeyRequest.name,
                "read" to apiKeyRequest.read,
                "write" to apiKeyRequest.write,
                "delete" to apiKeyRequest.delete,
            ).asJson()

        mvc.post(ENDPOINT) {
            with(csrf())
            contentType = MediaType.APPLICATION_JSON
            content = payload
        }.andExpect {
            status { isNotFound() }
        }
    }

    @Test
    @WithMockUser(username = PRINCIPAL_NAME)
    fun `Api keys can be deleted`() {
        every { apiKeyService.deleteApiKey(any(), any()) } returns Unit

        mvc.delete("$ENDPOINT/${apiKey.id}") {
            with(csrf())
        }.andExpect {
            status { isNoContent() }
        }

        verify(exactly = 1) { apiKeyService.deleteApiKey(user.id, apiKey.id) }
    }

    /*
    Security config tests
     */

    @Test
    @WithAnonymousUser
    fun `Unauthenticated user cannot retrieve api keys`() {
        mvc.get(ENDPOINT) {
        }.andExpect {
            status { isUnauthorized() }
        }
    }

    @Test
    @WithAnonymousUser
    fun `Unauthenticated user cannot create api keys`() {
        mvc.post(ENDPOINT) {
            with(csrf())
        }.andExpect {
            status { isUnauthorized() }
        }
    }

    @Test
    @WithAnonymousUser
    fun `Unauthenticated user cannot delete api keys`() {
        mvc.delete("/$ENDPOINT/${apiKey.id}") {
            with(csrf())
        }.andExpect {
            status { isUnauthorized() }
        }
    }
}
