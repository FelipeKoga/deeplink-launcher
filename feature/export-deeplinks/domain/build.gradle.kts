import extension.binariesFrameworkConfig

plugins {
    id("dev.koga.deeplinklauncher.multiplatform")
}

kotlin {
    binariesFrameworkConfig("exportDeeplinks.domain")

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.domain)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.koin.core)
            implementation(libs.kotlinx.immutable)
            implementation(libs.kotlinx.serialization.json)
        }
    }
}

android {
    namespace = "dev.koga.exportDeeplinks.domain"
}