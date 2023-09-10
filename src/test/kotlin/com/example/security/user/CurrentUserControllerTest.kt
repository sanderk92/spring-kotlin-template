package com.example.security.user

import com.example.config.EnableAspectOrientedProgramming
import com.example.config.EnableGlobalMethodSecurity
import com.example.security.PRINCIPAL_NAME
import com.example.security.user
import io.mockk.every
import io.mockk.mockk
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.hasItem
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@WebMvcTest
@Import(value = [
    CurrentUserController::class,
    EnableGlobalMethodSecurity::class,
    EnableAspectOrientedProgramming::class,
])
class CurrentUserControllerTest {

    @Autowired
    private lateinit var mvc: MockMvc

    @Autowired
    private lateinit var userService: UserService

    @TestConfiguration
    class TestConfig {
        @Bean
        fun userService() = mockk<UserService>()
    }

    @Test
    @WithMockUser(username = PRINCIPAL_NAME, authorities = ["test"])
    fun `Authenticated user can search users`() {
        every { userService.search(user.email, user.firstName, user.lastName) } returns listOf(user)

        mvc.get("/user") {
            param("email", user.email)
            param("firstName", user.firstName)
            param("lastName", user.lastName)
        }.andExpect {
            status { isOk() }
            jsonPath("$.size()", equalTo(1))
            jsonPath("$[0].id", equalTo(user.id.toString()))
            jsonPath("$[0].email", equalTo(user.email))
            jsonPath("$[0].firstName", equalTo(user.firstName))
            jsonPath("$[0].lastName", equalTo(user.lastName))
        }
    }

    @Test
    @WithAnonymousUser
    fun `Unauthenticated user gets a 401 when searching users`() {
        mvc.get("/user") {
        }.andExpect {
            status { isUnauthorized() }
        }
    }

    @Test
    @WithMockUser(username = PRINCIPAL_NAME, authorities = ["test"])
    fun `Authenticated user can retrieve user information`() {
        every { userService.findById(user.id) } returns user

        mvc.get("/user/me") {
        }.andExpect {
            status { isOk() }
            jsonPath("$.id", equalTo(user.id.toString()))
            jsonPath("$.email", equalTo(user.email))
            jsonPath("$.firstName", equalTo(user.firstName))
            jsonPath("$.lastName", equalTo(user.lastName))
            jsonPath("$.authorities", hasItem("test"))
        }
    }

    @Test
    @WithAnonymousUser
    fun `Unauthenticated user gets a 401 when retrieving user information`() {
        mvc.get("/user/me") {
        }.andExpect {
            status { isUnauthorized() }
        }
    }
}