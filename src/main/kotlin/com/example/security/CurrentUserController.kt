package com.example.security

import com.example.security.apikey.model.ApiKeyAuthorities.READ_AUTHORITY
import com.example.security.apikey.model.ApiKeyUserRoles.USER_ROLE
import com.example.config.AuthSchemes.APIKEY
import com.example.config.AuthSchemes.OAUTH2
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PostAuthorize
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping
class CurrentUserController {

    @Operation(
        description = "Get the currently authorized user",
        security = [SecurityRequirement(name = OAUTH2), SecurityRequirement(name = APIKEY)]
    )
    @GetMapping("/me")
    @PreAuthorize("hasAuthority('$USER_ROLE') or hasAuthority('$READ_AUTHORITY') or false")
    fun getCurrentUser(

        @Parameter(hidden = true)
        authentication: Authentication,

    ): ResponseEntity<CurrentUser> =
        ResponseEntity.ok(
            CurrentUser(
                id = authentication.name,
                authorities = authentication.authorities.map(GrantedAuthority::getAuthority)
            )
        )
}

data class CurrentUser(
    val id: String,
    val authorities: List<String>,
)