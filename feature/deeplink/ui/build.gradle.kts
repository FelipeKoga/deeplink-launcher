

plugins {
    alias(libs.plugins.deeplinkLauncher.composeMultiplatform)
}

kotlin {
    explicitApi()

    sourceSets {
        commonMain.dependencies {
            implementation(projects.domain.deeplink.api)
            implementation(projects.core.resources)
            implementation(projects.core.designsystem)
            implementation(projects.core.date)
            implementation(projects.core.platform)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.immutable)
            implementation(libs.koin.core)

            implementation(compose.components.uiToolingPreview)
            implementation(compose.preview)
        }
    }
}

dependencies {
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.kotlinx.datetime)
}