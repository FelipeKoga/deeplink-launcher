import extension.setupBinariesFramework

plugins {
    id("dev.koga.deeplinklauncher.multiplatform")
}

kotlin {
    setupBinariesFramework("deeplink.impl")

    sourceSets {
        commonMain.dependencies {
            implementation(projects.feature.deeplink.api)
            implementation(projects.core.preferences)

            implementation(projects.core.date)
            implementation(projects.core.database)

            implementation(libs.koin.core)
            implementation(libs.sqldelight.coroutines.extensions)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.immutable)
        }

        jvmMain.dependencies {
            implementation(projects.feature.deviceBridge.api)
        }
    }
}

android {
    namespace = "dev.koga.deeplinklauncher.deeplink.impl"
}