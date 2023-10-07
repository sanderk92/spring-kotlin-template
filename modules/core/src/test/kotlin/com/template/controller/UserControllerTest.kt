package com.template.controller

import com.template.controller.interfaces.UserInterface.Companion.ENDPOINT
import com.template.controller.objects.PRINCIPAL_NAME
import com.template.controller.objects.user
import com.template.security.user.UserAuthority
import com.template.security.user.UserService
import com.template.util.EnableAspectOrientedProgramming
import com.template.util.EnableGlobalMethodSecurity
import io.mockk.every
import io.mockk.mockk
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.hasItems
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
@Import(
    value = [
        UserController::class,
        EnableGlobalMethodSecurity::class,
        EnableAspectOrientedProgramming::class,
    ],
)
class UserControllerTest {

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
    @WithMockUser(username = PRINCIPAL_NAME, authorities = [UserAuthority.READ.role])
    fun `Authenticated user can search users`() {
        every { userService.search("some-query") } returns listOf(user)

        mvc.get(ENDPOINT) {
            param("query", "some-query")
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
    @WithMockUser(username = PRINCIPAL_NAME, authorities = [])
    fun `Unauthorized user gets a 403 when searching users`() {
        mvc.get(ENDPOINT) {
            param("query", "some-query")
        }.andExpect {
            status { isForbidden() }
        }
    }

    @Test
    @WithAnonymousUser
    fun `Unauthenticated user gets a 401 when searching users`() {
        mvc.get(ENDPOINT) {
        }.andExpect {
            status { isUnauthorized() }
        }
    }

    @Test
    @WithMockUser(username = PRINCIPAL_NAME, authorities = [UserAuthority.READ.role])
    fun `Authenticated user can retrieve user information`() {
        every { userService.findById(user.id) } returns user

        mvc.get("$ENDPOINT/me") {
        }.andExpect {
            status { isOk() }
            jsonPath("$.id", equalTo(user.id.toString()))
            jsonPath("$.email", equalTo(user.email))
            jsonPath("$.firstName", equalTo(user.firstName))
            jsonPath("$.lastName", equalTo(user.lastName))
            jsonPath("$.authorities", hasItems("READ"))
        }
    }

    @Test
    @WithMockUser(username = PRINCIPAL_NAME, authorities = [])
    fun `Unauthorized user gets a 403 when retrieving user information`() {
        mvc.get("$ENDPOINT/me") {
        }.andExpect {
            status { isForbidden() }
        }
    }

    @Test
    @WithAnonymousUser
    fun `Unauthenticated user gets a 401 when retrieving user information`() {
        mvc.get("$ENDPOINT/me") {
        }.andExpect {
            status { isUnauthorized() }
        }
    }
}
