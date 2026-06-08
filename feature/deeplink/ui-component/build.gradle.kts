

plugins {
    alias(libs.plugins.deeplinkLauncher.composeMultiplatform)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.feature.deeplink.api)
            implementation(projects.core.resources)
            implementation(projects.core.designsystem)
            implementation(projects.core.date)
            implementation(projects.core.platform)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.immutable)

            implementation(compose.components.uiToolingPreview)
            implementation(compose.preview)
        }
    }
}

dependencies {
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.kotlinx.datetime)
}