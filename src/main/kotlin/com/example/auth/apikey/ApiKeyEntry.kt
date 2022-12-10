package com.example.auth.apikey

data class ApiKeyEntry(
    val name: String,
    val read: Boolean,
    val write: Boolean,
    val delete: Boolean,
)