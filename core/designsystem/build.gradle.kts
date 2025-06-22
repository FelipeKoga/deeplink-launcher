import extension.setupBinariesFramework

plugins {
    alias(libs.plugins.deeplinkLauncher.composeMultiplatform)
}

kotlin {
    setupBinariesFramework("designsystem")

    sourceSets {
        commonMain.dependencies {
            api(projects.core.resources)
            api(libs.composeIcons.tablerIcons)
            implementation(libs.kotlinx.immutable)
        }
    }
}

android {
    namespace = "dev.koga.designsystem"
}
