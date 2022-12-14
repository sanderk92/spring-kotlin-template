package com.example.auth

import java.util.*

interface AuthenticatedUserRepository {
    fun create(subject: String): AuthenticatedUser
    fun findByApiKey(apiKey: String): Optional<AuthenticatedUser>
    fun findBySubject(subject: String): Optional<AuthenticatedUser>
}