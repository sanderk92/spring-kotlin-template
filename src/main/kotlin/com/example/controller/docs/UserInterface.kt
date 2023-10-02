package com.example.controller.docs

import com.example.config.SecuritySchemes
import com.example.controller.CurrentUserView
import com.example.controller.UserView
import com.example.security.user.CurrentUser
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity

@Tag(name = "User", description = "Retrieve information about the current user")
interface UserInterface {

    @Operation(
        summary = "Search for users with the specified parameters",
        description = "Accessible with role READ",
        security = [SecurityRequirement(name = SecuritySchemes.OIDC), SecurityRequirement(name = SecuritySchemes.APIKEY)],
    )
    fun searchUsers(
        @Parameter(description = "Query user by first or last name and email") query: String
    ): ResponseEntity<List<UserView>>

    @Operation(
        summary = "Get the details of the currently active authentication",
        description = "Accessible with role READ",
        security = [SecurityRequirement(name = SecuritySchemes.OIDC), SecurityRequirement(name = SecuritySchemes.APIKEY)]
    )
    fun getCurrentUser(
        @Parameter(hidden = true) currentUser: CurrentUser
    ): ResponseEntity<CurrentUserView>
}