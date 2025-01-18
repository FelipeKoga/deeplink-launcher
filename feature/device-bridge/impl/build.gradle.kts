plugins {
    id("kotlin")
    id("dev.koga.deeplinklauncher.code-analysis")
    alias(libs.plugins.kotlin.serialization)
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