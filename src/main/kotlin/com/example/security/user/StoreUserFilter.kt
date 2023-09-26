package com.example.security.user

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Service
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*

@Service
class StoreUserFilter(
    private val userService: UserService,
    @Value("\${spring.security.token.claims.email}") private val emailClaim: String,
    @Value("\${spring.security.token.claims.first-name}") private val firstNameClaim: String,
    @Value("\${spring.security.token.claims.last-name}") private val lastNameClaim: String,
) : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val authentication: Authentication? = SecurityContextHolder.getContext().authentication
        if (authentication?.isAuthenticated == true && authentication is JwtAuthenticationToken) {
            userService.findOrCreate(
                userId = UUID.fromString(authentication.name),
                email = authentication.tokenAttributes[emailClaim].toString(),
                firstName = authentication.tokenAttributes[firstNameClaim].toString(),
                lastName = authentication.tokenAttributes[lastNameClaim].toString(),
            )
        }
        chain.doFilter(request, response)
    }
}