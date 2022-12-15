package com.example.auth.apikey.model

import java.util.*

interface ApiKeyUserService {
    fun findById(userId: String): Optional<ApiKeyUser>
    fun findByApiKey(apiKeyId: String): Optional<ApiKeyUser>
    fun addApiKey(userId: String, apiKey: ApiKey): Optional<ApiKeyUser>
    fun deleteApiKey(userId: String, apiKeyId: String)
}