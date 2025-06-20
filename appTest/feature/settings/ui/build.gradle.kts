import extension.setupBinariesFramework

plugins {
    id("dev.koga.deeplinklauncher.multiplatform")
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    setupBinariesFramework("settings.ui")

    sourceSets {
        commonMain.dependencies {
            implementation(projects.feature.deeplink.api)
            implementation(projects.feature.purchase.api)

            implementation(projects.core.preferences)
            implementation(projects.core.designsystem)
            implementation(projects.core.navigation)
            implementation(projects.core.platform)
            implementation(projects.core.coroutines)
            implementation(projects.core.uiEvent)

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.viewmodel)

            implementation(libs.kotlinx.immutable)
            implementation(libs.aboutlibraries.compose)
            implementation(libs.compose.navigation)

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
        }
    }
}

android {
    namespace = "dev.koga.deeplinklauncher.settings.ui"
}