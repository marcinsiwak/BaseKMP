plugins {
    alias(libs.plugins.kotlinJvm)
}

dependencies {
    implementation(projects.server.database)
}