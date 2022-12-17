package com.example.config

import com.example.security.apikey.ApiKeyAuthenticationFilter
import com.example.security.user.CreateUserFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationFilter
import org.springframework.security.web.SecurityFilterChain

private val PUBLIC_ENDPOINTS = arrayOf(
    "/sso/login",
    "/swagger-ui/**",
    "/swagger-resources/**",
    "/v3/api-docs/**",
)

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig(
    private val apiKeyFilter: ApiKeyAuthenticationFilter,
    private val createUserFilter: CreateUserFilter,
) {

    @Bean
    fun configure(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf().ignoringAntMatchers("/**")
            .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and().authorizeRequests()
            .antMatchers(*PUBLIC_ENDPOINTS).permitAll()
            .anyRequest().authenticated()
            .and()
            .addFilterAfter(createUserFilter, BearerTokenAuthenticationFilter::class.java)
            .addFilterAfter(apiKeyFilter, createUserFilter::class.java)
            .authorizeRequests()
            .and().oauth2ResourceServer().jwt().jwtAuthenticationConverter(jwtAuthenticationConverter())
        return http.build()
    }

    private fun jwtAuthenticationConverter(): JwtAuthenticationConverter {
        val jwtAuthenticationConverter = JwtAuthenticationConverter()
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(authoritiesConverter())
        return jwtAuthenticationConverter
    }

    private fun authoritiesConverter(): (Jwt) -> Collection<GrantedAuthority> = { jwt ->
        val realmAccess = jwt.claims["realm_access"] as Map<*, *>
        val roles = realmAccess["roles"] as List<*>
        roles.map { SimpleGrantedAuthority(it.toString()) }
    }
}