package com.template.config

import com.template.security.apikey.ApiKeyAuthenticationFilter
import com.template.security.user.JwtProperties
import com.template.security.user.UserStorageFilter
import org.springframework.context.ApplicationContextException
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
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
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
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
     * notation. For example the Keycloak claim 'realm_access.roles'. All values inside the claim are translated
     * according to the configured role mappings, and default roles are assigned. Any issues while extracting roles
     * will result in an empty list of authorizations, and unknown roles are ignored.
     */
    @Bean
    fun jwtAuthConverter(jwtProperties: JwtProperties): JwtAuthenticationConverter {
        val claimStructure = jwtProperties.claims.authorities.split(".")

        if (claimStructure.isEmpty()) {
            throw ApplicationContextException("Required claim for authorities extraction is missing")
        }

        return JwtAuthenticationConverter().also { converter ->
            converter.setJwtGrantedAuthoritiesConverter { jwt ->
                tryExtractRoles(jwt, claimStructure)
                    .flatMap { jwtProperties.roleMappings[it] ?: emptyList() }
                    .plus(jwtProperties.roleDefaults)
                    .distinct()
                    .map { SimpleGrantedAuthority(it.role()) }
            }
        }
    }
}

private fun tryExtractRoles(jwt: Jwt, claimStructure: List<String>): List<String> =
    try {
        extractRoles(jwt, claimStructure)
    } catch (e: Exception) {
        emptyList()
    }

private fun extractRoles(jwt: Jwt, claimStructure: List<String>): List<String> {
    var map: Map<*, *> = jwt.claims

    claimStructure.dropLast(1)
        .forEach { claim -> map = map[claim] as Map<*, *> }

    return claimStructure.last()
        .let { map[it] as List<*> }
        .map(Any?::toString)
}
