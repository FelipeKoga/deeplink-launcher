import extension.setupBinariesFramework

plugins {
    alias(libs.plugins.deeplinkLauncher.multiplatform)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    explicitApi()
    
    setupBinariesFramework("deeplink.api")

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.preferences)
            implementation(projects.core.date)
            implementation(projects.core.navigation)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.immutable)
        }

        jvmMain.dependencies {
            implementation(projects.feature.deviceBridge.api)
        }
    }
}

android {
    namespace = "dev.koga.deeplinklauncher.deeplink.api"
}