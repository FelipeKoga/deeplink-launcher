package dev.koga.deeplinklauncher.di

import dev.koga.deeplinklauncher.screen.HomeScreenModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val homeUiModule = module {
    factoryOf(::HomeScreenModel)
}

expect val platformHomeUiModule: Module
