plugins {
    `embedded-kotlin`
    kotlin("kapt")
}

dependencies {
    api("com.squareup.moshi:moshi:1.11.0")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:1.11.0")
    implementation("net.swiftzer.semver:semver:1.1.1")
}
