import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.22" apply false
    kotlin("plugin.spring") version "1.9.22" apply false
    kotlin("plugin.jpa") version "1.9.22" apply false
    kotlin("plugin.allopen") version "1.9.22" apply false
    kotlin("plugin.noarg") version "1.9.22" apply false
    kotlin("kapt") version "1.9.22" apply false
    id("io.spring.dependency-management") version "1.1.4" apply false
}

subprojects {
    group = "com.template"
    version = "0.0.1"

    apply(plugin = "java")
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "org.jetbrains.kotlin.plugin.jpa")
    apply(plugin = "org.jetbrains.kotlin.plugin.allopen")
    apply(plugin = "org.jetbrains.kotlin.plugin.noarg")
    apply(plugin = "io.spring.dependency-management")

    repositories {
        mavenCentral()
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "17"
        }
    }

    val implementation by configurations
    dependencies {
        // Kotlin
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.16.1")
        implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.22")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.22")
        implementation("org.jetbrains.kotlin:kotlin-noarg:1.9.22")

        // Logging
        implementation("io.github.oshai:kotlin-logging-jvm:5.1.0")
    }
}
