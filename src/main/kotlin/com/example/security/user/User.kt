package com.example.security.user

import com.example.security.apikey.ApiKey
import java.util.*

interface User {
    val id: UUID
    val email: String
    val firstName: String
    val lastName: String
    val apiKeys: List<ApiKey>
    val authorities: List<UserAuthority>
}
