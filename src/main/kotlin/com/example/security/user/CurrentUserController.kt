package com.example.security.user

import com.example.config.SecuritySchemes.APIKEY
import com.example.config.SecuritySchemes.OIDC
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping
@Tag(name = "User", description = "Retrieve information about the current user")
class CurrentUserController {

    @Operation(
        description = "Get the currently authorized user",
        security = [SecurityRequirement(name = OIDC), SecurityRequirement(name = APIKEY)]
    )
    @CurrentUserExtractor
    @GetMapping("/me")
    fun getCurrentUser(@Parameter(hidden = true) currentUser: CurrentUser): ResponseEntity<CurrentUser> =
        ResponseEntity.ok(currentUser)
}
