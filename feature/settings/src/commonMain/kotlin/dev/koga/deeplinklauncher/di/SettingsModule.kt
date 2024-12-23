package dev.koga.deeplinklauncher.di

import dev.koga.deeplinklauncher.SettingsScreenModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val settingsUiModule = module {
    factoryOf(::SettingsScreenModel)
}