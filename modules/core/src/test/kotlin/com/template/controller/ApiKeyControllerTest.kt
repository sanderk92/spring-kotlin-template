package com.template.controller

import com.template.config.security.apikey.SecureApiKeyService
import com.template.config.security.user.SecureUserService
import com.template.config.security.user.UserAuthority
import com.template.controller.interfaces.ApiKeyInterface.Companion.ENDPOINT
import com.template.controller.objects.*
import com.template.domain.UserService
import com.template.util.EnableAspectOrientedProgramming
import com.template.util.EnableGlobalMethodSecurity
import com.template.util.asJson
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
        fun apiKeyService() = mockk<SecureApiKeyService>()

        @Bean
        fun userService() = mockk<UserService>()
    }

    @Autowired
    private lateinit var mvc: MockMvc

    @Autowired
    private lateinit var apiKeyService: SecureApiKeyService

    @Autowired
    private lateinit var secureUserService: SecureUserService

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
            jsonPath("$[0].read", equalTo(apiKey.read))
            jsonPath("$[0].write", equalTo(apiKey.write))
            jsonPath("$[0].delete", equalTo(apiKey.delete))
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
    @WithMockUser(username = PRINCIPAL_NAME, authorities = [UserAuthority.READ.role])
    fun `Authenticated user can create api keys`() {
        every { apiKeyService.create(any()) } returns unHashedApiKeyEntry
        every { apiKeyService.hash(any()) } returns hashedApiKeyEntry
        every { secureUserService.addApiKey(any(), any()) } returns apiKey

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
            jsonPath("$.key", equalTo(unHashedApiKeyEntry.key))
            jsonPath("$.name", equalTo(unHashedApiKeyEntry.name))
            jsonPath("$.read", equalTo(unHashedApiKeyEntry.read))
            jsonPath("$.write", equalTo(unHashedApiKeyEntry.write))
            jsonPath("$.delete", equalTo(unHashedApiKeyEntry.delete))
        }
        verify { apiKeyService.create(apiKeyRequest) }
        verify { apiKeyService.hash(unHashedApiKeyEntry) }
        verify { secureUserService.addApiKey(user.id, hashedApiKeyEntry) }
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
        every { secureUserService.deleteApiKey(any(), any()) } returns Unit

        mvc.delete("$ENDPOINT/${apiKey.id}") {
            with(csrf())
        }.andExpect {
            status { isOk() }
        }

        verify { secureUserService.deleteApiKey(user.id, apiKey.id) }
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
