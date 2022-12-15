package com.example.auth.apikey.model

interface ApiKeyUser {
    val id: String
    val apiKeys: List<ApiKey>
}
