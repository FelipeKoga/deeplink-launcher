
import extension.setupBinariesFramework

plugins {
    id("dev.koga.deeplinklauncher.multiplatform")
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    setupBinariesFramework("navigation")

    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)
            implementation(projects.core.coroutines)
            implementation(compose.runtime)
            implementation(compose.material)
            implementation(libs.compose.navigation)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.voyager.navigator)
            implementation(libs.voyager.bottomSheet)
        }
    }
}

android {
    namespace = "dev.koga.navigation"
}
