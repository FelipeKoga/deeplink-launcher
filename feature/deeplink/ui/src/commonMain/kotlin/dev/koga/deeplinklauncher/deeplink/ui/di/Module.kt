package dev.koga.deeplinklauncher.deeplink.ui.di

import dev.koga.deeplinklauncher.deeplink.ui.deeplink.screen.details.DeepLinkDetailsViewModel
import dev.koga.deeplinklauncher.deeplink.ui.folder.screen.details.FolderDetailsViewModel
import dev.koga.deeplinklauncher.deeplink.ui.navigation.DeepLinkNavigation
import dev.koga.deeplinklauncher.navigation.NavigationGraph
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val deeplinkUiModule = module {
    viewModelOf(::DeepLinkDetailsViewModel)
    viewModelOf(::FolderDetailsViewModel)
    singleOf(::DeepLinkNavigation) bind NavigationGraph::class
}
