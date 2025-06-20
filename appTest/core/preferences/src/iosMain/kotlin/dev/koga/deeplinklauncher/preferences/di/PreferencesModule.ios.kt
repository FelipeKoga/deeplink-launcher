package dev.koga.deeplinklauncher.preferences.di

import dev.koga.deeplinklauncher.preferences.datastore.dataStore
import org.koin.dsl.module

actual val platformModule = module {
    single { dataStore() }
}
