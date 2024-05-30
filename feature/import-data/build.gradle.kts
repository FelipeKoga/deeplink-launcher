plugins {
    id("dev.koga.deeplinklauncher.multiplatform")
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.domain)
            implementation(projects.core.designsystem)
            implementation(projects.core.navigation)
            implementation(projects.core.sharedui)

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.immutable)
            implementation(libs.koin.compose)
            implementation(libs.voyager.screenmodel)
            implementation(libs.voyager.navigator)
            implementation(libs.voyager.koin)
            implementation(libs.mpfilepicker)

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
        }

        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
        }
    }
}

android {
    namespace = "dev.koga.importData"
}