import extension.setupBinariesFramework

plugins {
    alias(libs.plugins.deeplinkLauncher.composeMultiplatform)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    setupBinariesFramework("datatransfer.impl")

    sourceSets {
        commonMain.dependencies {
            implementation(projects.feature.dataTransfer.api)
            implementation(projects.feature.deeplink.api)

            implementation(projects.core.file)
            implementation(projects.core.date)
            implementation(projects.core.platform)
            implementation(projects.core.designsystem)
            implementation(projects.core.navigation)
            implementation(projects.core.uiEvent)

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.immutable)
            implementation(libs.kotlinx.serialization.json)

            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.viewmodel)

            implementation(libs.mpfilepicker)
            implementation(libs.compose.navigation)
        }
    }
}

android {
    namespace = "dev.koga.deeplinklauncher.datatransfer.impl"
}
