// package com.example.security.apikey
//
// import com.example.controller.ApiKeyController
// import com.example.util.EnableAspectOrientedProgramming
// import com.example.util.EnableGlobalMethodSecurity
// import com.example.security.*
// import com.example.security.user.UserService
// import com.fasterxml.jackson.databind.ObjectMapper
// import io.mockk.every
// import io.mockk.mockk
// import io.mockk.verify
// import org.hamcrest.CoreMatchers.equalTo
// import org.junit.jupiter.api.Test
// import org.springframework.beans.factory.annotation.Autowired
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
// import org.springframework.boot.test.context.TestConfiguration
// import org.springframework.context.annotation.Bean
// import org.springframework.context.annotation.Import
// import org.springframework.http.MediaType
// import org.springframework.security.test.context.support.WithAnonymousUser
// import org.springframework.security.test.context.support.WithMockUser
// import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
// import org.springframework.test.web.servlet.MockMvc
// import org.springframework.test.web.servlet.delete
// import org.springframework.test.web.servlet.get
// import org.springframework.test.web.servlet.post
//
// @WebMvcTest
// @Import(
//    value = [
//        ApiKeyController::class,
//        EnableGlobalMethodSecurity::class,
//        EnableAspectOrientedProgramming::class,
//    ]
// )
// class ApiKeyControllerTest {
//
//    @TestConfiguration
//    class TestConfig {
//
//        @Bean
//        fun apiKeyService() = mockk<ApiKeyService>()
//
//        @Bean
//        fun userService() = mockk<UserService>()
//    }
//
//    @Autowired
//    private lateinit var mvc: MockMvc
//
//    @Autowired
//    private lateinit var objectMapper: ObjectMapper
//
//    @Autowired
//    private lateinit var apiKeyService: ApiKeyService
//
//    @Autowired
//    private lateinit var userService: UserService
//
//    @Test
//    @WithMockUser(username = PRINCIPAL_NAME)
//    fun `Authenticated user can retrieve api keys`() {
//        every { userService.findById(user.id) } returns user
//
//        mvc.get("/key") {
//        }.andExpect {
//            status { isOk() }
//            content { equalTo(objectMapper.writeValueAsString(user)) }
//        }
//    }
//
//    @Test
//    @WithAnonymousUser
//    fun `Unauthenticated user cannot retrieve api keys`() {
//        mvc.get("/key") {
//        }.andExpect {
//            status { isUnauthorized() }
//        }
//    }
//
//    @Test
//    @WithMockUser(username = PRINCIPAL_NAME)
//    fun `Authenticated user can create api keys`() {
//        every { apiKeyService.create(any()) } returns unHashedApiKeyEntry
//        every { apiKeyService.hash(any()) } returns hashedApiKeyEntry
//        every { userService.addApiKey(any(), any()) } returns apiKey
//
//        mvc.post("/key") {
//            with(csrf())
//            content = objectMapper.writeValueAsString(apiKeyRequest)
//            contentType = MediaType.APPLICATION_JSON
//        }.andExpect {
//            status { isOk() }
//            content { equalTo(objectMapper.writeValueAsString(unHashedApiKeyEntry)) }
//        }
//        verify { apiKeyService.create(apiKeyRequest) }
//        verify { apiKeyService.hash(unHashedApiKeyEntry) }
//        verify { userService.addApiKey(user.id, hashedApiKeyEntry) }
//    }
//
//    @Test
//    @WithAnonymousUser
//    fun `Unauthenticated user cannot create api keys`() {
//        mvc.post("/key") {
//            with(csrf())
//        }.andExpect {
//            status { isUnauthorized() }
//        }
//    }
//
//    @Test
//    @WithMockUser(username = PRINCIPAL_NAME)
//    fun `Authenticated user can delete api keys`() {
//        every { userService.deleteApiKey(any(), any()) } returns Unit
//
//        mvc.delete("/key/${apiKey.id}") {
//            with(csrf())
//        }.andExpect {
//            status { isOk() }
//        }
//
//        verify { userService.deleteApiKey(user.id, apiKey.id) }
//    }
//
//    @Test
//    @WithAnonymousUser
//    fun `Unauthenticated user cannot delete api keys`() {
//        mvc.delete("/key/${apiKey.id}") {
//            with(csrf())
//        }.andExpect {
//            status { isUnauthorized() }
//        }
//    }
// }
