package com.template.config.security.apikey

import java.util.*

interface SecureApiKey {
    val id: UUID
    val key: String
    val name: String
    val read: Boolean
    val write: Boolean
    val delete: Boolean
}

sealed interface SecureApiKeyEntry {
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
) : SecureApiKeyEntry

data class UnHashedApiKeyEntry(
    override val key: String,
    override val name: String,
    override val read: Boolean,
    override val write: Boolean,
    override val delete: Boolean,
) : SecureApiKeyEntry
