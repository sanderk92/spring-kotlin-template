package com.template

import io.cucumber.junit.Cucumber
import io.cucumber.junit.CucumberOptions
import io.cucumber.spring.CucumberContextConfiguration
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource

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

        @JvmStatic
        @DynamicPropertySource
        fun setProperties(registry: DynamicPropertyRegistry) {
            registry.add("OIDC_PROVIDER_CONFIG_URL") { "http://localhost:8080" }
            registry.add("OIDC_PROVIDER_JWK_URL") { "http://localhost:8080" }
            registry.add("OIDC_SERVER_CLIENT_ID") { "test" }
            registry.add("OIDC_SERVER_CLIENT_SECRET") { "test" }
            registry.add("OIDC_SWAGGER_CLIENT_ID") { "test" }

            registry.add("DATABASE_URL") { "jdbc:h2:mem:testdb" }
            registry.add("DATABASE_DRIVER") { "org.h2.Driver" }
            registry.add("DATABASE_DIALECT") { "org.hibernate.dialect.H2Dialect" }
            registry.add("DATABASE_USERNAME") { "username" }
            registry.add("DATABASE_PASSWORD") { "username" }
        }
    }
}
