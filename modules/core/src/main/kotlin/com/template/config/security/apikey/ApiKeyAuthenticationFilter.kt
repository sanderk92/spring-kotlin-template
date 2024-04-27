package com.template.config.security.apikey

import com.template.config.security.user.SecureApiKeyService
import com.template.controller.interfaces.ApiKeyInterface
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
internal class ApiKeyAuthenticationFilter(
    private val secureApiKeyService: SecureApiKeyService,
    private val hashGenerator: HashGenerator,
) : OncePerRequestFilter() {
    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        return request.servletPath.startsWith(ApiKeyInterface.ENDPOINT)
    }

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        request.getHeader("API-Key")?.also { apiKey ->

            val hashedApiKey = hashGenerator.hash(apiKey)
            secureApiKeyService.findByApiKey(hashedApiKey)?.also { key ->

                val context = SecurityContextHolder.createEmptyContext()
                context.authentication = ApiKeyAuthentication(key.user.id.value.toString(), hashedApiKey, key.authorities)
                SecurityContextHolder.setContext(context)
            }
        }
        chain.doFilter(request, response)
    }
}
