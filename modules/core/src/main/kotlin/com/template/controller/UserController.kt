package com.template.controller

import com.template.controller.UserController.Companion.USER_CONTROLLER_ENDPOINTS
import com.template.controller.docs.UserInterface
import com.template.security.user.CurrentUser
import com.template.security.user.ExtractCurrentUser
import com.template.security.user.UserAuthority
import com.template.security.user.UserService
import java.util.*
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Validated
@RestController
@RequestMapping(USER_CONTROLLER_ENDPOINTS)
class UserController(private val userService: UserService) : UserInterface {

    companion object {
        const val USER_CONTROLLER_ENDPOINTS = "/users"
    }

    @GetMapping
    @ExtractCurrentUser
    @PreAuthorize("hasRole(T(com.template.security.user.UserAuthority).READ)")
    override fun searchUsers(@RequestParam(required = true) query: String): ResponseEntity<List<UserView>> =
        userService.search(query)
            .map { UserView(it.id, it.email, it.firstName, it.lastName) }
            .let { ResponseEntity.ok(it) }

    @GetMapping("/me")
    @ExtractCurrentUser
    @PreAuthorize("hasRole(T(com.template.security.user.UserAuthority).READ)")
    override fun getCurrentUser(currentUser: CurrentUser): ResponseEntity<CurrentUserView> =
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
    val authorities: List<UserAuthority>,
)
