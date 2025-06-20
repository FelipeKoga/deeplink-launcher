import extension.setupBinariesFramework

plugins {
    id("dev.koga.deeplinklauncher.multiplatform")
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    setupBinariesFramework("deeplink.ui")

    sourceSets {
        commonMain.dependencies {
            api(projects.feature.deeplink.api)

            implementation(projects.core.designsystem)
            implementation(projects.core.navigation)
            implementation(projects.core.resources)
            implementation(projects.core.platform)
            implementation(projects.core.coroutines)

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.viewmodel)
            implementation(libs.compose.navigation)
            implementation(libs.kotlinx.immutable)
            implementation(libs.material3.windowSizeClass)

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.materialIconsExtended)
        }
    }
}

android {
    namespace = "dev.koga.deeplinklauncher.deeplink.ui"
}