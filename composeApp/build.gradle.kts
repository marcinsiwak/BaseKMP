import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)

    id("pl.msiwak.convention.target.config")
    id("pl.msiwak.convention.android.config")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

val versionMajor = 1
val versionMinor = 0
val versionPatch = 0
val versionBuild = 0
val versionCode = 1_000_000 * versionMajor + 10_000 * versionMinor + 100 * versionPatch + versionBuild

val appVersionCode: Int = Integer.valueOf(versionCode)

kotlin {
//    cocoapods {
//        baseSetup()
//        podfile = project.file("../iosApp/Podfile")
//        framework {
//            baseName = "ComposeApp"
//            linkerOpts("-ObjC")
//
////            export(projects.sharedFrontend.network)
//            export(projects.sharedFrontend.commonModel)
//            export(projects.libConnection)
//        }
//
////        pod("FirebaseCore", linkOnly = true)
////        pod("FirebaseCrashlytics", linkOnly = true)
////        pod("FirebaseRemoteConfig", linkOnly = true)
////        pod("FirebaseAnalytics", linkOnly = true)
//    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true

            export(projects.sharedFrontend.commonModel)
            export(projects.libConnection)
        }
    }

    sourceSets {

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
        }
        commonMain.dependencies {
            implementation(projects.sharedFrontend)
            implementation(projects.sharedFrontend.navigation)
            implementation(projects.sharedFrontend.uiExample)
            implementation(projects.sharedFrontend.uiAiGenerated)
            implementation(projects.sharedFrontend.uiGame)
            implementation(projects.sharedFrontend.commonResources)
            implementation(projects.sharedFrontend.commonModel)
            implementation(projects.sharedFrontend.data)
            implementation(projects.sharedFrontend.domain)
            implementation(projects.sharedFrontend.domainImpl)
            implementation(projects.sharedFrontend.gameManager)
            implementation(projects.sharedFrontend.globalLoaderManager)
            implementation(projects.sharedFrontend.remoteConfig)
            implementation(projects.libConnection)
            api(projects.sharedFrontend.network)

            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewModel)
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.compose.navigation)
            implementation(libs.firebase.gitlive.crashlytics)
            implementation(libs.firebase.gitlive.analytics)
        }
    }
}

android {
    namespace = "pl.msiwak.cardsthegame"

    defaultConfig {
        applicationId = "pl.msiwak.cardsthegame.android"
        versionCode = appVersionCode
        versionName = "$versionMajor.$versionMinor.$versionPatch"
    }
    val signingPropertiesFile = rootProject.file("signing/release.properties")

    signingConfigs {
        with(signingPropertiesFile) {
            if (!exists()) return@with

            val releaseKeystoreProp = Properties()
            releaseKeystoreProp.load(FileInputStream(this))
            maybeCreate("release")
            getByName("release") {
                keyAlias = releaseKeystoreProp["keyAlias"] as String
                keyPassword = releaseKeystoreProp["keyPassword"] as String
                storeFile = rootProject.file(releaseKeystoreProp["storeFile"] as String)
                storePassword = releaseKeystoreProp["storePassword"] as String
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")

        }
        debug {
            applicationIdSuffix = ".debug"
            matchingFallbacks.add("release")
        }
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}
