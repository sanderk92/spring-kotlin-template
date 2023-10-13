package com.template.mappers

import com.template.controller.interfaces.ApiKeyCreatedDto
import com.template.controller.interfaces.ApiKeyDto
import com.template.domain.model.ApiKey
import com.template.domain.model.ApiKeyCreated
import com.template.persistence.entity.ApiKeyEntity
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring")
interface ApiKeyMapper {

    fun toApiKey(entity: ApiKeyEntity): ApiKey

    fun toApiKeyCreated(entity: ApiKeyEntity, unHashedKey: String): ApiKeyCreated

    fun toApiKeyDto(model: ApiKey): ApiKeyDto

    @Mapping(source = "unHashedKey", target = "key")
    fun toApiKeyCreatedDto(model: ApiKeyCreated): ApiKeyCreatedDto
}
