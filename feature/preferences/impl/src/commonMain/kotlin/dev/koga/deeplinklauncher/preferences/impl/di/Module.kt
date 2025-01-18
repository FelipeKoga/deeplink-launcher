package dev.koga.deeplinklauncher.preferences.impl.di

import dev.koga.deeplinklauncher.preferences.api.repository.PreferencesRepository
import dev.koga.deeplinklauncher.preferences.impl.repository.DefaultPreferencesRepository
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val preferencesImplModule = module {
    singleOf(::DefaultPreferencesRepository) bind PreferencesRepository::class

    includes(platformModule)
}

internal expect val platformModule: Module
