package dev.koga.deeplinklauncher.di

import dev.koga.deeplinklauncher.database.DriverFactory
import dev.koga.deeplinklauncher.database.NativeDriverFactory
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal actual val platformDatabaseModule: Module = module {
    singleOf(::NativeDriverFactory) bind DriverFactory::class
}
