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
            baseName = "remote-config"
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.sharedFrontend.commonModel)

            implementation(libs.koin.core)
            implementation(libs.kotlinx.serialization)
            implementation(libs.firebase.gitlive.remoteConfig)
        }
    }
}

android {
    namespace = "pl.msiwak.basekmp.remoteconfig"
}