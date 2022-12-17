package com.example.config

import com.example.security.apikey.ApiKeyAuthenticationFilter
import com.example.security.apikey.HashGenerator
import com.example.security.apikey.model.UserEntityService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import java.security.SecureRandom

// TODO Should improve configurability
// TODO JWTs should always have a user in the db
// TODO add Integration test configuration

private val PUBLIC_ENDPOINTS = arrayOf(
    "/sso/login",
    "/swagger-ui/**",
    "/swagger-resources/**",
    "/v3/api-docs/**",
)

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig(
    private val userEntityService: UserEntityService,
    private val hashGenerator: HashGenerator,
) {

    @Bean
    fun configure(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf().ignoringAntMatchers("/**")
            .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and().authorizeRequests()
            .antMatchers(*PUBLIC_ENDPOINTS).permitAll()
            .anyRequest().authenticated()
            .and().addFilterBefore(apiKeyAuthenticationFilter(), BasicAuthenticationFilter::class.java)
            .authorizeRequests()
            .and().oauth2ResourceServer().jwt().jwtAuthenticationConverter(jwtAuthenticationConverter())
        return http.build()
    }

    @Bean
    fun secureRandom(): SecureRandom {
        return SecureRandom()
    }

    @Bean
    fun apiKeyAuthenticationFilter(): ApiKeyAuthenticationFilter {
        return ApiKeyAuthenticationFilter(userEntityService, hashGenerator)
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