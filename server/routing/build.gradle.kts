plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
}

dependencies {
    implementation(projects.server.domain)

    implementation(libs.ktor.server.core)
    implementation(libs.koin.ktor)
}
