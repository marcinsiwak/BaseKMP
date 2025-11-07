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
            baseName = "data"
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.sharedFrontend.network)
            implementation(projects.sharedFrontend.commonModel)
            implementation(projects.shared.model)
            implementation(projects.sharedFrontend.globalLoaderManager)

            implementation(libs.kotlinx.coroutines)
        }
    }
}

android {
    namespace = "pl.msiwak.cardsthegame.data"
}
