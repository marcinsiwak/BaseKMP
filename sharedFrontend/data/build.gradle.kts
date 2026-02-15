plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
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
            baseName = "data"
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.sharedFrontend.network)
            implementation(projects.sharedFrontend.commonModel)
            implementation(projects.sharedFrontend.globalLoaderManager)

            implementation(libs.kotlinx.coroutines)
        }
    }
}

android {
    namespace = "pl.msiwak.basekmp.data"
}
