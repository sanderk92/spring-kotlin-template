package com.template.security.apikey

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("spring.security.api-key")
data class ApiKeyProperties(
    val path: String,
    val header: String
)
