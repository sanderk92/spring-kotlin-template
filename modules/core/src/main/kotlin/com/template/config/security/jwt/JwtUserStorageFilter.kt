package com.template.config.security.jwt

import com.template.config.security.user.SecureUser
import com.template.config.security.user.SecureUserEntry
import com.template.config.security.user.SecureUserService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import java.util.UUID
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.filter.OncePerRequestFilter

@Component
internal class JwtUserStorageFilter(
    private val secureUserService: SecureUserService,
    private val userInfoProperties: UserInfoProperties,
    private val restTemplate: RestTemplate,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
    ) {
        val authentication: Authentication? = SecurityContextHolder.getContext().authentication
        if (authentication?.isAuthenticated == true && authentication is JwtAuthenticationToken) {
            secureUserService.findById(extractId(authentication)) ?: createUser(authentication)
        }
        chain.doFilter(request, response)
    }

    private fun createUser(authentication: JwtAuthenticationToken): SecureUser {
        val userInfo = fetchUserInfo(authentication)
        return secureUserService.create(
            SecureUserEntry(
                id = extractId(authentication),
                email = extractEmail(userInfo),
                username = extractUsername(userInfo),
                firstName = extractFirstName(userInfo),
                lastName = extractLastName(userInfo),
            ),
        )
    }

    private fun fetchUserInfo(authentication: JwtAuthenticationToken): Map<String, String> {
        val headers = HttpHeaders()
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer ${authentication.token.tokenValue}")
        val request = HttpEntity(null, headers)
        val responseType = object : ParameterizedTypeReference<Map<String, String>>() {}
        val response = restTemplate.exchange(userInfoProperties.endpoint, HttpMethod.GET, request, responseType)
        return response.body ?: throw IllegalStateException("Failed to fetch user information: $response")
    }

    private fun extractId(authentication: JwtAuthenticationToken) =
        runCatching { UUID.fromString(authentication.name) }.getOrNull()
            ?: throw IllegalArgumentException("Invalid or missing id in JWT: '${authentication.name}'")

    private fun extractEmail(map: Map<String, String>) =
        map[userInfoProperties.email]
            ?: throw IllegalArgumentException("Missing required user info: '${userInfoProperties.email}'")

    private fun extractUsername(map: Map<String, String>) =
        map[userInfoProperties.username]
            ?: throw IllegalArgumentException("Missing required user info: '${userInfoProperties.username}'")

    private fun extractFirstName(map: Map<String, String>) =
        map[userInfoProperties.firstName]
            ?: throw IllegalArgumentException("Missing required user info: '${userInfoProperties.firstName}'")

    private fun extractLastName(map: Map<String, String>) =
        map[userInfoProperties.lastName]
            ?: throw IllegalArgumentException("Missing required user info: '${userInfoProperties.lastName}'")
}
