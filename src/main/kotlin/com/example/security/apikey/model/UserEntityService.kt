package com.example.security.apikey.model

import com.example.security.apikey.ApiKeyEntry
import java.util.*

interface UserEntityService {
    fun findById(userId: UUID): UserEntity?
    fun findByApiKey(apiKey: String): UserEntity?
    fun addApiKey(userId: UUID, entry: ApiKeyEntry): UserEntity?
    fun deleteApiKey(userId: UUID, apiKeyId: UUID)
}