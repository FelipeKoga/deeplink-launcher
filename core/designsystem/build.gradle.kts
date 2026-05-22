

plugins {
    alias(libs.plugins.deeplinkLauncher.composeMultiplatform)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.core.resources)
            api(libs.composeIcons.tablerIcons)
            implementation(libs.kotlinx.immutable)

            implementation(compose.components.uiToolingPreview)
            implementation(compose.preview)
        }
    }
}

dependencies {
    debugImplementation(libs.androidx.ui.tooling)
}