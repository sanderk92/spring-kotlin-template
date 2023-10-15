package com.template.config.security.apikey

import com.template.config.security.user.Authority

internal interface SecureApiKey {
    val hashedKey: String
    val authorities: List<Authority>
}
