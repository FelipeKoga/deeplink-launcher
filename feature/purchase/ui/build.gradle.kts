import extension.envProperties
import extension.setupBinariesFramework

plugins {
    id("dev.koga.deeplinklauncher.multiplatform")
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

val envProperties = rootProject.envProperties()

kotlin {
    setupBinariesFramework("purchase.ui")

    sourceSets {
        commonMain.dependencies {
            api(projects.feature.purchase.api)
            implementation(projects.core.designsystem)

            implementation(libs.kotlinx.immutable)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.koin.core)

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
        }
    }
}

android {
    namespace = "dev.koga.deeplinklauncher.purchase.ui"
}
