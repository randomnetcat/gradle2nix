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

    dependencyLocking.lockAllConfigurations()
}

tasks {
    wrapper {
        gradleVersion = "7.3.1"
        distributionType = Wrapper.DistributionType.ALL
    }
}
