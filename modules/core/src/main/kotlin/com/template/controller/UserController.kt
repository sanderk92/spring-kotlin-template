package com.template.controller

import com.template.controller.interfaces.CurrentUserView
import com.template.controller.interfaces.UserInterface
import com.template.controller.interfaces.UserView
import com.template.security.user.CurrentUser
import com.template.security.user.UserAuthority
import com.template.security.user.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val userService: UserService
) : UserInterface {

    override fun searchUsers(query: String): ResponseEntity<List<UserView>> =
        userService.search(query)
            .map { UserView(it.id, it.email, it.firstName, it.lastName) }
            .let { ResponseEntity.ok(it) }

    override fun getCurrentUser(currentUser: CurrentUser): ResponseEntity<CurrentUserView> =
        userService.findById(currentUser.id)
            ?.let { CurrentUserView(it.id, it.email, it.firstName, it.lastName, currentUser.authorities) }
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()
}
