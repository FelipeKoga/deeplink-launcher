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
include(":shared")
include(":core:database")
include(":core:domain")
include(":core:designsystem")
include(":core:sharedui")
include(":core:navigation")
include(":core:resources")
include(":feature:home")
include(":feature:export")
include(":feature:deeplink-details")
include(":feature:folder-details")

include(":feature:import-deeplink:ui")
include(":feature:import-deeplink:domain")

include(":feature:settings:domain")
include(":feature:settings:ui")
