import extension.setupBinariesFramework

plugins {
    id("dev.koga.deeplinklauncher.multiplatform")
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    setupBinariesFramework("importexport.ui")

    sourceSets {
        commonMain.dependencies {
            implementation(projects.feature.importExport.api)
            implementation(projects.core.designsystem)
            implementation(projects.core.navigation)
            implementation(projects.core.file)
            implementation(projects.core.date)
            implementation(projects.core.uiEvent)

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.immutable)
            implementation(libs.koin.compose)
            implementation(libs.koin.viewmodel)
            implementation(libs.compose.navigation)
            implementation(libs.mpfilepicker)

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
        }

        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
        }
    }
}

android {
    namespace = "dev.koga.deeplinklauncher.importexport.ui"
}