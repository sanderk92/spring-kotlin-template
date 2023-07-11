package com.example.security.dev

import com.example.security.apikey.ApiKey
import java.time.Instant
import java.util.UUID

data class InMemoryApiKey(
    override val id: UUID,
    override val key: String,
    override val name: String,
    override val authorities: List<String>,
    val created: Instant = Instant.now(),
) : ApiKey