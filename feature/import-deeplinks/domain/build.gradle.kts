plugins {
    id("dev.koga.deeplinklauncher.multiplatform")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.domain)
            implementation(libs.koin.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.immutable)
        }
    }
}

android {
    namespace = "dev.koga.importdeeplink.domain"
}