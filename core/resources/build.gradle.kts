import extension.setupBinariesFramework

plugins {
    alias(libs.plugins.deeplinkLauncher.composeMultiplatform)
}

kotlin {
    setupBinariesFramework("resources")
}

android {
    namespace = "dev.koga.resources"
}

compose.resources {
    publicResClass = true
    packageOfResClass = "dev.koga.resources"
    generateResClass = always
}