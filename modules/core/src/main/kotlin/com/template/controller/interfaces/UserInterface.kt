package com.template.controller.interfaces

import com.template.config.SecuritySchemes
import com.template.config.security.user.CurrentUser
import com.template.config.security.user.READ_ROLE
import com.template.controller.interfaces.UserInterface.Companion.ENDPOINT
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import java.util.*
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@RequestMapping(ENDPOINT)
@Secured(READ_ROLE)
@Tag(name = "User", description = "Retrieve information about the current user")
interface UserInterface {

    companion object {
        const val ENDPOINT = "/users"
    }

    @GetMapping
    @Operation(
        summary = "Search for users with the specified parameters",
        description = "Accessible with role READ",
        security = [SecurityRequirement(name = SecuritySchemes.OIDC), SecurityRequirement(name = SecuritySchemes.APIKEY)],
    )
    fun searchUsers(
        @Parameter(description = "Query user by first or last name and email") @RequestParam
        query: String
    ): ResponseEntity<List<UserDto>>

    @GetMapping("/me")
    @Operation(
        summary = "Get the details of the currently active authentication",
        description = "Accessible with role READ",
        security = [SecurityRequirement(name = SecuritySchemes.OIDC), SecurityRequirement(name = SecuritySchemes.APIKEY)]
    )
    fun getCurrentUser(
        @Parameter(hidden = true) currentUser: CurrentUser
    ): ResponseEntity<CurrentUserDto>
}

data class UserDto(
    val id: UUID,
    val email: String,
    val username: String,
    val firstName: String,
    val lastName: String,
)

data class CurrentUserDto(
    val id: UUID,
    val email: String,
    val username: String,
    val firstName: String,
    val lastName: String,
    val authorities: List<String>,
)
