package com.example.security.apikey.model

import java.util.*

interface User {
    val id: UUID
    val apiKeys: List<ApiKey>
}
