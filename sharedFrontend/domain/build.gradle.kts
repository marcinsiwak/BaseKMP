import pl.msiwak.convention.config.baseSetup

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinCocoapods)
    id("pl.msiwak.convention.android.config")
    id("pl.msiwak.convention.target.config")
}

kotlin {
    cocoapods {
        baseSetup()
        framework {
            baseName = "domain"
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.sharedFrontend.data)
            implementation(projects.sharedFrontend.commonModel)
            implementation(projects.libConnection)
            implementation(libs.kotlinx.coroutines)
        }
    }
}

android {
    namespace = "pl.msiwak.cardsthegame.domain"
}
