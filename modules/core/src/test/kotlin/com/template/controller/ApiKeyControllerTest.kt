package com.template.controller

import com.template.config.security.user.Authority
import com.template.config.security.user.Authority.*
import com.template.config.security.user.READ_ROLE
import com.template.controller.interfaces.ApiKeyInterface.Companion.ENDPOINT
import com.template.controller.objects.*
import com.template.domain.ApiKeyService
import com.template.domain.UserService
import com.template.mappers.ApiKeyMapperImpl
import com.template.mappers.UserMapperImpl
import com.template.util.EnableAspectOrientedProgramming
import com.template.util.EnableGlobalMethodSecurity
import com.template.util.asJson
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.util.*
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
        EnableGlobalMethodSecurity::class,
        EnableAspectOrientedProgramming::class,
    ],
)
class ApiKeyControllerTest {

    @TestConfiguration
    class TestConfig {

        @Bean
        fun apiKeyService() = mockk<ApiKeyService>()

        @Bean
        fun userService() = mockk<UserService>()

        @Bean
        fun apiKeyMapper() = ApiKeyMapperImpl()

        @Bean
        fun userMapper() = UserMapperImpl()
    }

    @Autowired
    private lateinit var mvc: MockMvc

    @Autowired
    private lateinit var apiKeyService: ApiKeyService

    @Autowired
    private lateinit var secureUserService: UserService

    @Test
    @WithMockUser(username = PRINCIPAL_NAME)
    fun `Authenticated user can retrieve api keys`() {
        every { secureUserService.findById(user.id) } returns user

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
    @WithAnonymousUser
    fun `Unauthenticated user cannot retrieve api keys`() {
        mvc.get(ENDPOINT) {
        }.andExpect {
            status { isUnauthorized() }
        }
    }

    @Test
    @WithMockUser(username = PRINCIPAL_NAME, authorities = [READ_ROLE])
    fun `Authenticated user can create api keys`() {
        every { apiKeyService.createApiKey(any(), any(), any()) } returns apiKeyCreated

        val payload = mapOf(
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
            jsonPath("$.key", equalTo(apiKeyCreated.unHashedKey))
            jsonPath("$.name", equalTo(apiKeyCreated.name))
            jsonPath("$.authorities", equalTo(apiKeyCreated.authorities.map(Authority::toString)))
        }
        verify { apiKeyService.createApiKey(UUID.fromString(PRINCIPAL_NAME), apiKeyRequest.name, listOf(READ, WRITE, DELETE)) }
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
    @WithMockUser(username = PRINCIPAL_NAME)
    fun `Authenticated user can delete api keys`() {
        every { apiKeyService.deleteApiKey(any(), any()) } returns Unit

        mvc.delete("$ENDPOINT/${apiKey.id}") {
            with(csrf())
        }.andExpect {
            status { isOk() }
        }

        verify { apiKeyService.deleteApiKey(user.id, apiKey.id) }
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
