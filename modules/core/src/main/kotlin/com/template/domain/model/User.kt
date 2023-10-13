package com.template.domain.model

import com.template.config.security.apikey.SecureApiKey
import com.template.config.security.user.SecureUser
import com.template.config.security.user.UserAuthority
import java.util.*

data class User(
    override val id: UUID,
    override val email: String,
    override val username: String,
    override val firstName: String,
    override val lastName: String,
    override val apiKeys: List<SecureApiKey>,
    override val authorities: List<UserAuthority>
) : SecureUser
