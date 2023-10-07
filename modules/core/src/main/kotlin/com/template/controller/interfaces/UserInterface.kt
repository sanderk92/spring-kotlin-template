package com.template.controller.interfaces

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.template.config.SecuritySchemes
import com.template.controller.interfaces.UserInterface.Companion.ENDPOINT
import com.template.security.user.CurrentUser
import com.template.security.user.UserAuthority
import com.template.security.user.UserAuthorityJsonSerializer
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
@Secured(UserAuthority.READ.role)
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
        @Parameter(description = "Query user by first or last name and email") @RequestParam query: String
    ): ResponseEntity<List<UserView>>

    @GetMapping("/me")
    @Operation(
        summary = "Get the details of the currently active authentication",
        description = "Accessible with role READ",
        security = [SecurityRequirement(name = SecuritySchemes.OIDC), SecurityRequirement(name = SecuritySchemes.APIKEY)]
    )
    fun getCurrentUser(
        @Parameter(hidden = true) currentUser: CurrentUser
    ): ResponseEntity<CurrentUserView>
}

data class UserView(
    val id: UUID,
    val email: String,
    val firstName: String,
    val lastName: String,
)

data class CurrentUserView(
    val id: UUID,
    val email: String,
    val firstName: String,
    val lastName: String,
    @JsonSerialize(contentUsing = UserAuthorityJsonSerializer::class)
    val authorities: List<UserAuthority>,
)
