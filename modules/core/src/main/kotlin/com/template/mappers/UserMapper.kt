package com.template.mappers

import com.template.config.security.user.Authority
import com.template.controller.interfaces.CurrentUserDto
import com.template.controller.interfaces.UserDto
import com.template.domain.model.User
import com.template.persistence.entity.UserEntity
import org.mapstruct.InjectionStrategy.CONSTRUCTOR
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants.ComponentModel.SPRING

@Mapper(componentModel = SPRING, injectionStrategy = CONSTRUCTOR, uses = [ApiKeyMapper::class])
internal interface UserMapper {
    @Mapping(source = "id", target = "id.value")
    fun toUser(entity: UserEntity): User

    @Mapping(source = "id.value", target = "id")
    fun toUserDto(user: User): UserDto

    @Mapping(source = "user.id.value", target = "id")
    fun toCurrentUserDto(user: User, authorities: List<Authority>): CurrentUserDto
}
