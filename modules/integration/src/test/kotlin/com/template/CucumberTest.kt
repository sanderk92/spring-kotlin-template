package com.template

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.template.CucumberTest.Companion.mongoContainer
import com.template.CucumberTest.Companion.wiremock
import io.cucumber.junit.Cucumber
import io.cucumber.junit.CucumberOptions
import io.cucumber.spring.CucumberContextConfiguration
import io.github.oshai.kotlinlogging.KotlinLogging
import org.junit.jupiter.api.AfterAll
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.utility.DockerImageName

private val log = KotlinLogging.logger {}

@RunWith(Cucumber::class)
@CucumberOptions(
    plugin = ["pretty"],
    features = ["src/test/resources/features"],
    monochrome = true,
    tags = "@Integration"
)
@CucumberContextConfiguration
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [Application::class]
)
class CucumberTest {

    companion object {

        val wiremock = WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort())

        @Container
        val mongoContainer = MongoDBContainer(DockerImageName.parse("mongo:6.0.3"))

        init {
            wiremock.start()
            mongoContainer.start()

            log.info { "Wiremock accessible on ${wiremock.baseUrl()}" }
            log.info { "MongoDB accessible on ${mongoContainer.connectionString}" }
        }

        @JvmStatic
        @DynamicPropertySource
        fun setProperties(registry: DynamicPropertyRegistry) {
            registry.add("OIDC_PROVIDER_CONFIG_URL") { wiremock.baseUrl() + "/config" }
            registry.add("OIDC_PROVIDER_JWK_URL") { wiremock.baseUrl() + "/jwk" }
            registry.add("OIDC_SERVER_CLIENT_ID") { "test" }
            registry.add("OIDC_SERVER_CLIENT_SECRET") { "test" }
            registry.add("OIDC_SWAGGER_CLIENT_ID") { "test" }
            registry.add("feature.users.generate") { true }
            registry.add("feature.users.in-memory") { true }
        }
    }
}

@AfterAll
fun clean() {
    wiremock.stop()
    mongoContainer.stop()
}
