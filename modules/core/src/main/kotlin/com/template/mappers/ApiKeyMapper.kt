package com.template.mappers

import com.template.controller.interfaces.ApiKeyCreatedDto
import com.template.controller.interfaces.ApiKeyDto
import com.template.domain.model.ApiKey
import com.template.domain.model.ApiKeyCreated
import com.template.persistence.entity.ApiKeyEntity
import org.mapstruct.InjectionStrategy.CONSTRUCTOR
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants.ComponentModel.SPRING

@Mapper(componentModel = SPRING, injectionStrategy = CONSTRUCTOR)
internal interface ApiKeyMapper {
    @Mapping(source = "id", target = "id.value")
    fun toApiKey(entity: ApiKeyEntity): ApiKey

    @Mapping(source = "id.value", target = "id")
    fun toApiKeyDto(model: ApiKey): ApiKeyDto

    @Mapping(source = "entity.id", target = "id.value")
    fun toApiKeyCreated(entity: ApiKeyEntity, key: String): ApiKeyCreated

    @Mapping(source = "id.value", target = "id")
    fun toApiKeyCreatedDto(model: ApiKeyCreated): ApiKeyCreatedDto
}
