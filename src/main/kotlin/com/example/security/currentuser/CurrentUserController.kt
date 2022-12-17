package com.example.security.currentuser

import com.example.config.AuthSchemes.APIKEY
import com.example.config.AuthSchemes.OAUTH2
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
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
    fun getCurrentUser(principal: Authentication): ResponseEntity<CurrentUserInformation> =
        ResponseEntity.ok(
            CurrentUserInformation(
                id = principal.name,
                authorities = principal.authorities.map(GrantedAuthority::getAuthority)
            )
        )
}

data class CurrentUserInformation(
    val id: String,
    val authorities: List<String>,
)