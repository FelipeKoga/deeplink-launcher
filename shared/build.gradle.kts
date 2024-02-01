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
            implementation(projects.feature.home)
            implementation(projects.feature.export)
            implementation(projects.feature.deeplinkDetails)
            implementation(projects.feature.folderDetails)

            implementation(projects.feature.settings.domain)
            implementation(projects.feature.settings.ui)
            implementation(projects.feature.importDeeplink.domain)
            implementation(projects.feature.importDeeplink.ui)

            api(projects.core.domain)
            implementation(projects.core.designsystem)
            implementation(projects.core.navigation)
            implementation(projects.core.database)

            implementation(libs.koin.core)
            implementation(libs.kotlinx.coroutines.core)

            implementation(libs.koin.compose)

            implementation(libs.voyager.screenmodel)
            implementation(libs.voyager.navigator)
            implementation(libs.voyager.transitions)

            implementation(libs.voyager.koin)

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.components.resources)

            implementation(libs.moko.resources.core)
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

multiplatformResources {
    multiplatformResourcesPackage = "dev.koga.deeplinklauncher"
}
