plugins {
    id("kotlin")
    alias(libs.plugins.deeplinkLauncher.codeAnalysis)
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    jvmToolchain(17)

    dependencies {
        implementation(libs.kotlinx.coroutines.core)
        implementation(libs.kotlinx.serialization.json)
    }
}