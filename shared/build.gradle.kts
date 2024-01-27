plugins {
    id("dev.koga.deeplinklauncher.multiplataform")
    kotlin("plugin.serialization") version "1.9.20"
}

kotlin {
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
            api(projects.domain)
            api(projects.database)

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.koin.core)
            implementation(libs.voyager.screenmodel)
            api(libs.kotlinx.datetime)
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.koin.android)
            }
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "dev.koga.shared"
}