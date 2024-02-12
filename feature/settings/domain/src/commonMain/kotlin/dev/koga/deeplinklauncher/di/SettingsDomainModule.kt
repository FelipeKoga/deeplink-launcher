package dev.koga.deeplinklauncher.di

import org.koin.core.module.Module
import org.koin.dsl.module

val settingsDomainModule = module {
    includes(platformModule)
}

expect val platformModule: Module
