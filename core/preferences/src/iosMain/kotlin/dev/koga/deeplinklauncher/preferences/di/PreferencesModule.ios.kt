package dev.koga.deeplinklauncher.preferences.di

import dev.koga.deeplinklauncher.preferences.datastore.dataStore
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

public actual val platformModule: Module = module {
    singleOf(::dataStore)
}
