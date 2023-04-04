plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.kapt)
}

dependencies {
    api(libs.moshi.core)
    kapt(libs.moshi.codegen)
    implementation("net.swiftzer.semver:semver:1.1.1")
}
