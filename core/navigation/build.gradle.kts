
import extension.setupBinariesFramework

plugins {
    alias(libs.plugins.deeplinkLauncher.multiplatform)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    explicitApi()
    setupBinariesFramework("navigation")

    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)
            implementation(projects.core.coroutines)
            implementation(libs.compose.navigation)
            implementation(libs.kotlinx.serialization.json)
        }
    }
}

android {
    namespace = "dev.koga.deeplinklauncher.navigation"
}
