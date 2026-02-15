plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.serialization)
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
    namespace = "pl.msiwak.basekmp.navigation"
}
