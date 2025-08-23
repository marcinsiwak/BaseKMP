import pl.msiwak.convention.config.baseSetup

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinCocoapods)
    alias(libs.plugins.serialization)
    id("pl.msiwak.convention.android.config")
    id("pl.msiwak.convention.target.config")
}

apply(from = "$rootDir/gradle/buildVariants.gradle")

kotlin {
    cocoapods {
        baseSetup()
        framework {
            baseName = "navigation"
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.navigation)
            implementation(libs.kotlinx.serialization)
        }
    }
}

android {
    namespace = "pl.msiwak.cardsthegame.navigation"
}
