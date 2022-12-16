package com.example.security.apikey.model

interface ApiKeyUser {
    val id: String
    val apiKeys: List<ApiKey>
}

object ApiKeyUserRoles {
    const val USER_ROLE = "USER"
}