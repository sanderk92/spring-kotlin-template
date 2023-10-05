package com.template.config

import com.template.security.apikey.ApiKeyAuthenticationFilter
import com.template.security.user.JwtProperties
import com.template.security.user.UserStorageFilter
import javax.naming.ConfigurationException
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter
import org.springframework.security.web.SecurityFilterChain

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
    private val userStorageFilter: UserStorageFilter,
) {

    @Bean
    fun configure(http: HttpSecurity): SecurityFilterChain {
        http
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
            .addFilterAfter(userStorageFilter, BearerTokenAuthenticationFilter::class.java)
            .addFilterAfter(apiKeyFilter, userStorageFilter::class.java)
        return http.build()
    }

    /**
     * A custom [JwtAuthenticationConverter] capable of extracting authorities from a nested claim, specified by dot
     * notation. For example the Keycloak claim 'realm_access.roles'. All values inside the claim are translated to
     * [UserAuthority] according to the configured role mappings.
     */
    @Bean
    fun jwtAuthConverter(jwtProperties: JwtProperties): JwtAuthenticationConverter {
        val claimStructure = jwtProperties.claims.authorities.split(".")

        if (claimStructure.isEmpty()) {
            throw ConfigurationException("Invalid claim for extracting authorities: '${jwtProperties.claims.authorities}'")
        }

        return JwtAuthenticationConverter().also { converter ->
            converter.setJwtGrantedAuthoritiesConverter { jwt ->
                var map: Map<*, *> = jwt.claims

                claimStructure.dropLast(1)
                    .forEach { claim -> map = map[claim] as Map<*, *> }

                claimStructure.last()
                    .let { map[it] as List<*> }
                    .flatMap { jwtProperties.roleMappings[it.toString()] ?: emptyList() }
                    .distinct()
                    .map { SimpleGrantedAuthority(it.value()) }
            }
        }
    }
}