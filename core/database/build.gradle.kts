plugins {
    id("dev.koga.deeplinklauncher.multiplatform")
    kotlin("plugin.serialization") version "1.9.20"
    alias(libs.plugins.sqlDelight)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.domain)

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.koin.core)
            implementation(libs.sqldelight.coroutines.extensions)
        }

        androidMain.dependencies {
            implementation(libs.sqldelight.android.driver)
        }

        jvmMain.dependencies {
            implementation(libs.sqldelight.jvm)
        }
    }
}

android {
    namespace = "dev.koga.database"
}

sqldelight {
    databases {
        create(name = "DeepLinkLauncherDatabase") {
            packageName.set("dev.koga.deeplinklauncher.database")
        }
    }
}
