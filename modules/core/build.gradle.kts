import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "com.template"
version = "1.0-SNAPSHOT"

plugins {
    id("org.springframework.boot") version "3.1.1"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
    id("org.springdoc.openapi-gradle-plugin") version "1.7.0"
    id("org.jlleitschuh.gradle.ktlint") version "10.3.0"
    id("com.google.cloud.tools.jib") version "3.3.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework:spring-aspects:6.0.3")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("io.mockk:mockk:1.12.4")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

openApi {
    apiDocsUrl.set("http://localhost:8080/api/v3/api-docs")
    customBootRun {
        args.set(listOf("--spring.profiles.active=openapi"))
    }
}

springBoot {
    buildInfo()
}

ktlint {
    enableExperimentalRules.set(true)
    disabledRules.set(setOf("no-wildcard-imports"))
}

jib {
    to {
        // TODO publish image
    }
}
