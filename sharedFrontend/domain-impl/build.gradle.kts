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
            baseName = "domain-impl"
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.sharedFrontend.commonModel)
            implementation(projects.sharedFrontend.domain)
            implementation(projects.sharedFrontend.data)
            implementation(libs.kotlinx.coroutines)
        }
    }
}

android {
    namespace = "pl.msiwak.cardsthegame.domainimpl"
}
