package com.example.common

import io.mockk.mockk
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationFilter
import org.springframework.security.web.SecurityFilterChain
import javax.servlet.*

/*
 * For Integration tests we can mock security by setting the SecurityContext in a filter and returning custom
 * JWTs from a mock JwtDecoder. To trigger this mock, we must simply supply a bearer token header with our requests,
 * which will be passed to our mock JwtDecoder.
 */

@Qualifier("MockJwtDecoder")
annotation class MockJwtDecoder

@Configuration("IT-SecurityConfiguration")
@Order(Ordered.LOWEST_PRECEDENCE)
class SecurityConfiguration {

    @Bean
    @MockJwtDecoder
    fun mockJwtDecoder() = mockk<JwtDecoder>()

    @Bean
    fun additionalConfiguration(http: HttpSecurity): SecurityFilterChain {
        http
            .addFilterBefore(mockSecurityContextFilter, BearerTokenAuthenticationFilter::class.java)
            .authorizeRequests()
            .antMatchers("/**").permitAll()
            .anyRequest().permitAll()
            .and().oauth2ResourceServer().jwt()
        return http.build()
    }
}

private val mockSecurityContextFilter = object : Filter {

    override fun init(filterConfig: FilterConfig) {}

    override fun doFilter(req: ServletRequest, res: ServletResponse?, chain: FilterChain) {
        val context = SecurityContextHolder.getContext()
        context.authentication = mockk<JwtAuthenticationToken>()
        chain.doFilter(req, res)
    }

    override fun destroy() {
        SecurityContextHolder.clearContext()
    }
}
