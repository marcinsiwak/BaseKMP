plugins {
    alias(libs.plugins.kotlinJvm)
}

dependencies {
    implementation(projects.server.domain
    )
    implementation(libs.ktor.server.core)
}
