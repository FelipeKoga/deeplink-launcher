package dev.koga.deeplinklauncher.home.ui.di

import dev.koga.deeplinklauncher.home.ui.HomeViewModel
import dev.koga.deeplinklauncher.home.ui.navigation.HomeNavigation
import dev.koga.deeplinklauncher.navigation.NavigationGraph
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val homeUiModule = module {
    viewModelOf(::HomeViewModel)
    singleOf(::HomeNavigation) bind NavigationGraph::class

    includes(platformHomeUiModule)
}

expect val platformHomeUiModule: Module
