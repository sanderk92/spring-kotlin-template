import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("org.springframework.boot") version "3.1.1"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("org.openapi.generator") version "7.0.1"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
}

repositories {
    mavenCentral()
}

sourceSets {
    main {
        java {
            srcDir("$buildDir/generated/src/main/kotlin")
        }
    }
}

val cucumberVersion = "7.14.0"
val testContainersVersion = "1.19.1"

dependencies {
    implementation(project(mapOf("path" to ":modules:core")))

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("io.github.oshai:kotlin-logging-jvm:5.1.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.2")

    implementation("org.springframework.boot:spring-boot-starter-webflux:3.1.4")
    testImplementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-test")

    testImplementation("org.junit.platform:junit-platform-launcher")
    testImplementation("org.junit.vintage:junit-vintage-engine")
    testImplementation("org.assertj:assertj-core:3.24.2")
    testImplementation("org.wiremock:wiremock:3.2.0")
    testImplementation("io.mockk:mockk:1.13.8")

    testImplementation("io.cucumber:cucumber-spring:$cucumberVersion")
    testImplementation("io.cucumber:cucumber-java:$cucumberVersion")
    testImplementation("io.cucumber:cucumber-junit:$cucumberVersion")

    testImplementation("org.testcontainers:testcontainers:$testContainersVersion")
    testImplementation("org.testcontainers:junit-jupiter:$testContainersVersion")
    testImplementation("org.testcontainers:mongodb:$testContainersVersion")
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
