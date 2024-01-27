plugins {
    id("dev.koga.deeplinklauncher.application")
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
    
//    debugImplementation(libs.compose.ui.tooling)
}