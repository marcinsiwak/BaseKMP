plugins {
    alias(libs.plugins.kotlinJvm)
}

dependencies {

    implementation(libs.postgresql)
    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.kotlin.datetime)
    implementation(libs.flyway.core)
}