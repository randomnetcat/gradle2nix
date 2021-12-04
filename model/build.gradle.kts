plugins {
    kotlin("jvm")
    kotlin("kapt")
}

dependencies {
    api("com.squareup.moshi:moshi:1.12.0")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:1.12.0")
    implementation("net.swiftzer.semver:semver:1.1.1")
}
