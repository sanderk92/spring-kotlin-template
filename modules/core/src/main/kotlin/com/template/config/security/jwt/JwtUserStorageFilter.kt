package com.template.config.security.jwt

import com.template.config.security.user.Authority
import com.template.config.security.user.SecureUser
import com.template.config.security.user.SecureUserEntry
import com.template.config.security.user.SecureUserService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import java.util.*
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
internal class JwtUserStorageFilter(
    private val secureUserService: SecureUserService,
    private val claims: JwtClaims,
) : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val authentication: Authentication? = SecurityContextHolder.getContext().authentication
        if (authentication?.isAuthenticated == true && authentication is JwtAuthenticationToken) {
            secureUserService.findById(extractId(authentication))
                ?.also { user -> updateUser(user, authentication) }
                ?: createUser(authentication)
        }
        chain.doFilter(request, response)
    }

    private fun updateUser(secureUser: SecureUser, authentication: JwtAuthenticationToken) {
        val authorities = extractAuthorities(authentication)
        if (secureUser.authorities.toSet() != authorities.toSet()) {
            secureUserService.update(secureUser.id, authorities)
        }
    }

    private fun createUser(authentication: JwtAuthenticationToken) =
        secureUserService.create(
            SecureUserEntry(
                id = extractId(authentication),
                email = extractEmail(authentication),
                username = extractUsername(authentication),
                firstName = extractFirstName(authentication),
                lastName = extractLastName(authentication),
                authorities = extractAuthorities(authentication)
            )
        )

    private fun extractId(authentication: JwtAuthenticationToken) =
        runCatching { UUID.fromString(authentication.name) }.getOrNull()
            ?: throw IllegalArgumentException("Invalid or missing id in JWT: '${authentication.name}'")

    private fun extractEmail(authentication: JwtAuthenticationToken) =
        authentication.tokenAttributes[claims.email]?.toString()
            ?: throw IllegalArgumentException("Missing required claim in JWT: '${claims.email}'")

    private fun extractUsername(authentication: JwtAuthenticationToken) =
        authentication.tokenAttributes[claims.username]?.toString()
            ?: throw IllegalArgumentException("Missing required claim in JWT: '${claims.username}'")

    private fun extractFirstName(authentication: JwtAuthenticationToken) =
        authentication.tokenAttributes[claims.firstName]?.toString()
            ?: throw IllegalArgumentException("Missing required claim in JWT: '${claims.firstName}'")

    private fun extractLastName(authentication: JwtAuthenticationToken) =
        authentication.tokenAttributes[claims.lastName]?.toString()
            ?: throw IllegalArgumentException("Missing required claim in JWT: '${claims.lastName}'")

    private fun extractAuthorities(authentication: JwtAuthenticationToken): List<Authority> =
        authentication.authorities
            .map(GrantedAuthority::getAuthority)
            .mapNotNull(Authority.Companion::valueOfRole)
}
