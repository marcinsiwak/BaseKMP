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
        mavenCentral()
    }
}

include(":composeApp")
include(":shared")
include(":shared:model")
include(":shared-frontend")
include(":shared-frontend:buildConfig")
include(":shared-frontend:navigation")
include(":shared-frontend:ui-example")
include(":shared-frontend:common-model")
include(":shared-frontend:common-resources")
include(":shared-frontend:network")
include(":shared-frontend:data")
include(":shared-frontend:domain")
include(":shared-frontend:domain-impl")

include(":server")
include(":server:data")
include(":server:domain")
include(":server:database")
include(":server:routing")
include(":server:network")
