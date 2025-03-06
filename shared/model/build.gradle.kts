plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    id("pl.msiwak.convention.android.config")
    id("pl.msiwak.convention.target.config")
    id("pl.msiwak.convention.releaseonly.config")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // add libs
        }
    }
}

android {
    namespace = "pl.msiwak.fantasyleague.shared.model"
}
