package com.example.auth

import com.example.auth.apikey.ApiKey
import java.util.*

interface AuthenticatedUser {
    val id: UUID
    val subject: String
    val apiKey: List<ApiKey>
}

object UserRoles {
    const val USER = "USER"
}