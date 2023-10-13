package com.template.controller

import com.template.config.security.user.CurrentUser
import com.template.controller.interfaces.CurrentUserDto
import com.template.controller.interfaces.UserDto
import com.template.controller.interfaces.UserInterface
import com.template.domain.UserService
import com.template.mappers.UserMapper
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val userService: UserService,
    private val userMapper: UserMapper,
) : UserInterface {

    override fun searchUsers(query: String): ResponseEntity<List<UserDto>> =
        userService.search(query)
            .map(userMapper::toUserDto)
            .let { ResponseEntity.ok(it) }

    override fun getCurrentUser(currentUser: CurrentUser): ResponseEntity<CurrentUserDto> =
        userService.findById(currentUser.id)
            ?.let(userMapper::toCurrentUserDto)
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()
}
