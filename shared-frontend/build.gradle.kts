import com.android.aaptcompiler.android.stringToInt

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
        summary = "Shared Module"
        homepage = "https://github.com/marcinsiwak/BaseKMP"
        version = "1.0"
        ios.deploymentTarget = "16.0"
        framework {
            baseName = "shared-frontend"
        }
    }

    sourceSets {
        commonMain.dependencies {
            // put your Multiplatform dependencies here
        }
    }
}

android {
    namespace = "pl.msiwak.basekmp.shared"
}
