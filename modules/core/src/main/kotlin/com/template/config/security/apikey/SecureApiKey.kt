package com.template.config.security.apikey

import com.template.config.security.user.Authority
import com.template.config.security.user.SecureUser

internal interface SecureApiKey {
    val user: SecureUser
    val hashedKey: String
    val authorities: List<Authority>
}
