plugins {
    alias(libs.plugins.deeplinkLauncher.multiplatform)
    alias(libs.plugins.sqlDelight)
}

kotlin {
    explicitApi()

    sourceSets {
        commonMain.dependencies {
            implementation(projects.domain.deeplink.api)
            implementation(projects.core.coroutines)
            implementation(projects.core.database)
            implementation(projects.core.date)
            implementation(projects.core.preferences)

            implementation(libs.koin.core)
            implementation(libs.sqldelight.coroutines.extensions)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)
        }

        androidMain.dependencies {
            implementation(libs.androidx.core)
        }

        jvmMain.dependencies {
            implementation(projects.library.deviceBridge.api)
        }

        jvmTest.dependencies {
            implementation(projects.domain.deeplink.testing)
            implementation(libs.sqldelight.jvm)
        }
    }
}

sqldelight {
    databases {
        create(name = "DeepLinkLauncherDatabase") {
            packageName.set("dev.koga.deeplinklauncher.database")
        }
    }
    linkSqlite = true
}
