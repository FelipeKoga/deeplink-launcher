@file:Suppress("UnstableApiUsage")

include(":navigation")


include(":core")


include(":feature")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

include(":androidApp")
include(":shared")
include(":core:database")
include(":core:domain")
include(":core:designsystem")
include(":core:sharedui")
include(":core:navigation")
include(":feature:home")
include(":feature:import")
include(":feature:export")
include(":feature:deeplink-details")
include(":feature:folder-details")
