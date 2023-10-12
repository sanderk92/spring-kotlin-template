package com.template.domain.model

import com.template.config.security.apikey.ApiKey
import com.template.config.security.user.SecureUser
import com.template.config.security.user.UserAuthority
import java.util.*

data class User(
    override val id: UUID,
    override val email: String,
    override val username: String,
    override val firstName: String,
    override val lastName: String,
    override val apiKeys: List<ApiKey>,
    override val authorities: List<UserAuthority>
) : SecureUser

data class ApiKey(
    override val id: UUID,
    override val key: String,
    override val name: String,
    override val read: Boolean,
    override val write: Boolean,
    override val delete: Boolean
) : ApiKey
