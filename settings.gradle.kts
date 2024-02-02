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

include(":feature:import-deeplinks:ui")
include(":feature:import-deeplinks:domain")

include(":feature:export-deeplinks:ui")
include(":feature:export-deeplinks:domain")

include(":feature:deeplink-details:ui")
include(":feature:folder-details:ui")

include(":feature:settings:domain")
include(":feature:settings:ui")
