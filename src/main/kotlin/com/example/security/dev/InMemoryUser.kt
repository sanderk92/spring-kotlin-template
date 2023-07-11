package com.example.security.dev

import com.example.security.user.User
import java.util.*

data class InMemoryUser(
    override val id: UUID,
    override val apiKeys: List<InMemoryApiKey>,
) : User