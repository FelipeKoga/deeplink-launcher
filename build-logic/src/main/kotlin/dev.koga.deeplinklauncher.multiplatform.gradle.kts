plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("dev.koga.deeplinklauncher.code-analysis")
}

kotlin {
    applyDefaultHierarchyTemplate()

    jvm {
        compilations.all {
            kotlinOptions {
                jvmTarget = Configuration.JVM_TARGET
            }
        }
    }

    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = Configuration.JVM_TARGET
            }
        }
    }
}

val libs: VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

android {
    compileSdk = Configuration.COMPILE_SDK
    defaultConfig {
        minSdk = Configuration.MIN_SDK
    }
}

