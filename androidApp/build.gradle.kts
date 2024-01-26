plugins {
    id("dev.koga.deeplinklauncher.application")
}

android {
    namespace = "dev.koga.deeplinklauncher.android"
}

dependencies {
    implementation(projects.shared)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.androidx.activity.compose)

    implementation(libs.voyager.navigator)
    implementation(libs.voyager.screenmodel)
    implementation(libs.voyager.transitions)
    implementation(libs.voyager.koin)
    implementation(libs.voyager.tab.navigator)
    implementation(libs.voyager.bottomsheet)

    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    implementation(libs.androidx.material3.android)

    implementation(libs.balloon.compose)

    debugImplementation(libs.compose.ui.tooling)
}