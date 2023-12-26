package com.template.mappers

import com.template.config.security.user.Authority
import com.template.controller.interfaces.CurrentUserDto
import com.template.controller.interfaces.UserDto
import com.template.domain.model.User
import com.template.persistence.entity.UserEntity
import org.mapstruct.InjectionStrategy.CONSTRUCTOR
import org.mapstruct.Mapper

@Mapper(componentModel = "spring", injectionStrategy = CONSTRUCTOR, uses = [ApiKeyMapper::class])
internal interface UserMapper {
    fun toUser(entity: UserEntity): User

    fun toUserDto(user: User): UserDto

    fun toCurrentUserDto(
        user: User,
        authorities: List<Authority>,
    ): CurrentUserDto
}
