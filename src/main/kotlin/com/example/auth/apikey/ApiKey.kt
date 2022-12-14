package com.example.auth.apikey

import org.springframework.security.core.GrantedAuthority
import java.util.*

data class ApiKey(
    val id: UUID,
    val key: String,
    val name: String,
    val authorities: List<GrantedAuthority>,
)

object ApiKeyAuthorities {
    const val READ_AUTHORITY = "READ_AUTHORITY"
    const val WRITE_AUTHORITY = "WRITE_AUTHORITY"
    const val DELETE_AUTHORITY = "DELETE_AUTHORITY"
}