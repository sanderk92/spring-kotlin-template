package com.template.config.security.apikey

import com.template.config.security.user.SecureUserService
import com.template.controller.interfaces.ApiKeyInterface
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
internal class ApiKeyAuthenticationFilter(
    private val secureUserService: SecureUserService,
    private val hashGenerator: HashGenerator,
) : OncePerRequestFilter() {

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        return request.servletPath.startsWith(ApiKeyInterface.ENDPOINT)
    }

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        request.getHeader("API-Key")?.also { apiKey ->

            val hashedApiKey = hashGenerator.hash(apiKey)
            secureUserService.findByApiKey(hashedApiKey)?.also { user ->

                val key = user.apiKeys.first { it.hashedKey == hashedApiKey }
                val context = SecurityContextHolder.createEmptyContext()
                context.authentication = ApiKeyAuthentication(user.id.toString(), hashedApiKey, key.authorities)
                SecurityContextHolder.setContext(context)
            }
        }
        chain.doFilter(request, response)
    }
}
