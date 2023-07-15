package com.example.config

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.security.SecurityScheme
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springdoc.core.models.GroupedOpenApi
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
