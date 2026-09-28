plugins {
    alias(libs.plugins.deeplinkLauncher.multiplatform)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    explicitApi()

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.date)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.datetime)
        }
    }
}
