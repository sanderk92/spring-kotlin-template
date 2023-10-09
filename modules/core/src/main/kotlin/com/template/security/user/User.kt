package com.template.security.user

import com.template.security.apikey.ApiKey
import java.util.*

interface User {
    val id: UUID
    val email: String
    val username: String
    val firstName: String
    val lastName: String
    val apiKeys: List<ApiKey>
    val authorities: List<UserAuthority>
}

data class UserEntry(
    val email: String,
    val username: String,
    val firstName: String,
    val lastName: String,
    val authorities: List<UserAuthority>,
)
