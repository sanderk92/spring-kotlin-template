import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("org.springframework.boot") version "3.1.7" // 3.2.1 currently causes wiremock issues
    id("org.openapi.generator") version "7.2.0"
}

sourceSets {
    main {
        java {
            // OpenApi generated sources
            srcDir("$projectDir/build/generated/src/main/kotlin")
        }
    }
}

val cucumberVersion = "7.15.0"

dependencies {
    implementation(project(mapOf("path" to ":modules:core")))

    // Spring
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    testImplementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-test")

    // Test
    testImplementation("com.h2database:h2:2.2.224")
    testImplementation("org.junit.platform:junit-platform-launcher")
    testImplementation("org.junit.vintage:junit-vintage-engine")
    testImplementation("org.wiremock:wiremock:3.3.1")
    testImplementation("org.assertj:assertj-core:3.24.2")
    testImplementation("io.mockk:mockk:1.13.8")

    // Cucumber
    testImplementation("io.cucumber:cucumber-spring:$cucumberVersion")
    testImplementation("io.cucumber:cucumber-java:$cucumberVersion")
    testImplementation("io.cucumber:cucumber-junit:$cucumberVersion")
}

openApiGenerate {
    generatorName.set("kotlin")
    library.set("jvm-spring-webclient")
    inputSpec.set("${project(":modules:core").projectDir}/build/openapi.json")
    outputDir.set("$projectDir/build/generated")
    configOptions.set(mapOf(
        "useSpringBoot3" to "true",
    ))
    additionalProperties.set(mapOf(
        "serializationLibrary" to "jackson",
    ))
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
