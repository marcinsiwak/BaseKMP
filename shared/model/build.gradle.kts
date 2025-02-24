plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    id("pl.msiwak.convention.android.config")
    id("pl.msiwak.convention.target.config")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // add libs
        }
    }
}

android {
    namespace = "pl.msiwak.basekmp.shared.model"
}
