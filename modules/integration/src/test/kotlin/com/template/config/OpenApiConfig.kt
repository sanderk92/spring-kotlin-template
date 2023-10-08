package com.template.config

import com.google.common.net.HttpHeaders
import com.template.objects.jwtString
import org.openapitools.client.apis.UserApi
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class OpenApiConfig(
    private val context: ServletWebServerApplicationContext
) {
    @Bean
    fun webClient(): WebClient = WebClient.builder()
        .baseUrl("http://localhost:${context.webServer.port}/api/")
        // Any token is required to trigger the BearerTokenJwtDecoder
        .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer $jwtString")
        .build()

    @Bean
    fun userApi(client: WebClient): UserApi = UserApi(client)
}