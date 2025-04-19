package dev.koga.deeplinklauncher.home.ui.di

import dev.koga.deeplinklauncher.home.ui.HomeViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val homeUiModule = module {
    viewModelOf(::HomeViewModel)
    includes(platformHomeUiModule)
}

expect val platformHomeUiModule: Module
