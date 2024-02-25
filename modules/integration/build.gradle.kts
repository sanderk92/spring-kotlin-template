import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("org.springframework.boot") version "3.1.7" // 3.2.3 causes lifecycle issues
    id("org.openapi.generator") version "7.3.0"
}

sourceSets {
    main {
        java {
            // OpenApi generated sources
            srcDir("$projectDir/build/generated/src/main/java")
        }
    }
}

val cucumberVersion = "7.15.0"

dependencies {
    implementation(project(mapOf("path" to ":modules:core")))

    // Http client
    implementation("jakarta.annotation:jakarta.annotation-api:3.0.0-M1")
    implementation("jakarta.ws.rs:jakarta.ws.rs-api:3.1.0")
    implementation("org.glassfish.jersey.core:jersey-client:3.1.5")
    implementation("org.glassfish.jersey.media:jersey-media-json-jackson:3.1.5")
    implementation("org.glassfish.jersey.media:jersey-media-multipart:3.1.5")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.16.1")

    // Spring
    testImplementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-test")

    // Test
    testImplementation("com.h2database:h2:2.2.224")
    testImplementation("org.junit.platform:junit-platform-launcher")
    testImplementation("org.junit.vintage:junit-vintage-engine")
    testImplementation("org.wiremock:wiremock-standalone:3.3.1")
    testImplementation("org.assertj:assertj-core:3.24.2")
    testImplementation("io.mockk:mockk:1.13.8")

    // Cucumber
    testImplementation("io.cucumber:cucumber-spring:$cucumberVersion")
    testImplementation("io.cucumber:cucumber-java:$cucumberVersion")
    testImplementation("io.cucumber:cucumber-junit:$cucumberVersion")
}

/**
 * The choice has been made to use the Java/Jersey3 client, as many other clients seem to not fit our use-case:
 * - OpenIdConnect support is not only missing, but blocks requests in many Java/Kotlin clients.
 * - Polymorphism support is not only missing, but blocks deserialization in many Java/Kotlin clients.
 * - File upload is broken in some Kotlin clients
 */
val openApiSpec = "${project(":modules:core").projectDir}/build/openapi.json"
openApiGenerate {
    generatorName.set("java")
    library.set("jersey3")
    inputSpec.set(openApiSpec)
    outputDir.set("$projectDir/build/generated")
    modelPackage.set("org.openapitools.client.models")
    apiPackage.set("org.openapitools.client.apis")
    invokerPackage.set("org.openapitools.client.invokers")
    configOptions.set(mapOf(
        "useSpringBoot3" to "true",
        "openApiNullable" to "false",
        "enumPropertyNaming" to "UPPERCASE",
    ))
    additionalProperties.set(mapOf(
        "serializationLibrary" to "jackson",
    ))
}
tasks.named("openApiGenerate") {
    if (!File(openApiSpec).exists()) {
        dependsOn(":modules:core:generateOpenApiDocs")
    }
}

tasks.named("compileKotlin") {
    dependsOn("openApiGenerate")
}

tasks.named("openApiGenerate") {
    dependsOn(":modules:core:generateOpenApiDocs")
}

val jar: Jar by tasks
val bootJar: BootJar by tasks

bootJar.enabled = false
jar.enabled = true
