package com.example.config

import com.example.security.apikey.ApiKeyAuthenticationFilter
import com.example.security.user.CurrentUserStorageFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter
import org.springframework.security.web.SecurityFilterChain
import java.security.SecureRandom

private val PUBLIC_ENDPOINTS = arrayOf(
    "/sso/login",
    "/swagger-ui/**",
    "/swagger-resources/**",
    "/v3/api-docs/**",
)

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
class SecurityConfig(
    private val apiKeyFilter: ApiKeyAuthenticationFilter,
    private val currentUserStorageFilter: CurrentUserStorageFilter,
) {

    @Bean
    fun configure(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf(Customizer.withDefaults())
            .sessionManagement(Customizer.withDefaults())
            .authorizeHttpRequests { auth -> auth
                .requestMatchers(*PUBLIC_ENDPOINTS).permitAll()
                .anyRequest().authenticated()
            }
            .addFilterAfter(currentUserStorageFilter, BearerTokenAuthenticationFilter::class.java)
            .addFilterAfter(apiKeyFilter, currentUserStorageFilter::class.java)
            .oauth2ResourceServer { server -> server
                .jwt(Customizer.withDefaults())
            }
        return http.build()
    }

    @Bean
    fun jwtAuthenticationConverter(): JwtAuthenticationConverter {
        val jwtAuthenticationConverter = JwtAuthenticationConverter()
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter{ jwt ->
            val realmAccess = jwt.claims["realm_access"] as Map<*, *>
            val roles = realmAccess["roles"] as List<*>
            roles.map { SimpleGrantedAuthority(it.toString()) }
        }
        return jwtAuthenticationConverter
    }

    @Bean
    fun secureRandom(): SecureRandom {
        return SecureRandom()
    }
}