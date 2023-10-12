package com.template.config.security.jwt

import org.springframework.context.ApplicationContextException
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter

@Configuration
class JwtRoleExtractor {

    /**
     * A custom [JwtAuthenticationConverter] capable of extracting authorities from a nested claim, specified by dot
     * notation. For example the Keycloak claim 'realm_access.roles'. All values inside the claim are translated
     * according to the configured role mappings, and default roles are assigned. Any issues while extracting roles
     * will result in only the default authorizations to be assigned. Any unknown roles are ignored.
     */
    @Bean
    fun jwtAuthConverter(jwtProperties: JwtProperties): JwtAuthenticationConverter {
        val claimStructure = jwtProperties.claims.authorities.split(".")

        if (claimStructure.isEmpty()) {
            throw ApplicationContextException("Required claim for authorities extraction is missing")
        }

        return JwtAuthenticationConverter().also { converter ->
            converter.setJwtGrantedAuthoritiesConverter { jwt ->
                runCatching { extractRoles(jwt, claimStructure) }
                    .getOrDefault(emptyList())
                    .flatMap { jwtProperties.roleMappings[it] ?: emptyList() }
                    .plus(jwtProperties.roleDefaults)
                    .distinct()
                    .map { SimpleGrantedAuthority(it.role()) }
            }
        }
    }

    private fun extractRoles(jwt: Jwt, claimStructure: List<String>): List<String> {
        var map: Map<*, *> = jwt.claims

        claimStructure.dropLast(1)
            .forEach { claim -> map = map[claim] as Map<*, *> }

        return claimStructure.last()
            .let { map[it] as List<*> }
            .map(Any?::toString)
    }
}
