

plugins {
    alias(libs.plugins.deeplinkLauncher.composeMultiplatform)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.feature.deeplink.api)
            implementation(projects.core.resources)
            implementation(projects.core.designsystem)
            implementation(libs.composeIcons.tablerIcons)
        }
    }
}