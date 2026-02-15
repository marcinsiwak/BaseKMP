rootProject.name = "BaseKMP"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        maven("https://packages.jetbrains.team/maven/p/kt/dev")
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        maven("https://packages.jetbrains.team/maven/p/kt/dev")
        mavenCentral()
    }
}

include(":composeApp")
include(":sharedFrontend")
include(":sharedFrontend:buildConfig")
include(":sharedFrontend:navigation")
include(":sharedFrontend:ui-example")
include(":sharedFrontend:ui-ai-generated")
include(":sharedFrontend:common-model")
include(":sharedFrontend:common-resources")
include(":sharedFrontend:network")
include(":sharedFrontend:data")
include(":sharedFrontend:domain")
include(":sharedFrontend:domain-impl")
include(":sharedFrontend:global-loader-manager")
include(":sharedFrontend:remote-config")
