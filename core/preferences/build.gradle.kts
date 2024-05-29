plugins {
    id("dev.koga.deeplinklauncher.multiplatform")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.domain)
            implementation(libs.koin.core)
            implementation(libs.androidx.datastore.preferences.core)
        }
    }
}

android {
    namespace = "dev.koga.preferences"
}