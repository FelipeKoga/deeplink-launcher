package dev.koga.deeplinklauncher.preferences.di

import dev.koga.deeplinklauncher.preferences.PreferencesDataSource
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val preferencesModule = module {
    factoryOf(::PreferencesDataSource)
    includes(preferencesPlatformModule)
}


expect val preferencesPlatformModule: Module