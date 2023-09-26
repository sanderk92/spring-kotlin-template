package com.example.security.apikey

import java.util.*

interface ApiKey {
    val id: UUID
    val key: String
    val name: String
    val authorities: List<String>
}

