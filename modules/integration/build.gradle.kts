import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.spring") version "1.9.22"
    kotlin("plugin.jpa") version "1.9.22"
    kotlin("plugin.allopen") version "1.9.22"
    kotlin("plugin.noarg") version "1.9.22"
    // org.springframework.boot 3.2.0 currently causes wiremock issues
    id("org.springframework.boot") version "3.1.7"
    id("io.spring.dependency-management") version "1.1.4"
    id("org.openapi.generator") version "7.2.0"
}

repositories {
    mavenCentral()
}

sourceSets {
    main {
        java {
            // OpenApi generated sources
            srcDir("$buildDir/generated/src/main/kotlin")
        }
    }
}

val cucumberVersion = "7.15.0"

dependencies {
    implementation(project(mapOf("path" to ":modules:core")))

    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.22")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.22")
    implementation("io.github.oshai:kotlin-logging-jvm:5.1.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.16.1")

    implementation("org.springframework.boot:spring-boot-starter-webflux")
    testImplementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-test")
    testImplementation("com.h2database:h2:2.2.224")
    testImplementation("org.wiremock:wiremock:3.3.1")

    testImplementation("org.junit.platform:junit-platform-launcher")
    testImplementation("org.junit.vintage:junit-vintage-engine")
    testImplementation("org.assertj:assertj-core:3.24.2")
    testImplementation("io.mockk:mockk:1.13.8")

    testImplementation("io.cucumber:cucumber-spring:$cucumberVersion")
    testImplementation("io.cucumber:cucumber-java:$cucumberVersion")
    testImplementation("io.cucumber:cucumber-junit:$cucumberVersion")
}

tasks.named("compileKotlin") {
    dependsOn("openApiGenerate")
}

tasks.named("openApiGenerate") {
    dependsOn(":modules:core:generateOpenApiDocs")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

openApiGenerate {
    generatorName.set("kotlin")
    library.set("jvm-spring-webclient")
    inputSpec.set("${project(":modules:core").buildDir}/openapi.json")
    outputDir.set("$buildDir/generated")
    configOptions.set(mapOf(
        "useSpringBoot3" to "true",
    ))
    additionalProperties.set(mapOf(
        "serializationLibrary" to "jackson",
    ))
}

val jar: Jar by tasks
val bootJar: BootJar by tasks

bootJar.enabled = false
jar.enabled = true
