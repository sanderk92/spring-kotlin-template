package com.example.config

import com.example.security.apikey.ApiKeyAuthenticationFilter
import com.example.security.user.JwtProperties
import com.example.security.user.StoreUserFilter
import com.example.security.user.UserAuthority
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
import javax.naming.ConfigurationException

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
    private val storeUserFilter: StoreUserFilter,
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
            .addFilterAfter(storeUserFilter, BearerTokenAuthenticationFilter::class.java)
            .addFilterAfter(apiKeyFilter, storeUserFilter::class.java)
            .oauth2ResourceServer { server -> server
                .jwt(Customizer.withDefaults())
            }
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
                    .map { SimpleGrantedAuthority(it.role) }
            }
        }
    }
}
