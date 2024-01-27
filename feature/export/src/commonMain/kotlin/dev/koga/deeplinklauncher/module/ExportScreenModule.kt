package dev.koga.deeplinklauncher.module

import cafe.adriel.voyager.core.registry.screenModule
import dev.koga.deeplinklauncher.ExportScreen
import dev.koga.navigation.SharedScreen


val featureExportScreenModule = screenModule {
    register<SharedScreen.ExportDeepLinks> { ExportScreen() }
}