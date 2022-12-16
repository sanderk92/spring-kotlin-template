package com.example.security.dev

import com.example.security.apikey.model.ApiKey
import com.example.security.apikey.model.ApiKeyUser

data class InMemoryUser(
    override val id: String,
    override val apiKeys: List<ApiKey>,
) : ApiKeyUser