package com.template.controller

import com.template.config.security.user.CurrentUser
import com.template.config.security.user.Authority
import com.template.controller.interfaces.CurrentUserView
import com.template.controller.interfaces.UserInterface
import com.template.controller.interfaces.UserView
import com.template.domain.UserService
import com.template.domain.model.User
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(private val userService: UserService) : UserInterface {

    override fun searchUsers(query: String): ResponseEntity<List<UserView>> =
        userService.search(query)
            .map(::toView)
            .let { ResponseEntity.ok(it) }

    override fun getCurrentUser(currentUser: CurrentUser): ResponseEntity<CurrentUserView> =
        userService.findById(currentUser.id)
            ?.let { toView(it, currentUser) }
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()
}

private fun toView(user: User) = UserView(
    id = user.id,
    email = user.email,
    username = user.username,
    firstName = user.firstName,
    lastName = user.lastName,
)

private fun toView(user: User, currentUser: CurrentUser) = CurrentUserView(
    id = user.id,
    email = user.email,
    username = user.username,
    firstName = user.firstName,
    lastName = user.lastName,
    authorities = currentUser.authorities.map(Authority::toString)
)
