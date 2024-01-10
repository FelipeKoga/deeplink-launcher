package dev.koga.deeplinklauncher.di

import app.cash.sqldelight.db.SqlDriver
import dev.koga.deeplinklauncher.database.DatabaseDriverFactory
import dev.koga.deeplinklauncher.database.DeepLinkLauncherDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val databaseModule = module {

    single<SqlDriver> { DatabaseDriverFactory(androidContext()).createDriver() }

    single<DeepLinkLauncherDatabase> { DeepLinkLauncherDatabase(get()) }
}
