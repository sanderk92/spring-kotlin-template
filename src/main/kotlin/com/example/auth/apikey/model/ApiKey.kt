package com.example.auth.apikey.model

data class ApiKey(
    val id: String,
    val key: String,
    val name: String,
    val authorities: List<String>,
)

data class ApiKeyRequest(
    val name: String,
    val read: Boolean,
    val write: Boolean,
    val delete: Boolean,
)

data class ApiKeyView(
    val id: String,
    val name: String,
    val authorities: List<String>,
)

object ApiKeyAuthorities {
    const val READ_AUTHORITY = "READ"
    const val WRITE_AUTHORITY = "WRITE"
    const val DELETE_AUTHORITY = "DELETE"
}