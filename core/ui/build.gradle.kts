plugins {
    alias(libs.plugins.deeplinkLauncher.composeMultiplatform)
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.core)
            implementation(libs.androidx.activity.compose)
        }
        commonMain.dependencies {
            implementation(libs.material3.windowSizeClass)
        }
    }
}