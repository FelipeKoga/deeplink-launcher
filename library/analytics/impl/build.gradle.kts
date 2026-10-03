plugins {
    alias(libs.plugins.deeplinkLauncher.multiplatform)
    alias(libs.plugins.deeplinkLauncher.metro)
}

kotlin {
    sourceSets {
        val noOpMain by creating {
            dependsOn(commonMain.get())
        }

        jvmMain.get().dependsOn(noOpMain)
        iosMain.get().dependsOn(noOpMain)

        commonMain.dependencies {
            implementation(projects.library.analytics.api)

        }

        androidMain.dependencies {
            implementation(libs.firebase.analytics)
        }
    }
}
