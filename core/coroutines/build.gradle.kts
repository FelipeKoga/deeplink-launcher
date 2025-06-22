
import extension.setupBinariesFramework

plugins {
    alias(libs.plugins.deeplinkLauncher.multiplatform)
}

kotlin {
    setupBinariesFramework("coroutines")

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.koin.core)
        }
    }
}

android {
    namespace = "dev.koga.deeplinklauncher.coroutines"
}
