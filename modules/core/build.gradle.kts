plugins {
    kotlin("kapt")
    id("org.springframework.boot") version "3.2.1"
    id("org.springdoc.openapi-gradle-plugin") version "1.7.0"
    id("org.jlleitschuh.gradle.ktlint") version "12.0.3"
    id("com.google.cloud.tools.jib") version "3.4.0"
    id("org.flywaydb.flyway") version "10.4.1"
}

dependencies {
    // Spring
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework:spring-aspects")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")

    // Database
    implementation("org.postgresql:postgresql:42.7.1")
    implementation("com.h2database:h2:2.2.224")
    implementation("org.flywaydb:flyway-core:10.4.1")

    // Mapping
    implementation("org.mapstruct:mapstruct:1.5.5.Final")
    kapt("org.mapstruct:mapstruct-processor:1.5.5.Final")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("io.mockk:mockk:1.13.8")
}

openApi {
    apiDocsUrl.set("http://localhost:8080/v3/api-docs")
    customBootRun {
        args.set(listOf("--spring.profiles.active=openapi"))
    }
}

springBoot {
    buildInfo()
}

ktlint {
    enableExperimentalRules.set(true)
    filter {
        setExcludes(setOf("./build"))
    }
}

jib {
}

/*
 * All task dependencies must be defined explicitly. If a tool does not; we must define it ourselves.
 */
tasks.named("forkedSpringBootRun") {
    dependsOn(":modules:core:build")
}

tasks.named("runKtlintCheckOverMainSourceSet") {
    dependsOn(":modules:core:kaptKotlin")
}
