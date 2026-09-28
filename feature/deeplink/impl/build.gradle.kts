

plugins {
    alias(libs.plugins.deeplinkLauncher.composeMultiplatform)
    alias(libs.plugins.stability.analyzer)
}

kotlin {
    explicitApi()

    sourceSets {
        commonMain.dependencies {
            implementation(projects.feature.deeplink.api)
            implementation(projects.feature.deeplink.ui)
            implementation(projects.domain.deeplink.api)
            implementation(projects.core.preferences)
            implementation(projects.core.date)
            implementation(projects.core.designsystem)
            implementation(projects.core.navigation)
            implementation(projects.core.platform)
            implementation(projects.core.coroutines)
            implementation(projects.core.ui)
            implementation(projects.core.uiEvent)

            implementation(projects.library.analytics.api)

            implementation(libs.koin.core)
            implementation(libs.koin.viewmodel)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.immutable)

            implementation(libs.compose.navigation)
            implementation(libs.compose.runtime)
            implementation(libs.navigationevent.compose)

            implementation(libs.material3.windowSizeClass)
            implementation(libs.haze)
            implementation(libs.haze.materials)

            implementation(compose.components.uiToolingPreview)
            implementation(compose.preview)
        }
    }
}

dependencies {
    debugImplementation(libs.androidx.ui.tooling)
}