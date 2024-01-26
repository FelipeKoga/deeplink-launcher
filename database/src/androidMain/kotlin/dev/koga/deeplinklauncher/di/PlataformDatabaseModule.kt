package dev.koga.deeplinklauncher.di

import dev.koga.deeplinklauncher.database.DeepLinkLauncherDatabase
import org.koin.dsl.module

internal actual val platformDatabaseModule = module {
    single<DeepLinkLauncherDatabase> { DeepLinkLauncherDatabase(get()) }
}