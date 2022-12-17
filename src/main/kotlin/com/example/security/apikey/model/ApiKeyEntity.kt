package com.example.security.apikey.model

import java.util.UUID

interface ApiKeyEntity {
    val id: UUID
    val key: String
    val name: String
    val authorities: List<String>
}

