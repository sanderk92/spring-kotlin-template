package com.example.security.apikey

import com.example.config.EnableAspectOrientedProgramming
import com.example.security.PRINCIPAL_NAME
import com.example.config.EnableGlobalMethodSecurity
import com.example.security.apiKey
import com.example.security.apiKeyRequest
import com.example.security.apikey.model.UserService
import com.example.security.user
import com.example.security.user.CurrentUserController
import com.example.security.user.CurrentUserExtractorAspect
import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@WebMvcTest
@Import(value = [
    ApiKeyController::class,
    CurrentUserExtractorAspect::class,
    EnableGlobalMethodSecurity::class,
    EnableAspectOrientedProgramming::class,
])
class ApiKeyControllerTest {

    @TestConfiguration
    class TestConfig {

        @Bean
        fun apiKeyService() = mockk<ApiKeyService>()

        @Bean
        fun userService() = mockk<UserService>()
    }

    @Autowired
    private lateinit var mvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var apiKeyService: ApiKeyService

    @Autowired
    private lateinit var userService: UserService

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
