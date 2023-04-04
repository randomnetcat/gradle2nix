plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    api("org.apache.ivy:ivy:2.5.0")
    api("com.amazonaws:aws-java-sdk-s3:1.11.946")

    testImplementation("com.adobe.testing:s3mock-junit5:2.1.28")
    testImplementation(libs.strikt.core)
    testImplementation(libs.strikt.jvm)
    testImplementation(libs.junit.api)
    testImplementation(libs.junit.params)
    testRuntimeOnly(libs.junit.engine)
    testRuntimeOnly(libs.junit.launcher)
}

tasks {
    test {
        useJUnitPlatform {
            includeEngines("junit-jupiter")
        }
        systemProperty("fixtures", "$rootDir/fixtures")
    }
}
