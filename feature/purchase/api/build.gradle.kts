import extension.envProperties
import extension.setupBinariesFramework

plugins {
    alias(libs.plugins.deeplinkLauncher.multiplatform)
    alias(libs.plugins.kotlinSerialization)
}

val envProperties = rootProject.envProperties()

kotlin {
    setupBinariesFramework("purchase.api")

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.navigation)
            implementation(libs.kotlinx.immutable)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.koin.core)
        }
    }
}

android {
    namespace = "dev.koga.deeplinklauncher.purchase.api"
}
