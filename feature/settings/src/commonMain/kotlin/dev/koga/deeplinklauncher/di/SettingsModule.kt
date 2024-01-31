package dev.koga.deeplinklauncher.di

import dev.koga.deeplinklauncher.SettingsScreenModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val settingsModule = module {
    factoryOf(::SettingsScreenModel)
    includes(platformModule)
}

expect val platformModule: Module