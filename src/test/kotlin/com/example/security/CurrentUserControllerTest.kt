package com.example.security

import com.example.config.TestSecurityConfig
import com.example.security.apikey.model.ApiKeyAuthorities.READ_AUTHORITY
import com.example.security.apikey.model.ApiKeyUserRoles.USER_ROLE
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.hasItem
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

private const val USERNAME = "username"

@WebMvcTest
@Import(value = [CurrentUserController::class, TestSecurityConfig::class])
class CurrentUserControllerTest {

    @Autowired
    private lateinit var mvc: MockMvc

    @Test
    @WithAnonymousUser
    fun `User which is unauthenticated gets a 401`() {
        mvc.get("/me") {
        }.andExpect {
            status { isUnauthorized() }
        }
    }

    @Test
    @WithMockUser(username = USERNAME, roles = [], authorities = [])
    fun `User which has no authorities gets a 403`() {
        mvc.get("/me") {
        }.andExpect {
            status { isForbidden() }
        }
    }

    @Test
    @WithMockUser(username = USERNAME, authorities = [USER_ROLE])
    fun `User which has 'User' authority gets a 200`() {
        mvc.get("/me") {
        }.andExpect {
            status { isOk() }
            jsonPath("$.id", equalTo(USERNAME))
            jsonPath("$.authorities", hasItem(USER_ROLE))
        }
    }

    @Test
    @WithMockUser(username = USERNAME, authorities = [READ_AUTHORITY])
    fun `User which has 'Read' authority gets a 200`() {
        mvc.get("/me") {
        }.andExpect {
            status { isOk() }
            jsonPath("$.id", equalTo(USERNAME))
            jsonPath("$.authorities", hasItem(READ_AUTHORITY))
        }
    }
}
