plugins {
    id("dev.koga.deeplinklauncher.application")
    alias(libs.plugins.google.services)
    alias(libs.plugins.crashlytics)
}

android {
    namespace = "dev.koga.deeplinklauncher.android"
}

dependencies {
    implementation(projects.shared)

    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.androidx.activity.compose)
    implementation(libs.voyager.navigator)
    implementation(libs.voyager.transitions)

    implementation(libs.koin.compose)
    implementation(libs.koin.core)
}
