import extension.binariesFrameworkConfig

plugins {
    id("dev.koga.deeplinklauncher.multiplatform")
}


kotlin {
    binariesFrameworkConfig("navigation")

    sourceSets {
        commonMain.dependencies {
            implementation(libs.voyager.navigator)
            implementation(libs.voyager.bottomSheet)
        }
    }
}

android {
    namespace = "dev.koga.navigation"
}