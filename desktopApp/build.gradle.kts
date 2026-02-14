import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.aboutLibraries)
}

kotlin {
    jvmToolchain(17)
    jvm()

    sourceSets {
        jvmMain.dependencies {
            implementation(projects.shared)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(compose.desktop.currentOs)
        }
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        buildTypes.release.proguard {
            isEnabled.set(false)
        }

        nativeDistributions {
            includeAllModules = true

            packageVersion = "1.12.0"
            packageName = "DeepLink Launcher"
            description = "Manage & Launch deeplinks easily"
            appResourcesRootDir.set(project.layout.projectDirectory.dir("resources"))

            linux {
                iconFile.set(project.file("assets/icon.png"))
            }

            windows {
                iconFile.set(project.file("assets/icon.ico"))
                dirChooser = false
                perUserInstall = true
                shortcut = true
            }

            macOS {
                bundleID = "dev.koga.deeplinklauncher"
                iconFile.set(project.file("assets/icon.icns"))
            }

            targetFormats(
                TargetFormat.Exe,
                TargetFormat.Rpm,
                TargetFormat.Deb,
                TargetFormat.Msi,
                TargetFormat.Pkg,
                TargetFormat.Dmg,
            )
        }
    }
}