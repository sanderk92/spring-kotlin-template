package com.template.config.security.apikey

import java.util.*

interface ApiKey {
    val id: UUID
    val key: String
    val name: String
    val read: Boolean
    val write: Boolean
    val delete: Boolean
}

sealed interface ApiKeyEntry {
    val key: String
    val name: String
    val read: Boolean
    val write: Boolean
    val delete: Boolean
}

data class HashedApiKeyEntry(
    override val key: String,
    override val name: String,
    override val read: Boolean,
    override val write: Boolean,
    override val delete: Boolean,
) : ApiKeyEntry

data class UnHashedApiKeyEntry(
    override val key: String,
    override val name: String,
    override val read: Boolean,
    override val write: Boolean,
    override val delete: Boolean,
) : ApiKeyEntry
