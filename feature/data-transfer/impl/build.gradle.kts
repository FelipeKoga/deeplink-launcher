

plugins {
    alias(libs.plugins.deeplinkLauncher.screenshotTesting)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.deeplinkLauncher.metro)
}

kotlin {

    sourceSets {
        commonMain.dependencies {
            implementation(projects.feature.dataTransfer.api)
            implementation(projects.feature.deeplink.api)

            implementation(projects.core.file)
            implementation(projects.core.date)
            implementation(projects.core.platform)
            implementation(projects.core.designsystem)
            implementation(projects.core.navigation)
            implementation(projects.core.uiEvent)
            implementation(projects.library.analytics.api)

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.immutable)
            implementation(libs.kotlinx.serialization.json)

            implementation(libs.metrox.viewmodel.compose)

            implementation(libs.filekit.dialogs.compose)
            implementation(libs.navigation3.ui)

            implementation(compose.preview)
        }

        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
        }
    }
}

dependencies {
    debugImplementation(libs.androidx.ui.tooling)
}