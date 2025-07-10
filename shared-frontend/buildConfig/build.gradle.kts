import com.android.build.gradle.internal.tasks.factory.dependsOn
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import com.codingfeline.buildkonfig.gradle.BuildKonfigExtension
import com.codingfeline.buildkonfig.gradle.TargetConfigDsl
import org.gradle.internal.extensions.stdlib.capitalized
import org.jetbrains.kotlin.gradle.plugin.cocoapods.KotlinCocoapodsPlugin
import pl.msiwak.convention.config.baseSetup
import java.util.regex.Pattern

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinCocoapods)
    alias(libs.plugins.buildKonfig)
    id("pl.msiwak.convention.android.config")
    id("pl.msiwak.convention.target.config")
}

apply(from = "$rootDir/gradle/buildVariants.gradle")

kotlin {
    cocoapods {
        baseSetup()
        framework {
            baseName = "buildConfig"
        }
    }
}

buildkonfig {
    packageName = "pl.msiwak.baseKMP.buildConfig"

    defaultConfigs {
        buildConfigField(STRING, "BUILD_FLAVOUR", "productionDebug")
    }
    setupProductionReleaseTargets()
    setupProductionDebugTargets()
    setupStagingDebugTargets()
}

fun getCurrentVariant(): String {
    val tskReqStr: String = gradle.startParameter.taskRequests.toString()

    val pattern: Pattern = if (tskReqStr.contains("assemble")) {
        Pattern.compile("assemble(\\w+)(Release|Debug)")
    } else {
        Pattern.compile("generate(\\w+)(Release|Debug)")
    }

    val matcher = pattern.matcher(tskReqStr)

    return if (matcher.find()) {
        matcher.group(2)?.lowercase() ?: ""
    } else {
        println("NO MATCH FOUND")
        ""
    }
}

fun getCurrentFlavor(): String {
    val tskReqStr: String = gradle.startParameter.taskRequests.toString()

    val pattern: Pattern = when {
        tskReqStr.contains("assemble") -> Pattern.compile("assemble(\\w+)(Release|Debug)")
        tskReqStr.contains("bundle") -> Pattern.compile("bundle(\\w+)(Release|Debug)")
        else -> Pattern.compile("generate(\\w+)(Release|Debug)")
    }

    val matcher = pattern.matcher(tskReqStr)

    return if (matcher.find()) {
        matcher.group(1)?.lowercase() ?: ""
    } else {
        println("NO MATCH FOUND")
        ""
    }
}

tasks.create("setupBuildKonfig") {
    val variant = getCurrentVariant()
    val flavor = getCurrentFlavor()
    val androidKMPFlavor = flavor.plus(variant.capitalized())

    val buildKonfigFlavor = when {
        gradle.startParameter.taskNames.contains("composeApp:wasmJsBrowserDevelopmentRun") -> "stagingDebug"
        gradle.startParameter.taskNames.contains("composeApp:wasmJsBrowserDistribution") -> "productionRelease"
        gradle.startParameter.taskNames.contains("composeApp:wasmJsBrowserProductionRun") -> "productionRelease"
        androidKMPFlavor.isEmpty() -> project.findProperty(KotlinCocoapodsPlugin.CONFIGURATION_PROPERTY).toString()
        else -> androidKMPFlavor
    }
    project.setProperty("buildkonfig.flavor", buildKonfigFlavor)
}

tasks.preBuild.dependsOn("setupBuildKonfig")

android {
    namespace = "pl.msiwak.baseKMP.buildConfig"
}

private fun BuildKonfigExtension.setupProductionReleaseTargets() {
    targetConfigs {
        android {
            buildConfigField(STRING, "BUILD_FLAVOUR", "productionReleaseAndroid")
        }
        ios {
            buildConfigField(STRING, "BUILD_FLAVOUR", "productionReleaseIOS")
        }
        wasmJS {
            buildConfigField(STRING, "BUILD_FLAVOUR", "productionReleaseWasm")
        }
    }
}

private fun BuildKonfigExtension.setupProductionDebugTargets() {
    targetConfigs("productionDebug") {
        android {
            buildConfigField(STRING, "BUILD_FLAVOUR", "productionDebugAndroid")
        }
        ios {
            buildConfigField(STRING, "BUILD_FLAVOUR", "productionDebugIOS")
        }
        wasmJS {
            buildConfigField(STRING, "BUILD_FLAVOUR", "productionDebugWasm")
        }
    }
}

private fun BuildKonfigExtension.setupStagingDebugTargets() {
    targetConfigs("stagingDebug") {
        android {
            buildConfigField(STRING, "BUILD_FLAVOUR", "stagingDebugAndroid")
        }
        ios {
            buildConfigField(STRING, "BUILD_FLAVOUR", "stagingDebugIOS")
        }
        wasmJS {
            buildConfigField(STRING, "BUILD_FLAVOUR", "stagingDebugWasm")
        }
    }
}

private fun NamedDomainObjectContainer<TargetConfigDsl>.ios(block: TargetConfigDsl.() -> Unit) {
    listOf(
        "iosArm64",
        "iosSimulatorArm64",
        "iosX64"
    ).forEach {
        create(it, block)
    }
}

private fun NamedDomainObjectContainer<TargetConfigDsl>.android(block: TargetConfigDsl.() -> Unit) {
    create("android", block)
}

private fun NamedDomainObjectContainer<TargetConfigDsl>.wasmJS(block: TargetConfigDsl.() -> Unit) {
    create("wasmJs", block)
}
