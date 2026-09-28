plugins {
    alias(libs.plugins.deeplinkLauncher.multiplatform)
}

kotlin {
    explicitApi()

    sourceSets {
        commonMain.dependencies {
            api(projects.domain.deeplink.api)
            api(kotlin("test"))
            api(libs.kotlinx.coroutines.test)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)
        }

        jvmMain.dependencies {
            api(kotlin("test-junit"))
        }

        androidMain.dependencies {
            api(kotlin("test-junit"))
        }
    }
}
