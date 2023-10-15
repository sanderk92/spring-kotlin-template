package com.template.domain.model

import com.template.config.security.user.Authority
import com.template.config.security.user.SecureUser
import java.util.*

internal data class User(
    override val id: UUID,
    override val email: String,
    override val username: String,
    override val firstName: String,
    override val lastName: String,
    override val apiKeys: List<ApiKey>,
    override val authorities: List<Authority>
) : SecureUser
