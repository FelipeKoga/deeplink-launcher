plugins {
    alias(libs.plugins.deeplinkLauncher.multiplatform)
    alias(libs.plugins.sqlDelight)
}

// Implementation of the deeplink domain: SQLDelight schema and repositories,
// platform actuals of the ports and their DI bindings. Only :shared depends on it.
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

// Same database name, package and schema location as before the move out of
// :core:database, so existing installs keep their data and migrations.
sqldelight {
    databases {
        create(name = "DeepLinkLauncherDatabase") {
            packageName.set("dev.koga.deeplinklauncher.database")
        }
    }
    linkSqlite = true
}
