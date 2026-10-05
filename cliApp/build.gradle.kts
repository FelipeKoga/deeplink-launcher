plugins {
    id("kotlin")
    application
    alias(libs.plugins.deeplinkLauncher.codeAnalysis)
    alias(libs.plugins.kotlinSerialization)
}

version = "0.1.0"

kotlin {
    jvmToolchain(17)
}

application {
    applicationName = "deeplink"
    mainClass.set("dev.koga.deeplinklauncher.cli.MainKt")
}

tasks.jar {
    manifest {
        attributes("Implementation-Version" to project.version)
    }
}

dependencies {
    implementation(libs.clikt)
    implementation(libs.kotlinx.serialization.json)
    testImplementation(kotlin("test"))
    testImplementation(libs.junit)
}
