package dev.koga.deeplinklauncher.home.impl.di

import dev.koga.deeplinklauncher.home.impl.ui.HomeViewModel
import dev.koga.deeplinklauncher.home.impl.ui.navigation.HomeNavigationGraph
import dev.koga.deeplinklauncher.navigation.NavigationGraph
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val homeModule = module {
    viewModelOf(::HomeViewModel)
    singleOf(::HomeNavigationGraph) bind NavigationGraph::class

    includes(platformHomeUiModule)
}

expect val platformHomeUiModule: Module
