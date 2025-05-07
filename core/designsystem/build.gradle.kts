import extension.setupBinariesFramework

plugins {
    id("dev.koga.deeplinklauncher.multiplatform")
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    setupBinariesFramework("designsystem")

    sourceSets {
        commonMain.dependencies {
            api(projects.core.resources)
            api(libs.composeIcons.tablerIcons)

            implementation(libs.kotlinx.immutable)
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
        }
    }
}

android {
    namespace = "dev.koga.designsystem"
}
