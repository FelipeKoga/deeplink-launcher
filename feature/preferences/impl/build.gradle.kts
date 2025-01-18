import extension.setupBinariesFramework

plugins {
    id("dev.koga.deeplinklauncher.multiplatform")
}

kotlin {
    setupBinariesFramework("preferences.impl")

    sourceSets {
        commonMain.dependencies {
            implementation(projects.feature.preferences.api)

            implementation(libs.koin.core)
            implementation(libs.androidx.datastore.preferences.core)
        }
    }
}

android {
    namespace = "dev.koga.deeplinklauncher.preferences.impl"
}