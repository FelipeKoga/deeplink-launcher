package dev.koga.deeplinklauncher.module

import cafe.adriel.voyager.core.registry.screenModule
import dev.koga.deeplinklauncher.ImportScreen
import dev.koga.navigation.SharedScreen


val featureImportScreenModule = screenModule {
    register<SharedScreen.ImportDeepLinks> { ImportScreen() }
}