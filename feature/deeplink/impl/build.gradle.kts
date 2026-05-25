

plugins {
    alias(libs.plugins.deeplinkLauncher.composeMultiplatform)
    alias(libs.plugins.stability.analyzer)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    explicitApi()

    sourceSets {
        commonMain.dependencies {
            implementation(projects.feature.deeplink.api)
            implementation(projects.feature.deeplink.uiComponent)
            implementation(projects.core.preferences)
            implementation(projects.core.date)
            implementation(projects.core.database)
            implementation(projects.core.designsystem)
            implementation(projects.core.navigation)
            implementation(projects.core.platform)
            implementation(projects.core.coroutines)
            implementation(projects.core.ui)
            implementation(projects.core.uiEvent)

            implementation(libs.koin.core)
            implementation(libs.koin.viewmodel)
            implementation(libs.sqldelight.coroutines.extensions)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.immutable)
            implementation(libs.kotlinx.serialization.json)

            implementation(libs.compose.navigation)
            implementation(libs.compose.runtime)

            implementation(libs.material3.windowSizeClass)
            implementation(libs.haze)
            implementation(libs.haze.materials)

            implementation(compose.components.uiToolingPreview)
            implementation(compose.preview)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
        }

        jvmMain.dependencies {
            implementation(projects.library.deviceBridge.api)
            implementation(projects.core.file)
        }
    }
}

dependencies {
    debugImplementation(libs.androidx.ui.tooling)
}