package com.template.config.security.apikey

import com.template.config.security.apikey.SecureApiKey

internal interface SecureApiKeyService {
    fun findByApiKey(hashedApiKey: String): SecureApiKey?
}
