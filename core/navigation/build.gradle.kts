import extension.binariesFrameworkConfig

plugins {
    id("dev.koga.deeplinklauncher.multiplatform")
}


kotlin {
    binariesFrameworkConfig("navigation")

    sourceSets {
        commonMain.dependencies {
            implementation(libs.voyager.navigator)
        }
    }
}

android {
    namespace = "dev.koga.navigation"
}