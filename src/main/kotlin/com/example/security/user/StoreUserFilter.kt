package com.example.security.user

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken

@Service
class StoreUserFilter(
    private val userService: UserService,
    @Value("\${security.token.claims.email}") private val emailClaim: String,
    @Value("\${security.token.claims.first-name}") private val firstNameClaim: String,
    @Value("\${security.token.claims.last-name}") private val lastNameClaim: String,
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