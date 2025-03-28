import extension.setupBinariesFramework

plugins {
    id("dev.koga.deeplinklauncher.multiplatform")
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    setupBinariesFramework("home.ui")

    sourceSets {
        commonMain.dependencies {
            implementation(projects.feature.deeplink.ui)
            implementation(projects.feature.preferences.api)
            implementation(projects.core.date)
            implementation(projects.core.designsystem)
            implementation(projects.core.navigation)
            implementation(projects.core.resources)

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.koin.compose)
            implementation(libs.bundles.voyager)
            implementation(libs.kotlinx.immutable)
            implementation(libs.material3.windowSizeClass)

            implementation(libs.haze)
            implementation(libs.haze.materials)

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.materialIconsExtended)
        }

        jvmMain.dependencies {
            implementation(projects.feature.deviceBridge.api)
        }
    }
}

android {
    namespace = "dev.koga.deeplinklauncher.ui"
}