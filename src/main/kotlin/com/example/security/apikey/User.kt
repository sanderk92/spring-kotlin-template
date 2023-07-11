package com.example.security.apikey

import java.util.*

interface User {
    val id: UUID
    val apiKeys: List<ApiKey>
}
