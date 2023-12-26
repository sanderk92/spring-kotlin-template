import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "com.template"
version = "0.0.1"

plugins {
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.spring") version "1.9.22"
    kotlin("plugin.jpa") version "1.9.22"
    kotlin("plugin.allopen") version "1.9.22"
    kotlin("plugin.noarg") version "1.9.22"
    kotlin("kapt") version "1.9.22"
    id("org.springframework.boot") version "3.2.1"
    id("io.spring.dependency-management") version "1.1.4"
    id("org.springdoc.openapi-gradle-plugin") version "1.7.0"
    id("org.jlleitschuh.gradle.ktlint") version "12.0.3"
    id("com.google.cloud.tools.jib") version "3.4.0"
    id("org.flywaydb.flyway") version "10.4.1"
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
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework:spring-aspects")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")

    // Kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.16.1")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.22")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.22")
    implementation("org.jetbrains.kotlin:kotlin-noarg:1.9.22")

    // Database
    implementation("org.postgresql:postgresql:42.7.1")
    implementation("com.h2database:h2:2.2.224")
    implementation("org.flywaydb:flyway-core:10.4.1")

    // Mapping
    implementation("org.mapstruct:mapstruct:1.5.5.Final")
    kapt("org.mapstruct:mapstruct-processor:1.5.5.Final")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("io.mockk:mockk:1.13.8")
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
    filter {
        setExcludes(setOf("./build"))
    }
}

jib {
}

/*
 * To satisfy Gradle 8+
 */
tasks.named("forkedSpringBootRun") {
    dependsOn(":modules:core:jar")
    dependsOn(":modules:core:test")
    dependsOn(":modules:core:bootJar")
    dependsOn(":modules:core:processTestResources")
    dependsOn(":modules:core:kaptGenerateStubsTestKotlin")
    dependsOn(":modules:core:kaptTestKotlin")
    dependsOn(":modules:core:compileTestKotlin")
    dependsOn(":modules:core:runKtlintCheckOverTestSourceSet")
    dependsOn(":modules:core:runKtlintCheckOverKotlinScripts")
    dependsOn(":modules:core:ktlintMainSourceSetCheck")
    dependsOn(":modules:core:ktlintKotlinScriptCheck")
    dependsOn(":modules:core:ktlintTestSourceSetCheck")
}

tasks.named("runKtlintCheckOverMainSourceSet") {
    dependsOn(":modules:core:kaptKotlin")
}
