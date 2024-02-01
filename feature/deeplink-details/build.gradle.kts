import extension.binariesFrameworkConfig
import org.jetbrains.compose.ExperimentalComposeLibrary

plugins {
    id("dev.koga.deeplinklauncher.multiplatform")
    alias(libs.plugins.jetbrainsCompose)
}

kotlin {
    binariesFrameworkConfig("deeplink-details")

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.domain)
            implementation(projects.core.designsystem)
            implementation(projects.core.navigation)
            implementation(projects.core.sharedui)
            implementation(projects.core.resources)

            implementation(libs.kotlinx.coroutines.core)

            implementation(libs.koin.compose)

            implementation(libs.voyager.screenmodel)
            implementation(libs.voyager.navigator)
            implementation(libs.voyager.koin)

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.components.resources)

            implementation(libs.moko.resources.compose)
            implementation(libs.kotlinx.immutable)
        }
    }
}

android {
    namespace = "dev.koga.deeplink.details"
}