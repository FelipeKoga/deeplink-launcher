import extension.binariesFrameworkConfig
import org.jetbrains.compose.ExperimentalComposeLibrary

plugins {
    id("dev.koga.deeplinklauncher.multiplatform")
//    id(libs.plugins.moko.multiplatform.resources.get().pluginId)
    alias(libs.plugins.jetbrainsCompose)
}

kotlin {
    binariesFrameworkConfig("designsystem")

    sourceSets {
        commonMain.dependencies {
            api(projects.core.resources)
            implementation(libs.moko.resources.compose)

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.components.resources)
            implementation(libs.kotlinx.immutable)
        }

        androidMain {
            dependsOn(commonMain.get())
        }
    }
}

android {
    namespace = "dev.koga.designsystem"
}
