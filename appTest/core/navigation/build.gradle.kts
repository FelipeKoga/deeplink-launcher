
import extension.setupBinariesFramework

plugins {
    id("dev.koga.deeplinklauncher.multiplatform")
    alias(libs.plugins.kotlin.serialization)
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
