package com.template.config.security.apikey

internal interface SecureApiKeyService {
    fun findByApiKey(hashedApiKey: String): SecureApiKey?
}
