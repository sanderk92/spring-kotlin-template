package com.template.config

import org.openapitools.client.apis.ApiKeysApi
import org.openapitools.client.apis.UsersApi
import org.openapitools.client.invokers.ApiClient
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.HttpHeaders

@Configuration
internal class OpenApiConfig(
    private val context: ServletWebServerApplicationContext
) {
    @Bean
    @Primary
    fun apiClient(): ApiClient = ApiClient().apply {
        isDebugging = true
        basePath = "http://localhost:${context.webServer.port}"
        addDefaultHeader(HttpHeaders.AUTHORIZATION, "Bearer token")
    }

    @Bean
    fun usersApi(client: ApiClient): UsersApi = UsersApi(client)

    @Bean
    fun apiKeysApi(client: ApiClient): ApiKeysApi = ApiKeysApi(client)
}
