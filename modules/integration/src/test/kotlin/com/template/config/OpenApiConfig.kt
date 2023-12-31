package com.template.config

import org.openapitools.client.apis.ApiKeysApi
import org.openapitools.client.apis.UsersApi
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.web.reactive.function.client.WebClient

@Configuration
internal class OpenApiConfig(
    private val context: ServletWebServerApplicationContext
) {
    @Bean
    fun webClient(): WebClient = WebClient.builder()
        .baseUrl("http://localhost:${context.webServer.port}/")
        .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer token")
        .build()

    @Bean
    fun usersApi(client: WebClient): UsersApi = UsersApi(client)

    @Bean
    fun apiKeysApi(client: WebClient): ApiKeysApi = ApiKeysApi(client)
}
