plugins {
    alias(libs.plugins.kotlinJvm)
}

dependencies {
    implementation(projects.server.database)
    implementation(projects.server.network)

    implementation(projects.shared.model)
}
