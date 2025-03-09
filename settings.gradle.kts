rootProject.name = "FantasyLeague"
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
include(":shared-frontend:ui-create-type")
include(":shared-frontend:common-model")

include(":server")
include(":server:data")
include(":server:domain")
include(":server:database")
include(":server:routing")
