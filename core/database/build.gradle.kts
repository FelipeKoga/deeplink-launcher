plugins {
    alias(libs.plugins.deeplinkLauncher.multiplatform)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.sqldelight.runtime)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)
            implementation(libs.koin.core)
        }

        androidMain.dependencies {
            implementation(libs.sqldelight.android.driver)
        }

        jvmMain.dependencies {
            implementation(projects.core.platform)
            implementation(libs.sqldelight.jvm)
        }

        iosMain.dependencies {
            implementation(libs.native.driver)
            implementation(libs.stately)
        }
    }
}
