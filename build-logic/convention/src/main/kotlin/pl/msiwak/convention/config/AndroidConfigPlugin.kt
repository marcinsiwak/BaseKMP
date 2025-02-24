package pl.msiwak.convention.config

import com.android.build.gradle.BaseExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.the

class AndroidConfigPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val javaVersion = JavaVersion.VERSION_17
        val libs = project.the<VersionCatalogsExtension>().named("libs")

        with(project.extensions.getByType<BaseExtension>()) {
            compileSdkVersion(34)
            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_17
                targetCompatibility = JavaVersion.VERSION_17
            }

            with(defaultConfig) {
                minSdk = 24
            }
        }

//        project.kotlinExtension.jvmToolchain(
//            JavaLanguageVersion.of(javaVersion.majorVersion).asInt()
//        )
    }
}
