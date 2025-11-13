package pl.msiwak.convention.config

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.cocoapods.CocoapodsExtension

class TargetConfigPlugin : Plugin<Project> {
    @OptIn(ExperimentalWasmDsl::class)
    override fun apply(project: Project) {
        with(project.extensions.getByType<KotlinMultiplatformExtension>()) {
            androidTarget {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_17)
                }
            }
            iosX64()
            iosArm64()
            iosSimulatorArm64()
            jvmToolchain(17)
        }
    }
}

fun CocoapodsExtension.baseSetup() {
    summary = "Shared Module"
    homepage = "https://github.com/marcinsiwak/BaseKMP"
    version = "1.0"
    ios.deploymentTarget = "16.2"
    xcodeConfigurationToNativeBuildType["productionRelease"] =
        org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType.RELEASE
    xcodeConfigurationToNativeBuildType["productionDebug"] =
        org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType.DEBUG
    xcodeConfigurationToNativeBuildType["stagingDebug"] =
        org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType.DEBUG
}
