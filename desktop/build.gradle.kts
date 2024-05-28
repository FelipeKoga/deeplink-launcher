import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("dev.koga.deeplinklauncher.code-analysis")
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

group = "dev.koga.deeplinklauncher.desktop"
version = Configuration.versionCode

kotlin {
    jvm("desktop") {
        withJava()
        compilations.all {
            kotlinOptions.jvmTarget = Configuration.JVM_TARGET
        }
    }

    sourceSets {
        val desktopMain by getting {
            dependencies {
                implementation(project(":shared"))

                implementation(compose.desktop.currentOs)
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.components.resources)
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "dev.koga.deeplinklauncher.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Rpm, TargetFormat.Deb)
            packageName = "deeplinklauncher"
            macOS {
                bundleID = "dev.koga.deeplinklauncher.desktop"
            }
        }
    }
}