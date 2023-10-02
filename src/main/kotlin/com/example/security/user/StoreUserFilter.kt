package com.example.security.user

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.lang.IllegalArgumentException
import java.util.*

@Component
class StoreUserFilter(
    private val userService: UserService,
    private val claims: JwtClaims,
) : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val authentication: Authentication? = SecurityContextHolder.getContext().authentication
        if (authentication?.isAuthenticated == true && authentication is JwtAuthenticationToken) {
            userService.findById(UUID.fromString(authentication.name))
                ?.also { user -> updateUser(user, authentication) }
                ?: createUser(authentication)
        }
        chain.doFilter(request, response)
    }

    private fun updateUser(user: User, authentication: JwtAuthenticationToken) {
        val required = extractUserAuthorities(authentication)
        if (user.authorities.toSet() != required.toSet()) {
           userService.update(user.id, required)
        }
    }

    private fun createUser(authentication: JwtAuthenticationToken) =
        userService.create(
            userId = UUID.fromString(authentication.name),
            email = extractEmail(authentication),
            firstName = extractFirstName(authentication),
            lastName = extractLastName(authentication),
            authorities = extractUserAuthorities(authentication)
        )

    private fun extractUserAuthorities(authentication: JwtAuthenticationToken): List<UserAuthority> =
        authentication.authorities
            .map(GrantedAuthority::getAuthority)
            .mapNotNull(UserAuthority::valueOfRole)

    private fun extractEmail(authentication: JwtAuthenticationToken) =
        authentication.tokenAttributes[claims.email]?.toString()
            ?: throw IllegalArgumentException("Missing required claim in JWT: '${claims.email}'")

    private fun extractFirstName(authentication: JwtAuthenticationToken) =
        authentication.tokenAttributes[claims.firstName]?.toString()
            ?: throw IllegalArgumentException("Missing required claim in JWT: '${claims.firstName}'")

    private fun extractLastName(authentication: JwtAuthenticationToken) =
        authentication.tokenAttributes[claims.lastName]?.toString()
            ?: throw IllegalArgumentException("Missing required claim in JWT: '${claims.lastName}'")
}
