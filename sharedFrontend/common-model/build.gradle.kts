import pl.msiwak.convention.config.baseSetup

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinCocoapods)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.serialization)
    id("pl.msiwak.convention.android.config")
    id("pl.msiwak.convention.target.config")
}

kotlin {
    cocoapods {
        baseSetup()
        framework {
            baseName = "common-model"
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.sharedFrontend.navigation)
            implementation(projects.libConnection)

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.compose.navigation)
            implementation(libs.kotlinx.serialization)
            implementation(libs.kotlinx.datetime)
        }
    }
}

android {
    namespace = "pl.msiwak.cardsthegame.common.model"
}
