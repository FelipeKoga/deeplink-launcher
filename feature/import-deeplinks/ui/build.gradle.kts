import extension.binariesFrameworkConfig
import org.jetbrains.compose.ExperimentalComposeLibrary

plugins {
    id("dev.koga.deeplinklauncher.multiplatform")
    alias(libs.plugins.jetbrainsCompose)
}

kotlin {
    binariesFrameworkConfig("importdeeplinks.ui")

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.domain)
            implementation(projects.core.designsystem)
            implementation(projects.core.navigation)
            implementation(projects.core.sharedui)
            implementation(projects.feature.importDeeplinks.domain)

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.immutable)
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
        }

        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
        }
    }
}

android {
    namespace = "dev.koga.importdeeplinks.ui"
}