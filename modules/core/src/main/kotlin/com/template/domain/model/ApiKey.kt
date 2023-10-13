package com.template.domain.model

import com.template.config.security.apikey.SecureApiKey
import java.util.*

data class ApiKey(
    override val id: UUID,
    override val key: String,
    override val name: String,
    override val read: Boolean,
    override val write: Boolean,
    override val delete: Boolean
) : SecureApiKey
