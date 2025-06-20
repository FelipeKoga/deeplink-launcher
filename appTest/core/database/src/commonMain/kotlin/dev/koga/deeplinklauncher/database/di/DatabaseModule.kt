package dev.koga.deeplinklauncher.database.di

import dev.koga.deeplinklauncher.database.DatabaseProvider
import org.koin.core.module.Module
import org.koin.dsl.module

val databaseModule = module {
    single { DatabaseProvider(get()).create() }
    includes(platformDatabaseModule)
}

internal expect val platformDatabaseModule: Module
