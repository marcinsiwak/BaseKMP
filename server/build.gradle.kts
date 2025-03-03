plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    application
}

group = "pl.msiwak.basekmp"
version = "1.0.0"
application {
    mainClass.set("pl.msiwak.basekmp.ApplicationKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=${extra["io.ktor.development"] ?: "false"}")
}

dependencies {
    implementation(projects.shared)
    implementation(projects.shared.model)
    implementation(projects.server.domain)
    implementation(projects.server.data)
    implementation(projects.server.database)
    implementation(projects.server.routing)

    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.koin.ktor)
}