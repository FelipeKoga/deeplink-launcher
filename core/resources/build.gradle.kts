

plugins {
    alias(libs.plugins.deeplinkLauncher.composeMultiplatform)
}

compose.resources {
    publicResClass = true
    packageOfResClass = "dev.koga.resources"
    generateResClass = always
}