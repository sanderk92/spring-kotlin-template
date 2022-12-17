package com.example.security.dev

import com.example.security.apikey.model.User
import java.util.*

data class InMemoryUser(
    override val id: UUID,
    override val apiKeys: List<InMemoryApiKey>,
) : User