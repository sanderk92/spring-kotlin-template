package com.template.domain.model

import com.template.config.security.user.SecureUser
import java.util.UUID

internal data class User(
    override val id: Id,
    override val email: String,
    override val username: String,
    override val firstName: String,
    override val lastName: String,
    override val apiKeys: List<ApiKey>,
) : SecureUser {
    data class Id(
        override val value: UUID,
    ) : SecureUser.Id
}
