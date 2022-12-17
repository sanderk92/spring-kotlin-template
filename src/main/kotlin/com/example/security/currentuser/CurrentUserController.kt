package com.example.security.currentuser

import com.example.config.AuthSchemes.APIKEY
import com.example.config.AuthSchemes.OAUTH2
import com.example.security.apikey.model.ApiKeyAuthorities.READ
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping
@Tag(name = "User", description = "Retrieve information about the current user")
class CurrentUserController {

    @Operation(
        description = "Get the currently authorized user",
        security = [SecurityRequirement(name = OAUTH2), SecurityRequirement(name = APIKEY)]
    )
    @GetMapping("/me")
    @PreAuthorize("hasAuthority('$READ')")
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