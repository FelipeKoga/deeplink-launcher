plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("dev.koga.deeplinklauncher.code-analysis")
}

kotlin {
    applyDefaultHierarchyTemplate()
    jvmToolchain(17)
    jvm()
    androidTarget()

    sourceSets {
        commonMain.dependencies {
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.8.0")
        }
    }
}

android {
    compileSdk = AndroidAppConfiguration.COMPILE_SDK
    defaultConfig {
        minSdk = AndroidAppConfiguration.MIN_SDK
    }
}

