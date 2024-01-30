package dev.koga.deeplinklauncher.navigation

import cafe.adriel.voyager.core.registry.screenModule
import dev.koga.deeplinklauncher.ExportScreen
import dev.koga.navigation.SharedScreen

val exportScreenModule = screenModule {
    register<SharedScreen.ExportDeepLinks> { ExportScreen() }
}
