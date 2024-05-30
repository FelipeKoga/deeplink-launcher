@file:Suppress("UnstableApiUsage")

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
        gradlePluginPortal()
        mavenCentral()
    }
}

include(":androidApp")
include(":desktop")
include(":shared")
include(":core:database")
include(":core:domain")
include(":core:designsystem")
include(":core:sharedui")
include(":core:navigation")
include(":core:resources")
include(":core:preferences")

include(":feature:home")
include(":feature:home")
include(":feature:import-deeplinks")
include(":feature:import-deeplinks")
include(":feature:export-deeplinks")
include(":feature:deeplink-details")
include(":feature:folder-details")
include(":feature:settings")
include(":feature:settings")
