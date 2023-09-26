package com.example.security.apikey

import com.example.security.user.UserService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.web.filter.OncePerRequestFilter

@Service
class ApiKeyAuthenticationFilter(
    private val userService: UserService,
    private val hashGenerator: HashGenerator,
    @Value("\${spring.security.api-key.path}") private val apiKeyPath: String,
) : OncePerRequestFilter() {

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        return request.servletPath.startsWith(apiKeyPath)
    }

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        request.getHeader("apikey")?.also { apiKey ->

            val hashedApiKey = hashGenerator.hash(apiKey)
            userService.findByApiKey(hashedApiKey)?.also { user ->

                val authorities = user.apiKeys.firstOrNull { it.key == apiKey }?.authorities ?: emptyList()
                val authentication = ApiKeyAuthentication(user.id.toString(), hashedApiKey, authorities)

                val securityContext = SecurityContextHolder.createEmptyContext()
                securityContext.authentication = authentication
                SecurityContextHolder.setContext(securityContext)
            }
        }
        chain.doFilter(request, response)
    }
}
