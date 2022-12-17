package com.example.security.currentuser

import com.example.config.EnableGlobalMethodSecurity
import com.example.security.apikey.model.ApiKeyAuthorities
import org.hamcrest.CoreMatchers
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
    @WithMockUser(username = USERNAME, authorities = [ApiKeyAuthorities.READ])
    fun `User which is authenticated get a 200`() {
        mvc.get("/me") {
        }.andExpect {
            status { isOk() }
            jsonPath("$.id", CoreMatchers.equalTo(USERNAME))
            jsonPath("$.authorities", CoreMatchers.hasItem(ApiKeyAuthorities.READ))
        }
    }
}