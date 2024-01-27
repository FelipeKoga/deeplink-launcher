package dev.koga.deeplinklauncher.di

import dev.koga.deeplinklauncher.database.AndroidDriverFactory
import dev.koga.deeplinklauncher.database.DeepLinkLauncherDatabase
import dev.koga.deeplinklauncher.database.DriverFactory
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module

internal actual val platformDatabaseModule = module {
    singleOf(::AndroidDriverFactory) bind DriverFactory::class
    single<DeepLinkLauncherDatabase> { DeepLinkLauncherDatabase(get()) }
}