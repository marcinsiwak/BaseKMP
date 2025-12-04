plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    id("pl.msiwak.convention.android.config")
    id("pl.msiwak.convention.target.config")
}



kotlin {
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ui-game"
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.sharedFrontend.navigation)
            implementation(projects.sharedFrontend.commonModel)
            implementation(projects.sharedFrontend.commonResources)
            implementation(projects.sharedFrontend.domain)
            implementation(projects.sharedFrontend.gameManager)
            implementation(projects.sharedFrontend.data)
            implementation(projects.sharedFrontend.remoteConfig)

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.compose.navigation)
            implementation(libs.compose.navigation.back)
            implementation(libs.koin.compose)
            implementation(libs.compottie)
        }
    }
}

android {
    namespace = "pl.msiwak.baseKMP.ui.game"
}
