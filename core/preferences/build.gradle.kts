import extension.setupBinariesFramework

plugins {
    id("dev.koga.deeplinklauncher.multiplatform")
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    explicitApi()
    setupBinariesFramework("preferences")

    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.androidx.datastore.preferences.core)
        }
    }
}

android {
    namespace = "dev.koga.deeplinklauncher.preferences"
}