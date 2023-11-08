import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "com.template"
version = "0.0.1"

plugins {
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
    kotlin("plugin.jpa") version "1.5.21"
    kotlin("plugin.allopen") version "1.5.21"
    kotlin("plugin.noarg") version "1.9.10"
    kotlin("kapt") version "1.9.10"
    id("org.springframework.boot") version "3.1.1"
    id("io.spring.dependency-management") version "1.1.3"
    id("org.springdoc.openapi-gradle-plugin") version "1.7.0"
    id("org.jlleitschuh.gradle.ktlint") version "10.3.0"
    id("com.google.cloud.tools.jib") version "3.3.0"
}

repositories {
    mavenCentral()
}

sourceSets {
    main {
        java {
            // MapStruct generated sources
            srcDir("$buildDir/generated/source/kapt/main")
        }
    }
}

dependencies {
    // Spring
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework:spring-aspects:6.0.3")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")

    // Kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlin:kotlin-noarg")

    // Other
    implementation("org.postgresql:postgresql")
    implementation("com.h2database:h2:2.2.224")
    implementation("org.mapstruct:mapstruct:1.5.5.Final")
    kapt("org.mapstruct:mapstruct-processor:1.5.5.Final")

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
    disabledRules.set(setOf("no-wildcard-imports"))
}

jib {
}
