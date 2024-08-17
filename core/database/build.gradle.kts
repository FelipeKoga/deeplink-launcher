import extension.setFrameworkBaseName

plugins {
    id("dev.koga.deeplinklauncher.multiplatform")
    kotlin("plugin.serialization") version "1.9.20"
    alias(libs.plugins.sqlDelight)
}

kotlin {
    setFrameworkBaseName("database")

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

        nativeMain.dependencies {
            implementation("app.cash.sqldelight:native-driver:2.0.0")
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
