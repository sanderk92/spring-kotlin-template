package com.template

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.template.CucumberTest.Companion.wiremock
import io.cucumber.junit.Cucumber
import io.cucumber.junit.CucumberOptions
import io.cucumber.spring.CucumberContextConfiguration
import org.junit.jupiter.api.AfterAll
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.annotation.DirtiesContext.MethodMode.AFTER_METHOD
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource

@RunWith(Cucumber::class)
@CucumberContextConfiguration
@DirtiesContext(
    methodMode = AFTER_METHOD
)
@SpringBootTest(
    webEnvironment = RANDOM_PORT,
    classes = [Application::class]
)
@CucumberOptions(
    plugin = ["pretty"],
    features = ["src/test/resources/features"],
    monochrome = true,
    tags = "@Integration"
)
internal class CucumberTest {

    companion object {

        val wiremock: WireMockServer = WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort())

        init {
            wiremock.start()
        }

        @JvmStatic
        @DynamicPropertySource
        fun setProperties(registry: DynamicPropertyRegistry) {
            registry.add("SERVER_HOST") { "" } // unused
            registry.add("DATABASE_URL") { "jdbc:h2:mem:testdb" }
            registry.add("DATABASE_DIALECT") { "org.hibernate.dialect.H2Dialect" }
            registry.add("DATABASE_USERNAME") { "username" }
            registry.add("DATABASE_PASSWORD") { "username" }
            registry.add("OIDC_PROVIDER_CONFIG_URL") { "${wiremock.baseUrl()}/auth/config" } // unused
            registry.add("OIDC_PROVIDER_JWK_URL") { "${wiremock.baseUrl()}/auth/jwk" } // unused
            registry.add("OIDC_USER_INFO_ENDPOINT") { "${wiremock.baseUrl()}/auth/userinfo" }
            registry.add("OIDC_SERVER_CLIENT_ID") { "" } // unused
            registry.add("OIDC_SERVER_CLIENT_SECRET") { "" } // unused
            registry.add("OIDC_SWAGGER_CLIENT_ID") { "" } // unused
        }
    }
}

@AfterAll
fun tearDown() {
    wiremock.stop()
}