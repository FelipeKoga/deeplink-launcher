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


include(
    ":androidApp",
    ":desktopApp",
    ":feature:deeplink:ui",
    ":feature:deeplink:api",
    ":feature:deeplink:impl",
    ":feature:import-export:ui",
    ":feature:import-export:api",
    ":feature:import-export:impl",
    ":feature:preferences:api",
    ":feature:preferences:impl",
    ":feature:settings:ui",
    ":feature:home:ui",
    ":feature:devicebridge",
    ":feature:purchase:ui",
    ":feature:purchase:api",
    ":feature:purchase:impl",
    ":core:file",
    ":core:date",
    ":core:resources",
    ":core:navigation",
    ":core:designsystem",
    ":core:database",
    ":shared"
)
