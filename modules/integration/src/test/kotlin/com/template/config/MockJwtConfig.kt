package com.template.config

import io.mockk.mockk
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.jwt.JwtDecoder
import javax.servlet.*

@Qualifier("IT-MockJwtDecoder")
annotation class MockJwtDecoder

@Configuration
class MockJwtConfig {

    @Bean
    @MockJwtDecoder
    fun mockJwtDecoder() = mockk<JwtDecoder>()
}
