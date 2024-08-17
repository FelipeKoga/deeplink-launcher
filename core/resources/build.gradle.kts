import extension.setFrameworkBaseName

plugins {
    id("dev.koga.deeplinklauncher.multiplatform")
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    setFrameworkBaseName("resources")

    sourceSets {
        commonMain.dependencies {
            implementation(compose.components.resources)
        }
        androidMain.dependencies {
            implementation(libs.compose.runtime)
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