import extension.binariesFrameworkConfig
import org.jetbrains.compose.ExperimentalComposeLibrary

plugins {
    id("dev.koga.deeplinklauncher.multiplatform")
}

kotlin {
    binariesFrameworkConfig("settings.domain")

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.domain)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.koin.core)
            implementation(libs.kotlinx.immutable)
        }
    }
}

android {
    namespace = "dev.koga.settings.domain"
}