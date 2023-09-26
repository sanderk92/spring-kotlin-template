package com.example.config

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.security.SecurityScheme
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.servers.Server
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

object SecuritySchemes {
    const val APIKEY = "apikey"
    const val OIDC = "OIDC"
}

@SecurityScheme(
    name = SecuritySchemes.APIKEY,
    type = SecuritySchemeType.APIKEY,
    `in` = SecuritySchemeIn.HEADER,
    paramName = SecuritySchemes.APIKEY,
)
@SecurityScheme(
    name = SecuritySchemes.OIDC,
    type = SecuritySchemeType.OPENIDCONNECT,
    openIdConnectUrl = "\${springdoc.oidc.url}",
)
@Configuration
class SwaggerConfiguration(
    @Value("\${springdoc.openapi.host}") private val server: String,
    @Value("\${springdoc.openapi.version}") private val version: String,
) {

    @Bean
    fun api(): GroupedOpenApi = GroupedOpenApi.builder()
        .group("API")
        .pathsToMatch("/**")
        .build()

    @Bean
    fun apiInfo(): OpenAPI = OpenAPI()
        .addServersItem(Server().also { it.url = server })
        .info(
            Info().title("API")
                .description("API docs")
                .version(version)
        )
}
