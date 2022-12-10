package com.example.auth.dev

import com.example.auth.AuthenticatedUser
import com.example.auth.apikey.ApiKey
import java.util.*

data class InMemoryUser(
    override val id: UUID,
    override val subject: String,
    override val apiKey: List<ApiKey>,
) : AuthenticatedUser