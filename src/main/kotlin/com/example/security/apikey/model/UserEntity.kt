package com.example.security.apikey.model

import java.util.*

interface UserEntity {
    val id: UUID
    val apiKeys: List<ApiKeyEntity>
}
