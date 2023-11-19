package com.template.config

import com.template.config.security.apikey.ApiKeyAuthenticationFilter
import com.template.config.security.jwt.JwtUserStorageFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

private val PUBLIC_ENDPOINTS = arrayOf(
    "/sso/login",
    "/swagger-ui/**",
    "/swagger-resources/**",
    "/v3/api-docs/**",
    "/actuator/health"
)

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
internal class SecurityConfig(
    private val apiKeyFilter: ApiKeyAuthenticationFilter,
    private val jwtUserStorageFilter: JwtUserStorageFilter,
) {

    @Bean
    fun configure(http: HttpSecurity): SecurityFilterChain = http
        .cors(Customizer.withDefaults())
        .csrf(Customizer.withDefaults())
        .authorizeHttpRequests { auth ->
            auth
                .requestMatchers(*PUBLIC_ENDPOINTS).permitAll()
                .anyRequest().authenticated()
        }
        .sessionManagement { session ->
            session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }
        .oauth2ResourceServer { server ->
            server
                .jwt(Customizer.withDefaults())
        }
        .addFilterAfter(jwtUserStorageFilter, BearerTokenAuthenticationFilter::class.java)
        .addFilterAfter(apiKeyFilter, jwtUserStorageFilter::class.java)
        .build()
}
