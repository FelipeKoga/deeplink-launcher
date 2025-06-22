

plugins {
    alias(libs.plugins.deeplinkLauncher.multiplatform)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.kotlinx.datetime)
        }
    }
}