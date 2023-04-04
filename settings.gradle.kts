@file:Suppress("UnstableApiUsage")

pluginManagement {
    val shadowVersion: String by settings
    val stutterVersion: String by settings

    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "com.github.johnrengelman.shadow" -> useVersion(shadowVersion)
                "org.ajoberstar.stutter" -> useVersion(stutterVersion)
            }
        }
    }
}

//plugins {
//    kotlin("jvm") apply false
//    kotlin("kapt") apply false
//    id("com.github.johnrengelman.shadow") apply false
//    id("org.ajoberstar.stutter") apply false
//}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven { url = uri("https://repo.gradle.org/gradle/libs-releases") }
    }

    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)

    versionCatalogs {
        create("libs") {
            version("junit", "5.9.2")
            library("junit-api", "org.junit.jupiter", "junit-jupiter-api").versionRef("junit")
            library("junit-params", "org.junit.jupiter", "junit-jupiter-params").versionRef("junit")
            library("junit-engine", "org.junit.jupiter", "junit-jupiter-engine").versionRef("junit")
            library("junit-launcher", "org.junit.platform", "junit-platform-launcher").version("1.9.2")

            version("moshi", "1.14.0")
            library("moshi-core", "com.squareup.moshi", "moshi").versionRef("moshi")
            library("moshi-adapters", "com.squareup.moshi", "moshi-adapters").versionRef("moshi")
            library("moshi-kotlin", "com.squareup.moshi", "moshi-kotlin").versionRef("moshi")
            library("moshi-codegen", "com.squareup.moshi", "moshi-kotlin-codegen").versionRef("moshi")

            version("strikt", "0.34.1")
            library("strikt-core", "io.strikt", "strikt-core").versionRef("strikt")
            library("strikt-jvm", "io.strikt", "strikt-jvm").versionRef("strikt")

            version("kotlin", "1.6.21")
            plugin("kotlin-jvm", "org.jetbrains.kotlin.jvm").versionRef("kotlin")
            plugin("kotlin-kapt", "org.jetbrains.kotlin.kapt").versionRef("kotlin")
        }
    }
}

buildscript {
    configurations.classpath {
        resolutionStrategy.activateDependencyLocking()
    }
}

include(":app", ":ivy", ":model", ":plugin")
