package dev.koga.deeplinklauncher.navigation

import cafe.adriel.voyager.core.registry.screenModule
import dev.koga.deeplinklauncher.screen.ImportScreen
import dev.koga.deeplinklauncher.SharedScreen

val importScreenModule = screenModule {
    register<SharedScreen.ImportDeepLinks> { ImportScreen() }
}