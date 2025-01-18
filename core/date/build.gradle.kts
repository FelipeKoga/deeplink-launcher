import extension.setupBinariesFramework

plugins {
    id("dev.koga.deeplinklauncher.multiplatform")
}

kotlin {
    setupBinariesFramework("date")

    sourceSets {
        commonMain.dependencies {
            api(libs.kotlinx.datetime)
        }
    }
}

android {
    namespace = "dev.koga.deeplinklauncher.date"
}
