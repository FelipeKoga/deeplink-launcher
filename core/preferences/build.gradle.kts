import extension.binariesFrameworkConfig

plugins {
    id("dev.koga.deeplinklauncher.multiplatform")
//    kotlin("plugin.serialization") version "1.9.20"
}

kotlin {
    binariesFrameworkConfig("preferences")

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.domain)
            implementation(libs.koin.core)
            implementation(libs.androidx.datastore.preferences.core)

//            implementation(libs.kotlinx.coroutines.core)
//            implementation(libs.kotlinx.serialization.json)
        }
    }
}

android {
    namespace = "dev.koga.preferences"
}