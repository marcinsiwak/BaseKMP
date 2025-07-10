import pl.msiwak.convention.config.baseSetup

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinCocoapods)
    id("pl.msiwak.convention.android.config")
    id("pl.msiwak.convention.target.config")
}

apply(from = "$rootDir/gradle/buildVariants.gradle")

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
        }
    }
}

android {
    namespace = "pl.msiwak.baseKMP.domain"
}
