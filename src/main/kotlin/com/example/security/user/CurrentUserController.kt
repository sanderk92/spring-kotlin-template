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
import java.util.*

@RestController
@RequestMapping("/user")
@Tag(name = "User", description = "Retrieve information about the current user")
class CurrentUserController(private val userService: UserService) {

    @GetMapping
    @ExtractCurrentUser
    @Operation(
        summary = "Get the currently authorized user",
        security = [SecurityRequirement(name = OIDC), SecurityRequirement(name = APIKEY)]
    )
    fun getCurrentUser(@Parameter(hidden = true) currentUser: CurrentUser): ResponseEntity<UserView> =
        userService.findById(currentUser.id)
            ?.let { UserView(it.id, it.email, it.firstName, it.lastName, currentUser.authorities) }
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()
}

data class UserView(
    val id: UUID,
    val email: String,
    val firstName: String,
    val lastName: String,
    val authorities: List<String>,
)