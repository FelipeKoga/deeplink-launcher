package dev.koga.deeplinklauncher.settings.ui.di

import dev.koga.deeplinklauncher.settings.ui.SettingsViewModel
import dev.koga.deeplinklauncher.settings.ui.navigation.SettingsNavigation
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val settingsUiModule = module {
    viewModelOf(::SettingsViewModel)
    singleOf(::SettingsNavigation)
}
