import extension.binariesFrameworkConfig
import org.jetbrains.compose.ExperimentalComposeLibrary

plugins {
    id("dev.koga.deeplinklauncher.multiplatform")
    id(libs.plugins.moko.multiplatform.resources.get().pluginId)
    alias(libs.plugins.jetbrainsCompose)
}

kotlin {
    binariesFrameworkConfig("shared")

    sourceSets {
        commonMain.dependencies {
            implementation(projects.feature.home.ui)
            implementation(projects.feature.home.domain)
            implementation(projects.feature.settings.domain)
            implementation(projects.feature.settings.ui)
            implementation(projects.feature.exportDeeplinks.domain)
            implementation(projects.feature.exportDeeplinks.ui)
            implementation(projects.feature.importDeeplinks.domain)
            implementation(projects.feature.importDeeplinks.ui)
            implementation(projects.feature.deeplinkDetails.ui)
            implementation(projects.feature.folderDetails.ui)
            implementation(projects.core.domain)
            implementation(projects.core.designsystem)
            implementation(projects.core.navigation)
            implementation(projects.core.database)
            implementation(projects.core.preferences)

            implementation(libs.koin.core)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.moko.resources.core)
            implementation(libs.koin.compose)
            implementation(libs.voyager.bottomSheet)
            implementation(libs.voyager.navigator)
            implementation(libs.voyager.transitions)

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.material)
            implementation(compose.ui)
            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.components.resources)
        }

        androidMain {
            dependsOn(commonMain.get())
        }

        iosMain {
            dependsOn(commonMain.get())
        }
    }
}

android {
    namespace = "dev.koga.shared"
}