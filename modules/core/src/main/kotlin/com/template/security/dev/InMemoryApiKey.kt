package com.template.security.dev

import com.template.security.apikey.ApiKey
import java.time.Instant
import java.util.*

data class InMemoryApiKey(
    override val id: UUID,
    override val key: String,
    override val name: String,
    override val read: Boolean,
    override val write: Boolean,
    override val delete: Boolean,
    val created: Instant = Instant.now(),
) : ApiKey
