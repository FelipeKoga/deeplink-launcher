plugins {
    alias(libs.plugins.deeplinkLauncher.multiplatform)
    alias(libs.plugins.deeplinkLauncher.metro)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.coroutines)

            implementation(libs.kotlinx.coroutines.core)
        }
    }
}