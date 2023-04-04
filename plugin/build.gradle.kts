buildscript {
    configurations.classpath {
        resolutionStrategy.activateDependencyLocking()
    }
}

plugins {
    `kotlin-dsl`
    id("com.github.johnrengelman.shadow")
    id("org.ajoberstar.stutter")
}

sourceSets {
    test {
        java.srcDir("src/test/kotlin")
    }
}

dependencyLocking {
    lockAllConfigurations()
}

configurations {
    implementation {
        dependencies.remove(project.dependencies.gradleApi())
    }
}

dependencies {
    compileOnly("org.gradle:gradle-tooling-api:${gradle.gradleVersion}")
    implementation("org.apache.maven:maven-repository-metadata:3.6.3")
    implementation(project(":ivy"))
    implementation(project(":model"))
    shadow(gradleApi())

    compatTestImplementation("com.adobe.testing:s3mock-junit5:2.1.28")
    compatTestImplementation("com.squareup.okio:okio:3.0.0-alpha.1")
    compatTestImplementation("dev.minutest:minutest:2.0.0-alpha")
    compatTestImplementation("io.javalin:javalin:3.13.3")
    compatTestImplementation("io.strikt:strikt-core:0.28.2")
    compatTestImplementation(libs.junit.api)
    compatTestImplementation(libs.junit.params)
    compatTestImplementation(kotlin("reflect"))
    compatTestImplementation(kotlin("stdlib-jdk8"))
    compatTestImplementation(kotlin("test-junit5"))
    compatTestImplementation(gradleTestKit())
    compatTestImplementation(project(":model"))
    compatTestRuntimeOnly(libs.junit.engine)
    compatTestRuntimeOnly(libs.junit.launcher)
}

gradlePlugin {
    plugins {
        register("gradle2nix") {
            id = "org.nixos.gradle2nix"
            displayName = "gradle2nix"
            description = "Expose Gradle tooling model for the gradle2nix tool"
            implementationClass = "org.nixos.gradle2nix.Gradle2NixPlugin"
        }
    }
}

stutter {
    isSparse = true
    java(8) {
        compatibleRange("4.4")
    }
    java(11) {
        compatibleRange("5.0")
    }
}

tasks {
    pluginUnderTestMetadata {
        pluginClasspath.setFrom(files(shadowJar))
    }

    withType<Test> {
        useJUnitPlatform {
            includeEngines("junit-jupiter")
        }

        // Default logging config exposes a classpath conflict between
        // the Gradle API and SFL4J.
        // (Sprint Boot is used in S3Mock)
        systemProperty("org.springframework.boot.logging.LoggingSystem", "org.springframework.boot.logging.java.JavaLoggingSystem")

        systemProperty("fixtures", "$rootDir/fixtures")
    }
}
