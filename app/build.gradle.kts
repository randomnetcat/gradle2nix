import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.kapt)
    application
}

dependencies {
    implementation(project(":model"))
    implementation(kotlin("reflect"))
    implementation("org.gradle:gradle-tooling-api:${gradle.gradleVersion}")
    implementation("com.github.ajalt:clikt:2.8.0")
    implementation("org.slf4j:slf4j-api:2.0.0-alpha1")
    runtimeOnly("org.slf4j:slf4j-simple:2.0.0-alpha1")
    implementation(libs.moshi.adapters)
    implementation(libs.moshi.kotlin)
    kapt(libs.moshi.codegen)
    implementation("com.squareup.okio:okio:3.0.0-alpha.1")

    testRuntimeOnly(kotlin("reflect"))
    testImplementation("org.spekframework.spek2:spek-dsl-jvm:2.0.15")
    testRuntimeOnly("org.spekframework.spek2:spek-runner-junit5:2.0.15")
    testImplementation(libs.strikt.core)
    testImplementation(libs.strikt.jvm)
}

application {
    mainClass.set("org.nixos.gradle2nix.MainKt")
    applicationName = "gradle2nix"

    applicationDistribution.with(copySpec {
        val currentPath = rootDir.resolve("gradle-env.nix")
        val nextPath = rootDir.resolve("gradle-env.nix.next")

        from(if (nextPath.exists()) nextPath else currentPath)
        rename("gradle-env\\.nix\\.next", "gradle-env.nix")
        into("share")
    })

    applicationDistribution.with(copySpec {
        from(tasks.getByPath(":plugin:shadowJar"))
        into("share")
        rename("plugin.*\\.jar", "plugin.jar")
    })
}

sourceSets {
    test {
        resources {
            srcDir("$rootDir/fixtures")
        }
    }
}

tasks {
    (run) {
        dependsOn(installDist)
        doFirst {
            systemProperties("org.nixos.gradle2nix.share" to installDist.get().destinationDir.resolve("share"))
        }
    }

    startScripts {
        doLast {
            unixScript.writeText(unixScript.readText().replace("exec \"\$JAVACMD\"", "exec \"\$JAVACMD\" \"-Dorg.nixos.gradle2nix.share=\$APP_HOME/share\""))
            windowsScript.writeText(windowsScript.readText().replace("\"%JAVA_EXE%\" %DEFAULT_JVM_OPTS%", "\"%JAVA_EXE%\" \"-Dorg.nixos.gradle2nix.share=%APP_HOME%\" %DEFAULT_JVM_OPTS%"))
        }
    }

    test {
        dependsOn(installDist)
        doFirst {
            systemProperties("org.nixos.gradle2nix.share" to installDist.get().destinationDir.resolve("share"))
        }
        useJUnitPlatform {
            includeEngines("spek2")
        }
        testLogging {
            events("passed", "skipped", "failed")
        }
    }

    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
        }
    }
}
