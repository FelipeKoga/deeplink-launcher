import extension.getLibrary
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("dev.koga.deeplinklauncher.code-analysis")
}

val libs: VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

kotlin {
    applyDefaultHierarchyTemplate()
    jvmToolchain(17)
    jvm()
    androidTarget()

    sourceSets {
        val mobileMain by creating {
            dependsOn(commonMain.get())
        }

        androidMain.get().dependsOn(mobileMain)
        iosMain.get().dependsOn(mobileMain)

        iosMain.dependencies {
            implementation(libs.getLibrary("stately"))
        }
    }

    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }
}

android {
    compileSdk = AndroidAppConfiguration.COMPILE_SDK
    defaultConfig {
        minSdk = AndroidAppConfiguration.MIN_SDK
    }
}

