plugins {
    alias(libs.plugins.deeplinkLauncher.multiplatform)
}

// Test doubles owned by the deeplink domain, plus the contract suite that both the
// fakes and the SQL implementation must pass. Only test source sets depend on it.
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
