import pl.msiwak.convention.config.baseSetup

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinCocoapods)
    alias(libs.plugins.serialization)
    id("pl.msiwak.convention.android.config")
    id("pl.msiwak.convention.target.config")
}



kotlin {
    cocoapods {
        baseSetup()
        framework {
            baseName = "network"
        }
    }

    sourceSets {
        commonMain.dependencies {
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
                implementation(projects.sharedFrontend.commonModel)
                implementation(projects.sharedFrontend.gameManager)

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

android {
    namespace = "pl.msiwak.cardsthegame.network"
}
