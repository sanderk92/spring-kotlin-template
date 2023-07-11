package com.example.security.apikey.interfaces

import java.util.UUID

interface ApiKey {
    val id: UUID
    val key: String
    val name: String
    val authorities: List<String>
}

