

plugins {
    alias(libs.plugins.deeplinkLauncher.multiplatform)
    alias(libs.plugins.kotlinSerialization)
}
kotlin {
    explicitApi()
    
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.navigation)
            implementation(libs.kotlinx.serialization.json)
        }
    }
}