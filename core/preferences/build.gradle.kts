import extension.setupBinariesFramework

plugins {
    id("dev.koga.deeplinklauncher.multiplatform")
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    setupBinariesFramework("preferences")

    sourceSets {
        commonMain.dependencies {
            implementation(libs.datastore.core)
            implementation(libs.koin.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.coroutines.core)
        }
    }
}

android {
    namespace = "dev.koga.deeplinklauncher.preferences"
}