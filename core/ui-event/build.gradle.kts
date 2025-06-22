plugins {
    alias(libs.plugins.deeplinkLauncher.multiplatform)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.coroutines)

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.koin.core)
        }
    }
}