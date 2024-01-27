plugins {
    id("dev.koga.deeplinklauncher.multiplatform")
}


kotlin {
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "navigation"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.voyager.navigator)
        }
    }
}

android {
    namespace = "dev.koga.navigation"
}