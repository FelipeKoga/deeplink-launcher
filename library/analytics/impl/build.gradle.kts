plugins {
    alias(libs.plugins.deeplinkLauncher.multiplatform)
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

            implementation(libs.koin.core)
        }

        androidMain.dependencies {
            implementation(libs.firebase.analytics)
            implementation(libs.koin.android)
        }
    }
}
