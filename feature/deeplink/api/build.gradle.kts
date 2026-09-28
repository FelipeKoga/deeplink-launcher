plugins {
    alias(libs.plugins.deeplinkLauncher.multiplatform)
    alias(libs.plugins.kotlinSerialization)
}

// Only the navigation entry points of the deeplink screens. The deeplink domain
// (models, repositories, ports) is :domain:deeplink:api.
kotlin {
    explicitApi()
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.navigation)
            implementation(libs.kotlinx.serialization.json)
        }
    }
}
