


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
            implementation(libs.compose.navigation)
            implementation(libs.kotlinx.serialization.json)
        }
    }
}