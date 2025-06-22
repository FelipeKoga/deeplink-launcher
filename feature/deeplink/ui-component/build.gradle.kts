import extension.setupBinariesFramework

plugins {
    alias(libs.plugins.deeplinkLauncher.composeMultiplatform)
}

kotlin {
    setupBinariesFramework("deeplink.uicomponent")

    sourceSets {
        commonMain.dependencies {
            implementation(projects.feature.deeplink.api)
            implementation(projects.core.resources)
            implementation(projects.core.designsystem)
            implementation(libs.composeIcons.tablerIcons)
        }
    }
}

android {
    namespace = "dev.koga.deeplink.uicomponent"
}
