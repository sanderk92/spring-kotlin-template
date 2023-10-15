package com.template.controller

import com.template.config.security.user.Authority
import com.template.config.security.user.READ_ROLE
import com.template.controller.interfaces.UserInterface.Companion.ENDPOINT
import com.template.controller.objects.PRINCIPAL_NAME
import com.template.controller.objects.user
import com.template.domain.UserService
import com.template.mappers.ApiKeyMapperImpl
import com.template.mappers.UserMapperImpl
import com.template.util.EnableAspectOrientedProgramming
import com.template.util.EnableGlobalMethodSecurity
import io.mockk.every
import io.mockk.mockk
import org.apache.catalina.security.SecurityConfig
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
        SecurityConfig::class,
        EnableGlobalMethodSecurity::class,
        EnableAspectOrientedProgramming::class,
    ],
)
internal class UserControllerTest {

    @Autowired
    private lateinit var mvc: MockMvc

    @Autowired
    private lateinit var userService: UserService

    @TestConfiguration
    class TestConfig {

        @Bean
        fun userService() = mockk<UserService>()

        @Bean
        fun apiKeyMapper() = ApiKeyMapperImpl()

        @Bean
        fun userMapper() = UserMapperImpl()
    }

    /*
    Contract tests
     */

    @Test
    @WithMockUser(username = PRINCIPAL_NAME, authorities = [READ_ROLE])
    fun `Users can be searched for`() {
        every { userService.search("some-query") } returns listOf(user)

        mvc.get(ENDPOINT) {
            param("query", "some-query")
        }.andExpect {
            status { isOk() }
            jsonPath("$.size()", equalTo(1))
            jsonPath("$[0].id", equalTo(user.id.toString()))
            jsonPath("$[0].email", equalTo(user.email))
            jsonPath("$[0].username", equalTo(user.username))
            jsonPath("$[0].firstName", equalTo(user.firstName))
            jsonPath("$[0].lastName", equalTo(user.lastName))
        }
    }

    @Test
    @WithMockUser(username = PRINCIPAL_NAME, authorities = [READ_ROLE])
    fun `User information can be retrieved`() {
        every { userService.findById(user.id) } returns user

        mvc.get("$ENDPOINT/me") {
        }.andExpect {
            status { isOk() }
            jsonPath("$.id", equalTo(user.id.toString()))
            jsonPath("$.email", equalTo(user.email))
            jsonPath("$.username", equalTo(user.username))
            jsonPath("$.firstName", equalTo(user.firstName))
            jsonPath("$.lastName", equalTo(user.lastName))
            jsonPath("$.authorities", hasItems(Authority.READ.toString()))
        }
    }

    @Test
    @WithMockUser(username = PRINCIPAL_NAME, authorities = [READ_ROLE])
    fun `User information for non-existing user cannot be retrieved`() {
        every { userService.findById(user.id) } returns null

        mvc.get("$ENDPOINT/me") {
        }.andExpect {
            status { isNotFound() }
        }
    }

    /*
    Security config tests
     */

    @Test
    @WithMockUser(username = PRINCIPAL_NAME, authorities = [])
    fun `Unauthorized user cannot search users`() {
        mvc.get(ENDPOINT) {
            param("query", "some-query")
        }.andExpect {
            status { isForbidden() }
        }
    }

    @Test
    @WithAnonymousUser
    fun `Unauthenticated user cannot search users`() {
        mvc.get(ENDPOINT) {
        }.andExpect {
            status { isUnauthorized() }
        }
    }

    @Test
    @WithMockUser(username = PRINCIPAL_NAME, authorities = [])
    fun `Unauthorized user cannot retrieve user information`() {
        mvc.get("$ENDPOINT/me") {
        }.andExpect {
            status { isForbidden() }
        }
    }

    @Test
    @WithAnonymousUser
    fun `Unauthenticated user cannot retrieve user information`() {
        mvc.get("$ENDPOINT/me") {
        }.andExpect {
            status { isUnauthorized() }
        }
    }
}
