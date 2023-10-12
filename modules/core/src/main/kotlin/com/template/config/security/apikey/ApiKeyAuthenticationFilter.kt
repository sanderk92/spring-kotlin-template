package com.template.config.security.apikey

import com.template.controller.interfaces.ApiKeyInterface
import com.template.config.security.user.UserAuthority
import com.template.config.security.user.SecureUserService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class ApiKeyAuthenticationFilter(
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

                val key = user.apiKeys.first { it.key == hashedApiKey }
                val authorities = user.authorities.filter { key.hasAuthority(it) }

                val context = SecurityContextHolder.createEmptyContext()
                context.authentication = ApiKeyAuthentication(user.id.toString(), hashedApiKey, authorities)
                SecurityContextHolder.setContext(context)
            }
        }
        chain.doFilter(request, response)
    }

    private fun ApiKey.hasAuthority(authority: UserAuthority) = when (authority) {
        UserAuthority.READ -> this.read
        UserAuthority.WRITE -> this.write
        UserAuthority.DELETE -> this.delete
        UserAuthority.ADMIN -> false
    }
}
