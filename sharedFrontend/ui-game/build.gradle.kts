import pl.msiwak.convention.config.baseSetup

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinCocoapods)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    id("pl.msiwak.convention.android.config")
    id("pl.msiwak.convention.target.config")
}

apply(from = "$rootDir/gradle/buildVariants.gradle")

kotlin {
    cocoapods {
        baseSetup()
        framework {
            baseName = "ui-game"
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.sharedFrontend.navigation)
            implementation(projects.sharedFrontend.commonModel)
            implementation(projects.sharedFrontend.commonResources)
            implementation(projects.sharedFrontend.domain)

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.compose.navigation)
            implementation(libs.koin.compose)
        }
    }
}

android {
    namespace = "pl.msiwak.baseKMP.ui.game"
}
