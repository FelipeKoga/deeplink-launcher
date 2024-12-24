@file:Suppress("UnstableApiUsage")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    includeBuild("build-logic")
    repositories {
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

include(":androidApp")
include(":desktopApp")
include(":shared")
include(":core:database")
include(":core:domain")
include(":core:designsystem")
include(":core:sharedui")
include(":core:navigation")
include(":core:resources")
include(":core:preferences")
include(":core:purchase")

include(":feature:home")
include(":feature:import-data")
include(":feature:export-data")
include(":feature:deeplink-details")
include(":feature:folder-details")
include(":feature:settings")
