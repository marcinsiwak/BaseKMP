import pl.msiwak.convention.config.baseSetup

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinCocoapods)
    id("pl.msiwak.convention.android.config")
    id("pl.msiwak.convention.target.config")
}

apply(from = "$rootDir/gradle/buildVariants.gradle")

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

            }
            commonMain.dependencies {
                implementation(projects.shared.model)

                implementation(libs.ktor.core)
                implementation(libs.ktor.contentNegation)
                implementation(libs.ktor.logger)
                implementation(libs.ktor.serialization)
            }
            iosMain.dependencies {
                implementation(libs.ktor.ios)
            }
        }
    }
}

android {
    namespace = "pl.msiwak.fantasyleague.network"
}
