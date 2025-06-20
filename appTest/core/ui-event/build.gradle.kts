
import extension.setupBinariesFramework

plugins {
    id("dev.koga.deeplinklauncher.multiplatform")
}

kotlin {
    setupBinariesFramework("uievent")

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.coroutines)

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.koin.core)
        }
    }
}

android {
    namespace = "dev.koga.deeplinklauncher.uievent"
}
