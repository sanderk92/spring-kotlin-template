package com.example.security.apikey

import com.example.security.apikey.model.UserEntity
import com.example.security.apikey.model.UserEntityService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

private const val API_KEY_HEADER = "apikey"

class ApiKeyAuthenticationFilter(
    private val userRepository: UserEntityService,
    private val hashGenerator: HashGenerator,
) : OncePerRequestFilter() {

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        return request.servletPath.startsWith("/apikey")
    }

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        request.getHeader(API_KEY_HEADER)?.also { apiKey ->

            val hashedApiKey = hashGenerator.hash(apiKey)
            userRepository.findByApiKey(hashedApiKey)?.also { user ->

                val authorities = user.authoritiesFor(hashedApiKey)
                val authentication = ApiKeyAuthentication(user.id, hashedApiKey, authorities)

                val securityContext = SecurityContextHolder.createEmptyContext()
                securityContext.authentication = authentication
                SecurityContextHolder.setContext(securityContext)
            }
        }
        chain.doFilter(request, response)
    }
}

private fun UserEntity.authoritiesFor(apiKey: String): List<String> =
    this.apiKeys.firstOrNull { it.key == apiKey }?.authorities ?: emptyList()
