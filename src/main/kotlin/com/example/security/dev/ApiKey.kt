package com.example.security.dev

import com.example.security.apikey.model.ApiKeyEntity
import java.time.Instant
import java.util.UUID

data class ApiKey(
    override val id: UUID,
    override val key: String,
    override val name: String,
    override val authorities: List<String>,
    val created: Instant = Instant.now(),
) : ApiKeyEntity