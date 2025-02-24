plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

gradlePlugin {
    plugins {
        // todo create and register plugings
    }
}

dependencies {
    implementation(gradleApi())
    implementation(libs.gradle.ksp)
    implementation(libs.gradle.kotlin)
    implementation(libs.gradle.android)
}
