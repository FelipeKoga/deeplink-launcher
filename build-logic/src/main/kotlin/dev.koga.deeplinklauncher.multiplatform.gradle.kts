import extension.libs

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
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        val mobileMain by creating {
            dependsOn(commonMain.get())
        }

        androidMain.get().dependsOn(mobileMain)
        iosMain.get().dependsOn(mobileMain)
    }

    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }
}

android {
    namespace = "dev.koga.deeplinklauncher.android" +
            project.name
                .replace(":", ".")
                .replace("-", ".")

    compileSdk = AndroidAppConfiguration.COMPILE_SDK
    defaultConfig {
        minSdk = AndroidAppConfiguration.MIN_SDK
    }
}

