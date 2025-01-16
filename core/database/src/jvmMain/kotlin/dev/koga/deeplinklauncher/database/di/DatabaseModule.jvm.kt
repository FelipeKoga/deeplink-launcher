package dev.koga.deeplinklauncher.database.di

import dev.koga.deeplinklauncher.database.JvmDriverFactory
import dev.koga.deeplinklauncher.database.DriverFactory
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal actual val platformDatabaseModule: Module = module {
    singleOf(::JvmDriverFactory) bind DriverFactory::class
}
