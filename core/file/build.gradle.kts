

plugins {
    alias(libs.plugins.deeplinkLauncher.multiplatform)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.core)
        }
        commonMain.dependencies {
            api(libs.kotlinx.datetime)
            api(libs.kotlinx.immutable)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.koin.core)
        }
    }
}
