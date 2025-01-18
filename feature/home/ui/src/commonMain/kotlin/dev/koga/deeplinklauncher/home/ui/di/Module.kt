package dev.koga.deeplinklauncher.home.ui.di

import dev.koga.deeplinklauncher.home.ui.screen.HomeScreenModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val homeUiModule = module {
    factoryOf(::HomeScreenModel)

    includes(platformHomeUiModule)
}

expect val platformHomeUiModule: Module
