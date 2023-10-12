package com.template.persistence.model

import com.template.config.security.apikey.ApiKey
import com.template.config.security.user.UserAuthority
import java.util.*

data class UserEntity(
    val id: UUID,
    val email: String,
    val username: String,
    val firstName: String,
    val lastName: String,
    val apiKeys: List<ApiKey>,
    val authorities: List<UserAuthority>
)

data class ApiKeyEntity(
    val id: UUID,
    val key: String,
    val name: String,
    val read: Boolean,
    val write: Boolean,
    val delete: Boolean
)
