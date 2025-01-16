import extension.setupBinariesFramework

plugins {
    id("dev.koga.deeplinklauncher.multiplatform")
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    setupBinariesFramework("importexport.impl")

    sourceSets {
        commonMain.dependencies {
            implementation(projects.feature.importExport.api)
            implementation(projects.feature.deeplink.api)
            implementation(projects.core.file)

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.immutable)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.koin.core)
        }
    }
}

android {
    namespace = "dev.koga.deeplinklauncher.importexport.impl"
}