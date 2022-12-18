package com.example.security.apikey

import com.example.security.apikey.model.User
import com.example.security.apikey.model.UserService
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Service
class ApiKeyAuthenticationFilter(
    private val userService: UserService,
    private val hashGenerator: HashGenerator,
    @Value("\${spring.security.api-key.path}") private val apiKeyPath: String,
    @Value("\${spring.security.api-key.header}") private val apiKeyHeader: String,
) : OncePerRequestFilter() {

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        return request.servletPath.startsWith(apiKeyPath)
    }

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        request.getHeader(apiKeyHeader)?.also { apiKey ->

            val hashedApiKey = hashGenerator.hash(apiKey)
            userService.findByApiKey(hashedApiKey)?.also { user ->

                val authorities = user.authoritiesFor(hashedApiKey)
                val authentication = ApiKeyAuthentication(user.id.toString(), hashedApiKey, authorities)

                val securityContext = SecurityContextHolder.createEmptyContext()
                securityContext.authentication = authentication
                SecurityContextHolder.setContext(securityContext)
            }
        }
        chain.doFilter(request, response)
    }
}

private fun User.authoritiesFor(apiKey: String): List<String> =
    this.apiKeys.firstOrNull { it.key == apiKey }?.authorities ?: emptyList()
