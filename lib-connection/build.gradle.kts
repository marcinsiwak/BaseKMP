import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.serialization)
}

kotlin {
    jvm()
    androidLibrary {
        namespace = "pl.msiwak.lib_connection"
        compileSdk = 36
        minSdk = 24

        withJava() // enable java compilation support
        withHostTestBuilder {}.configure {}
        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }

        compilations.configureEach {
            compilerOptions.configure {
                jvmTarget.set(
                    JvmTarget.JVM_17
                )
            }
        }
    }
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "lib_connection"
        }
    }
    jvmToolchain(17)

    sourceSets {
        sourceSets {
            androidMain.dependencies {
                implementation(libs.ktor.android)
                implementation(libs.ktor.cio)
                implementation(libs.ktor.server.core)
                implementation(libs.ktor.server.cio)
                implementation(libs.ktor.server.websocket)
                implementation(libs.ktor.server.contentNegotiation)
                implementation(libs.androidx.annotation.jvm)
            }
            commonMain.dependencies {
                implementation(libs.koin.core)
                implementation(libs.ktor.core)
                implementation(libs.ktor.contentNegation)
                implementation(libs.ktor.websockets)
                implementation(libs.ktor.logger)
                implementation(libs.ktor.serialization)
                implementation(libs.kotlinx.serialization)
            }
            iosMain.dependencies {
                implementation(libs.ktor.ios)
            }
        }
    }
}
