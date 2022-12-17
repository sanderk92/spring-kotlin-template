package com.example.security.user

import com.example.security.PRINCIPAL_NAME
import com.example.config.EnableGlobalMethodSecurity
import com.example.security.apikey.ApiKeyAuthorities
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

@WebMvcTest
@Import(value = [CurrentUserController::class, EnableGlobalMethodSecurity::class])
class CurrentUserInformationControllerTest {

    @Autowired
    private lateinit var mvc: MockMvc

    @Test
    @WithAnonymousUser
    fun `Unauthenticated user gets a 401 when retrieving user information`() {
        mvc.get("/me") {
        }.andExpect {
            status { isUnauthorized() }
        }
    }

    @Test
    @WithMockUser(username = PRINCIPAL_NAME, authorities = [ApiKeyAuthorities.READ])
    fun `Authenticated user can retrieve user information`() {
        mvc.get("/me") {
        }.andExpect {
            status { isOk() }
            jsonPath("$.id", equalTo(PRINCIPAL_NAME))
            jsonPath("$.authorities", hasItem(ApiKeyAuthorities.READ))
        }
    }
}