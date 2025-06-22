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
    ":feature:home:api",
    ":feature:home:impl",
    ":feature:deeplink:api",
    ":feature:deeplink:impl",
    ":feature:deeplink:ui-component",
    ":feature:data-transfer:api",
    ":feature:data-transfer:impl",
    ":feature:settings:api",
    ":feature:settings:impl",
    ":feature:device-bridge:api",
    ":feature:device-bridge:impl",
    ":feature:purchase:ui",
    ":feature:purchase:api",
    ":feature:purchase:impl",
    ":core:file",
    ":core:date",
    ":core:resources",
    ":core:navigation",
    ":core:designsystem",
    ":core:database",
    ":core:preferences",
    ":core:platform",
    ":core:coroutines",
    ":core:ui-event",
    ":shared",
    ":baselineprofile"
)
