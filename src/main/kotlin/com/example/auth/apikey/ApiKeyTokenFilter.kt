package com.example.auth.apikey

import com.example.auth.AuthenticatedUserRepository
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

private const val API_KEY_HEADER = "apikey"

@Service
class ApiKeyTokenFilter(
    private val userRepository: AuthenticatedUserRepository,
    private val hashGenerator: HashGenerator,
): OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        request.getHeader(API_KEY_HEADER)?.let { apiKey ->
            val hashedApiKey = hashGenerator.hash(apiKey)
            userRepository.findByApiKey(hashedApiKey).ifPresent { user ->
                val authorities = user.apiKey.firstOrNull { it.key == hashedApiKey }?.authorities ?: emptyList()
                val securityContext = SecurityContextHolder.createEmptyContext()
                securityContext.authentication = ApiKeyAuthenticationToken(user.subject, authorities)
                SecurityContextHolder.setContext(securityContext)
            }
        }
        chain.doFilter(request, response)
    }
}

data class ApiKeyAuthenticationToken(
    private val subject: String,
    private val authorities: List<GrantedAuthority>,
): Authentication {

    override fun getName(): String {
        return subject
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return authorities.toMutableList()
    }

    override fun getCredentials(): Any {
        return "N/A"
    }

    override fun getDetails(): Any {
        return "N/A"
    }

    override fun getPrincipal(): Any {
        return subject
    }

    override fun isAuthenticated(): Boolean {
        return true
    }

    override fun setAuthenticated(isAuthenticated: Boolean) {
        // No-op
    }
}
