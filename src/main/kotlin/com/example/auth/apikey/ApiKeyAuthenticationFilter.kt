package com.example.auth.apikey

import com.example.auth.apikey.model.ApiKeyUser
import com.example.auth.apikey.model.ApiKeyUserService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

private const val API_KEY_HEADER = "apikey"

@Service
class ApiKeyAuthenticationFilter(
    private val userRepository: ApiKeyUserService,
    private val hashGenerator: HashGenerator,
) : OncePerRequestFilter() {

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        return request.servletPath.startsWith("/apikey")
    }

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        request.apiKeyHeader().ifPresent { apiKey ->
            val hashedApiKey = hashGenerator.hash(apiKey)
            userRepository.findByApiKey(hashedApiKey).ifPresent { user ->

                val authorities = user.authoritiesFor(hashedApiKey)
                val authentication = ApiKeyAuthentication(user.id, hashedApiKey, authorities)

                val securityContext = SecurityContextHolder.createEmptyContext()
                authentication.isAuthenticated = true
                securityContext.authentication = authentication
                SecurityContextHolder.setContext(securityContext)
            }
        }
        chain.doFilter(request, response)
    }
}

private fun HttpServletRequest.apiKeyHeader(): Optional<String> =
    Optional.ofNullable(this.getHeader(API_KEY_HEADER))

private fun ApiKeyUser.authoritiesFor(apiKey: String): List<String> =
    this.apiKeys.firstOrNull { it.key == apiKey }?.authorities ?: emptyList()
