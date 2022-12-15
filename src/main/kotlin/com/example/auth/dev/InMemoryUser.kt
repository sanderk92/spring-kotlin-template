package com.example.auth.dev

import com.example.auth.apikey.model.ApiKey
import com.example.auth.apikey.model.ApiKeyUser

data class InMemoryUser(
    override val id: String,
    override val apiKeys: List<ApiKey>,
) : ApiKeyUser