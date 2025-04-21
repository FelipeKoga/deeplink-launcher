package dev.koga.deeplinklauncher.deeplink.ui.di

import dev.koga.deeplinklauncher.deeplink.ui.deeplink.screen.details.DeepLinkDetailsViewModel
import dev.koga.deeplinklauncher.deeplink.ui.folder.screen.details.FolderDetailsViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val deeplinkUiModule = module {
    viewModelOf(::DeepLinkDetailsViewModel)
    viewModelOf(::FolderDetailsViewModel)
}
