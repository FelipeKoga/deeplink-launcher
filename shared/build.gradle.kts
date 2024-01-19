plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    kotlin("plugin.serialization") version "1.9.20"
    alias(libs.plugins.sqlDelight)
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.koin.core)
            implementation(libs.sql.coroutines.extensions)
            implementation(libs.voyager.screenmodel)
            api(libs.kotlinx.datetime)
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.ktor.client.android)
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
    namespace = "dev.koga.deeplinklauncher"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
}

sqldelight {
    databases {
        create(name = "DeepLinkLauncherDatabase") {
            packageName.set("dev.koga.deeplinklauncher.database")
        }
    }
}