package com.example.security.user

import com.example.config.SecuritySchemes.APIKEY
import com.example.config.SecuritySchemes.OIDC
import com.example.security.user.CurrentUserController.Companion.USER_CONTROLLER_ENDPOINTS
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.constraints.NotBlank
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@Validated
@RestController
@RequestMapping(USER_CONTROLLER_ENDPOINTS)
@Tag(name = "User", description = "Retrieve information about the current user")
class CurrentUserController(private val userService: UserService) {

    companion object {
        const val USER_CONTROLLER_ENDPOINTS = "/users"
    }

    @GetMapping
    @ExtractCurrentUser
    @Operation(
        summary = "Search for users with the specified parameters",
        security = [SecurityRequirement(name = OIDC), SecurityRequirement(name = APIKEY)],
    )
    fun searchUsers(
        @Parameter(description = "Query user by first or last name and email") @RequestParam @NotBlank query: String
    ): ResponseEntity<List<UserView>> =
        userService.search(query)
            .map { UserView(it.id, it.email, it.firstName, it.lastName) }
            .let { ResponseEntity.ok(it) }

    @GetMapping("/me")
    @ExtractCurrentUser
    @Operation(
        summary = "Get the currently authorized user",
        security = [SecurityRequirement(name = OIDC), SecurityRequirement(name = APIKEY)]
    )
    fun getCurrentUser(
        @Parameter(hidden = true) currentUser: CurrentUser
    ): ResponseEntity<CurrentUserView> =
        userService.findById(currentUser.id)
            ?.let { CurrentUserView(it.id, it.email, it.firstName, it.lastName, currentUser.authorities) }
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()
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
    val authorities: List<String>,
)