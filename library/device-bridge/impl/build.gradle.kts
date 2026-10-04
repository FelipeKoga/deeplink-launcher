plugins {
    id("kotlin")
    alias(libs.plugins.deeplinkLauncher.codeAnalysis)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.deeplinkLauncher.metro)
}

kotlin {
    jvmToolchain(17)

    dependencies {
        implementation(projects.library.deviceBridge.api)
        implementation(libs.kotlinx.coroutines.core)
        implementation(libs.kotlinx.serialization.json)
        testImplementation(libs.junit)
        testImplementation(libs.kotlinx.coroutines.test)
    }
}