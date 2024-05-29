plugins {
    id("dev.koga.deeplinklauncher.multiplatform")
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(compose.components.resources)
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