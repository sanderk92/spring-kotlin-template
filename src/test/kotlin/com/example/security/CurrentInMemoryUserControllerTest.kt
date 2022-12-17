package com.example.security

import com.example.config.EnableGlobalMethodSecurity
import com.example.security.apikey.model.ApiKeyAuthorities.READ
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
@Import(value = [CurrentUserController::class, EnableGlobalMethodSecurity::class])
class CurrentInMemoryUserControllerTest {

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
    fun `User with no authorities gets a 403`() {
        mvc.get("/me") {
        }.andExpect {
            status { isForbidden() }
        }
    }

    @Test
    @WithMockUser(username = USERNAME, authorities = [READ])
    fun `User with 'Read' authority gets a 200`() {
        mvc.get("/me") {
        }.andExpect {
            status { isOk() }
            jsonPath("$.id", equalTo(USERNAME))
            jsonPath("$.authorities", hasItem(READ))
        }
    }
}
