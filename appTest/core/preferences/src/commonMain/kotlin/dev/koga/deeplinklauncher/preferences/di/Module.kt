package dev.koga.deeplinklauncher.preferences.di

import dev.koga.deeplinklauncher.preferences.repository.PreferencesDataSource
import dev.koga.deeplinklauncher.preferences.repository.PreferencesDataStore
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val preferencesModule = module {
    singleOf(::PreferencesDataStore) bind PreferencesDataSource::class

    includes(platformModule)
}

internal expect val platformModule: Module
