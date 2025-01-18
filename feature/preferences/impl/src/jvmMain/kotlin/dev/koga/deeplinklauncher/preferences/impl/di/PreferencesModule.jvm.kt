package dev.koga.deeplinklauncher.preferences.impl.di

import dev.koga.deeplinklauncher.preferences.impl.datastore.dataStore
import org.koin.dsl.module

actual val platformModule = module {
    single { dataStore() }
}
