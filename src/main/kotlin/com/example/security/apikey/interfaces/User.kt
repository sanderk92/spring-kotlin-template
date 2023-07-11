package com.example.security.apikey.interfaces

import java.util.*

interface User {
    val id: UUID
    val apiKeys: List<ApiKey>
}
