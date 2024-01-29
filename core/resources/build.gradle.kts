import extension.binariesFrameworkConfig

plugins {
    id("dev.koga.deeplinklauncher.multiplatform")
    id(libs.plugins.moko.multiplatform.resources.get().pluginId)
}

kotlin {
    binariesFrameworkConfig("resources")

    sourceSets {
        commonMain.dependencies {
            implementation(libs.moko.resources.core)
        }

        androidMain {
            dependsOn(commonMain.get())
        }

        iosMain {
            dependsOn(commonMain.get())
        }

        jvmMain {
            dependsOn(commonMain.get())
        }
    }
}

android {
    namespace = "dev.koga.resources"
}

multiplatformResources {
    multiplatformResourcesPackage = "dev.koga.resources"
}