import extension.binariesFrameworkConfig

plugins {
    id("dev.koga.deeplinklauncher.multiplatform")
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    binariesFrameworkConfig("resources")

    sourceSets {
        commonMain.dependencies {
            implementation(compose.components.resources)
            implementation("androidx.compose.runtime:runtime:1.6.7")
        }
    }
}

android {
    namespace = "dev.koga.resources"
}

compose.resources {
    publicResClass = true
    packageOfResClass = "dev.koga.resources"
    generateResClass = always
}