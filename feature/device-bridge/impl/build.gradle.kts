plugins {
    id("kotlin")
    alias(libs.plugins.deeplinkLauncher.codeAnalysis)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    jvmToolchain(17)

    dependencies {
        implementation(projects.feature.deviceBridge.api)
        implementation(libs.kotlinx.coroutines.core)
        implementation(libs.kotlinx.serialization.json)
        implementation(libs.koin.core)
        testImplementation(libs.junit)
        testImplementation(libs.kotlinx.coroutines.test)
    }
}