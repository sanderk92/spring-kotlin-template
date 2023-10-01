package com.example.security.dev

import com.example.security.user.User
import com.example.security.user.UserAuthority
import java.util.*

data class InMemoryUser(
    override val id: UUID,
    override val email: String,
    override val firstName: String,
    override val lastName: String,
    override val apiKeys: List<InMemoryApiKey>,
    override val authorities: List<UserAuthority>,
) : User