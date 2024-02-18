package com.template.config

import org.openapitools.client.apis.ApiKeysApi
import org.openapitools.client.apis.UsersApi
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.web.client.RestClient

@Configuration
internal class OpenApiConfig(
    private val context: ServletWebServerApplicationContext
) {
    @Bean
    fun restClient(): RestClient = RestClient.builder()
        .baseUrl("http://localhost:${context.webServer.port}/")
        .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer token")
        .build()

    @Bean
    fun usersApi(client: RestClient): UsersApi = UsersApi(client)

    @Bean
    fun apiKeysApi(client: RestClient): ApiKeysApi = ApiKeysApi(client)
}
