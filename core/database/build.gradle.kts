

plugins {
    alias(libs.plugins.deeplinkLauncher.multiplatform)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.sqlDelight)
    alias(libs.plugins.deeplinkLauncher.metro)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.datetime)
            implementation(libs.sqldelight.coroutines.extensions)
        }

        androidMain.dependencies {
            implementation(libs.sqldelight.android.driver)
        }

        jvmMain.dependencies {
            implementation(projects.core.platform)
            implementation(libs.sqldelight.jvm)
        }

        jvmTest.dependencies {
            implementation(libs.junit)
        }

        androidUnitTest.dependencies {
            implementation(libs.junit)
            implementation(libs.robolectric)
        }

        iosMain.dependencies {
            implementation(libs.native.driver)
            implementation(libs.stately)
        }

        iosTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

sqldelight {
    databases {
        create(name = "DeepLinkLauncherDatabase") {
            packageName.set("dev.koga.deeplinklauncher.database")
            schemaOutputDirectory.set(file("src/commonMain/sqldelight/databases"))
            verifyMigrations.set(true)
        }
    }
    linkSqlite = true
}

tasks.matching { it.name == "verifyCommonMainDeepLinkLauncherDatabaseMigration" }.configureEach {
    inputs.files(fileTree("src/commonMain/sqldelight") { include("**/*.db") })
        .withPathSensitivity(PathSensitivity.RELATIVE)
}

tasks.matching { it.name == "jvmTest" }.configureEach {
    dependsOn("verifySqlDelightMigration")
}
