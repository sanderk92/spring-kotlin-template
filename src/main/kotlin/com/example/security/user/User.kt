package com.example.security.user

import com.example.security.apikey.ApiKey
import java.util.*

interface User {
    val id: UUID
    val apiKeys: List<ApiKey>
}
