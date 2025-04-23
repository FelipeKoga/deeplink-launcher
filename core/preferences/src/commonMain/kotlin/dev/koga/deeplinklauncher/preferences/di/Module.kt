package dev.koga.deeplinklauncher.preferences.di

import dev.koga.deeplinklauncher.preferences.repository.DefaultPreferencesRepository
import dev.koga.deeplinklauncher.preferences.repository.PreferencesRepository
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

public val preferencesModule: Module = module {
    singleOf(::DefaultPreferencesRepository) bind PreferencesRepository::class

    includes(platformModule)
}

public expect val platformModule: Module
