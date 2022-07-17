package com.example.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.web.SecurityFilterChain

private val PERMITTED_ENDPOINTS = arrayOf(
    "/sso/login",
    "/swagger-ui/**",
    "/swagger-resources/**",
    "/v3/api-docs/**",
)

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention
@MustBeDocumented
@AuthenticationPrincipal(expression = "claims['sub']")
annotation class ResourceOwner

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfiguration {

    @Bean
    fun configure(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf().ignoringAntMatchers("/**")
            .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and().authorizeRequests()
            .antMatchers(*PERMITTED_ENDPOINTS).permitAll()
            .anyRequest().authenticated()
            .and().oauth2ResourceServer().jwt()
            .jwtAuthenticationConverter(jwtAuthenticationConverter())
        return http.build()
    }

    private fun jwtAuthenticationConverter(): JwtAuthenticationConverter {
        val jwtAuthenticationConverter = JwtAuthenticationConverter()
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(authoritiesConverter())
        return jwtAuthenticationConverter
    }

    // TODO Rewrite
    private fun authoritiesConverter(): (Jwt) -> Collection<GrantedAuthority> = { jwt ->
        val realmAccess = jwt.claims["realm_access"] as Map<* ,*>
        val roles = realmAccess["roles"] as List<*>
        roles.map { SimpleGrantedAuthority(it.toString()) }
    }
}
