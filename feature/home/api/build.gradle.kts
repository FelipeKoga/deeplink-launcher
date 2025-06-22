import extension.setupBinariesFramework

plugins {
    alias(libs.plugins.deeplinkLauncher.multiplatform)
    alias(libs.plugins.kotlinSerialization)
}
kotlin {
    explicitApi()
    
    setupBinariesFramework("home.api")

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.navigation)
            implementation(libs.kotlinx.serialization.json)
        }
    }
}

android {
    namespace = "dev.koga.deeplinklauncher.home.api"
}