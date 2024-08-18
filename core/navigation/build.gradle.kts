
import extension.setupBinariesFramework

plugins {
    id("dev.koga.deeplinklauncher.multiplatform")
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    setupBinariesFramework("navigation")

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.material)

            implementation(libs.voyager.navigator)
            implementation(libs.voyager.bottomSheet)
        }
    }
}

android {
    namespace = "dev.koga.navigation"
}
