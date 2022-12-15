package com.example.config

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.security.OAuthFlow
import io.swagger.v3.oas.annotations.security.OAuthFlows
import io.swagger.v3.oas.annotations.security.SecurityScheme
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springdoc.core.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

object AuthSchemes {
    const val APIKEY = "apikey"
    const val OAUTH2 = "bearer"
}

@SecurityScheme(
    name = AuthSchemes.APIKEY,
    `in` = SecuritySchemeIn.HEADER,
    type = SecuritySchemeType.APIKEY,
)
@SecurityScheme(
    name = AuthSchemes.OAUTH2,
    scheme = "bearer",
    bearerFormat = "JWT",
    `in` = SecuritySchemeIn.HEADER,
    type = SecuritySchemeType.OAUTH2,
    flows = OAuthFlows(
        authorizationCode = OAuthFlow(
            authorizationUrl = "\${springdoc.oAuthFlow.authUrl}",
            tokenUrl = "\${springdoc.oAuthFlow.tokenUrl}",
        )
    ),
)
@Configuration
class SwaggerConfiguration {

    @Bean
    fun api(): GroupedOpenApi = GroupedOpenApi.builder()
        .group("Test")
        .pathsToMatch("/**")
        .build()

    @Bean
    fun apiInfo(): OpenAPI = OpenAPI()
        .info(
            Info().title("Test")
                .description("Test")
                .version("1.0")
        )
}
