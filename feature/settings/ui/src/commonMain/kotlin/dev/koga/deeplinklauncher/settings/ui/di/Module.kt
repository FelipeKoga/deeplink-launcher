package dev.koga.deeplinklauncher.settings.ui.di

import dev.koga.deeplinklauncher.settings.ui.screen.SettingsScreenModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val settingsUiModule = module {
    factoryOf(::SettingsScreenModel)
}
