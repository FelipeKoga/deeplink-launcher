plugins {
    alias(libs.plugins.deeplinkLauncher.multiplatform)
}

// Product-agnostic SQLite infrastructure: platform drivers and column adapters.
// Schemas live with the domain that owns them (e.g. :domain:deeplink:impl).
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
