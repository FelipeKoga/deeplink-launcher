import extension.setFrameworkBaseName

plugins {
    id("dev.koga.deeplinklauncher.multiplatform")
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    setFrameworkBaseName("navigation")

    sourceSets {
        commonMain.dependencies {
            implementation(libs.voyager.navigator)
            implementation(libs.voyager.bottomSheet)
            implementation(compose.runtime)
        }
    }
}

android {
    namespace = "dev.koga.navigation"
}