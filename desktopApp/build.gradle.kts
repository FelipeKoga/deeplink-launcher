import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("dev.koga.deeplinklauncher.code-analysis")
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
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
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
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
            modules("java.sql")
            modules("jdk.unsupported")

            packageVersion = DesktopAppConfiguration.VERSION_NAME
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