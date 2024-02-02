import extension.binariesFrameworkConfig

plugins {
    id("dev.koga.deeplinklauncher.multiplatform")
    kotlin("plugin.serialization") version "1.9.20"
}

kotlin {
    binariesFrameworkConfig("domain")

    sourceSets {
        commonMain.dependencies {
            api(libs.kotlinx.datetime)

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.koin.core)
            implementation(libs.sql.coroutines.extensions)
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.sql.android.driver)
                implementation(libs.koin.android)
            }
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "dev.koga.domain"
}
