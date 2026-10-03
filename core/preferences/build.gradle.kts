

plugins {
    alias(libs.plugins.deeplinkLauncher.multiplatform)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.deeplinkLauncher.metro)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.androidx.datastore.preferences.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.coroutines.core)
        }

        jvmMain.dependencies {
            implementation(projects.core.platform)
        }
    }
}