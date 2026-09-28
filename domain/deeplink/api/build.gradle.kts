plugins {
    alias(libs.plugins.deeplinkLauncher.multiplatform)
    alias(libs.plugins.kotlinSerialization)
}

// Contract of the deeplink domain: models, repositories and ports shared by every
// screen feature. No UI, navigation or vendor types (docs/MODULARIZATION.md).
kotlin {
    explicitApi()

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.date)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.datetime)
        }
    }
}
