package dev.koga.deeplinklauncher.navigation

import cafe.adriel.voyager.core.registry.screenModule
import dev.koga.deeplinklauncher.SettingsScreen
import dev.koga.deeplinklauncher.SharedScreen

val settingsScreenModule = screenModule {
    register<SharedScreen.Settings> { SettingsScreen() }
}
