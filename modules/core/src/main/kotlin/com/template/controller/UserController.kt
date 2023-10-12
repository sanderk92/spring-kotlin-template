package com.template.controller

import com.template.config.security.user.CurrentUser
import com.template.config.security.user.UserAuthority
import com.template.config.security.user.SecureUserService
import com.template.controller.interfaces.CurrentUserView
import com.template.controller.interfaces.UserInterface
import com.template.controller.interfaces.UserView
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val secureUserService: SecureUserService
) : UserInterface {

    override fun searchUsers(query: String): ResponseEntity<List<UserView>> =
        secureUserService.search(query)
            .map { UserView(it.id, it.email, it.username, it.firstName, it.lastName) }
            .let { ResponseEntity.ok(it) }

    override fun getCurrentUser(currentUser: CurrentUser): ResponseEntity<CurrentUserView> =
        secureUserService.findById(currentUser.id)
            ?.let { CurrentUserView(it.id, it.email, it.username, it.firstName, it.lastName, currentUser.stringAuthorities()) }
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()
}

private fun CurrentUser.stringAuthorities() = this.authorities.map(UserAuthority::toString)
