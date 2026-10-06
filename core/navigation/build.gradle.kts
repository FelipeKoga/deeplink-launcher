


plugins {
    alias(libs.plugins.deeplinkLauncher.multiplatform)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.deeplinkLauncher.metro)
}

kotlin {
    explicitApi()
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.coroutines)
            api(libs.navigation3.ui)
            implementation(libs.kotlinx.serialization.json)
        }
    }
}