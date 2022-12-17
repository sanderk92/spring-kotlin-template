package com.example.config

import io.mockk.every
import io.mockk.mockk
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true)
class ITSecurityConfiguration {

    @Bean
    fun mockAuthenticationProvider(): MockAuthenticationProvider {
        val mock = mockk<MockAuthenticationProvider>()
        every { mock.provide() } returns Optional.empty()
        return mock
    }

    @Bean
    fun configure(http: HttpSecurity): SecurityFilterChain {
        http
            .addFilterBefore(
                MockAuthenticationProviderFilter(mockAuthenticationProvider()),
                BasicAuthenticationFilter::class.java,
            )
            .authorizeRequests()
            .antMatchers("/**").permitAll()
            .anyRequest().permitAll()
        return http.build()
    }
}

interface MockAuthenticationProvider {
    fun provide(): Optional<Authentication>
}

class MockAuthenticationProviderFilter(private val provider: MockAuthenticationProvider) : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        provider.provide().ifPresent { authentication ->
            val context = SecurityContextHolder.getContext()
            context.authentication = authentication
            chain.doFilter(request, response)
        }
    }
}
