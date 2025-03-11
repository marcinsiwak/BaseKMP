plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    application
}

group = "pl.msiwak"
version = "1.0.0"
application {
    mainClass.set("pl.msiwak.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
    implementation(projects.shared)
    implementation(projects.shared.model)
    implementation(projects.server.domain)
    implementation(projects.server.data)
    implementation(projects.server.database)
    implementation(projects.server.routing)
    implementation(projects.server.network)

    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.config.yaml)
    implementation(libs.ktor.server.contentNegotiation)
    implementation(libs.ktor.server.json)
    implementation(libs.koin.logger.slf4j)
    implementation(libs.koin.ktor)
}
