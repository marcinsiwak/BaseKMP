plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinJvm) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.kotlinCocoapods) apply false
    alias(libs.plugins.ktlint)
    alias(libs.plugins.detekt)
    id("com.google.gms.google-services") version "4.4.4" apply false
    id("com.google.firebase.crashlytics") version "3.0.6" apply false
    alias(libs.plugins.android.kotlin.multiplatform.library) apply false
}

private val detektFormattingLib = libs.detekt.formatting

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "io.gitlab.arturbosch.detekt")

    detekt {
        buildUponDefaultConfig = true
        allRules = false
        ignoreFailures = false
        parallel = true
        config.setFrom(file("$rootDir/config/detekt/detekt.yml"))
    }

    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        filter {
            exclude { element ->
                element.file.path.contains("generated/")
            }
            exclude { element ->
                element.file.path.contains("buildkonfig/")
            }
            exclude { element ->
                element.file.path.contains("Buildkonfig/")
            }
            exclude { element ->
                element.file.path.endsWith("main.kt")
            }
            include("**/kotlin/**")
        }
    }

    dependencies {
        detektPlugins(detektFormattingLib)
    }
}

dependencyLocking {
    lockAllConfigurations()
}
