package dev.koga.deeplinklauncher.di

import dev.koga.deeplinklauncher.platform.PlatformInfo
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

actual val platformModule: Module = module {
    factoryOf(::PlatformInfo)
}
