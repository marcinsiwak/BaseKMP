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
            baseName = "sharedFrontend"
        }
    }

    sourceSets {
        commonMain.dependencies {
        }
    }
}

android {
    namespace = "pl.msiwak.cardsthegame.shared"
}
