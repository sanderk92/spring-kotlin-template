package com.example.security.user

import com.example.security.apikey.model.UserEntityService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Service
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Service
class CreateUserFilter(private val userEntityService: UserEntityService) : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication != null && authentication is JwtAuthenticationToken && authentication.isAuthenticated) {
            userEntityService.createIfNotExists(UUID.fromString(authentication.name))
        }
        chain.doFilter(request, response)
    }
}