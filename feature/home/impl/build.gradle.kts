

plugins {
    alias(libs.plugins.deeplinkLauncher.composeMultiplatform)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.feature.home.api)
            implementation(projects.feature.deeplink.api)
            implementation(projects.feature.deeplink.uiComponent)
            implementation(projects.feature.settings.api)

            implementation(projects.core.preferences)
            implementation(projects.core.date)
            implementation(projects.core.designsystem)
            implementation(projects.core.navigation)
            implementation(projects.core.resources)
            implementation(projects.core.coroutines)

            implementation(libs.compose.navigation)

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.viewmodel)
            implementation(libs.kotlinx.immutable)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.material3.windowSizeClass)

            implementation(libs.haze)
            implementation(libs.haze.materials)
        }

        jvmMain.dependencies {
            implementation(projects.feature.deviceBridge.api)
        }
    }
}