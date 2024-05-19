package dev.koga.deeplinklauncher.preferences.di

import dev.koga.deeplinklauncher.datasource.PreferencesDataSource
import dev.koga.deeplinklauncher.preferences.PreferencesDataStore
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val preferencesModule = module {
    singleOf(::PreferencesDataStore) bind PreferencesDataSource::class

    includes(preferencesPlatformModule)
}

expect val preferencesPlatformModule: Module
