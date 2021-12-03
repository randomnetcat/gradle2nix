plugins {
    kotlin("jvm")
}

dependencies {
    api("org.apache.ivy:ivy:2.5.0")
    api("com.amazonaws:aws-java-sdk-s3:1.11.946")

    testImplementation("com.adobe.testing:s3mock-junit5:2.1.28")
    testImplementation("io.strikt:strikt-core:0.28.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:5.7.0")
}

tasks {
    test {
        useJUnitPlatform {
            includeEngines("junit-jupiter")
        }
        systemProperty("fixtures", "$rootDir/fixtures")
    }
}
