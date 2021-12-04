plugins {
    base
}

group = "org.nixos.gradle2nix"
version = property("VERSION") ?: "unspecified"

subprojects {
    group = rootProject.group
    version = rootProject.version
}

allprojects {
    extensions.findByType<JavaPluginExtension>()?.apply {
        toolchain.languageVersion.set(JavaLanguageVersion.of("1.8"))

        sourceSets.all {
            configurations {
                named(compileClasspathConfigurationName) {
                    resolutionStrategy.activateDependencyLocking()
                }
                named(runtimeClasspathConfigurationName) {
                    resolutionStrategy.activateDependencyLocking()
                }
            }
        }
    }

    tasks.register("lock") {
        doFirst {
            assert(gradle.startParameter.isWriteDependencyLocks)
            file("buildscript-gradle.lockfile").delete()
            file("gradle.lockfile").delete()
        }
        doLast {
            configurations.matching { it.isCanBeResolved }.all { resolve() }
        }
    }
}

tasks {
    wrapper {
        gradleVersion = "7.3.1"
        distributionType = Wrapper.DistributionType.ALL
    }
}
