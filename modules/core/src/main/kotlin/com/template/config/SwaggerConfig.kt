package com.template.config

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.security.SecurityScheme
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.servers.Server
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.info.BuildProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

internal object SecuritySchemes {
    const val APIKEY = "API-Key"
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
internal class SwaggerConfiguration(
    private val buildProperties: BuildProperties,
) {
    @Bean
    fun groupedOpenApi(): GroupedOpenApi =
        GroupedOpenApi.builder()
            .group("API")
            .pathsToMatch("/**")
            .build()

    @Bean
    fun openApi(
        @Value("\${server.host}") host: String,
    ): OpenAPI =
        OpenAPI()
            .addServersItem(Server().also { it.url = host }).info(
                Info()
                    .title("API")
                    .description("API docs")
                    .version(buildProperties.version),
            )
}
