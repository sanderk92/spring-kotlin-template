package com.template.mappers

import com.template.controller.interfaces.ApiKeyCreatedDto
import com.template.controller.interfaces.ApiKeyDto
import com.template.domain.model.ApiKey
import com.template.domain.model.ApiKeyCreated
import com.template.persistence.entity.ApiKeyEntity
import org.mapstruct.InjectionStrategy.CONSTRUCTOR
import org.mapstruct.Mapper

@Mapper(componentModel = "spring", injectionStrategy = CONSTRUCTOR)
internal interface ApiKeyMapper {

    fun toApiKey(entity: ApiKeyEntity): ApiKey

    fun toApiKeyDto(model: ApiKey): ApiKeyDto

    fun toApiKeyCreated(entity: ApiKeyEntity, key: String): ApiKeyCreated

    fun toApiKeyCreatedDto(model: ApiKeyCreated): ApiKeyCreatedDto
}
