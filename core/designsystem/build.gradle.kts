import extension.binariesFrameworkConfig
import org.jetbrains.compose.ExperimentalComposeLibrary

plugins {
    id("dev.koga.deeplinklauncher.multiplatform")
    alias(libs.plugins.jetbrainsCompose)
}

kotlin {
    binariesFrameworkConfig("designsystem")

    sourceSets {
        commonMain.dependencies {
            api(projects.core.resources)
            api(libs.moko.resources.compose)
            implementation(libs.kotlinx.immutable)

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.components.resources)
        }

        androidMain {
            dependsOn(commonMain.get())
        }
    }
}

android {
    namespace = "dev.koga.designsystem"
}
