package dev.koga.deeplinklauncher.deeplink.ui.di

import dev.koga.deeplinklauncher.deeplink.ui.deeplink.screen.details.DeepLinkDetailsScreenModel
import dev.koga.deeplinklauncher.deeplink.ui.folder.screen.details.FolderDetailsScreenModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val deeplinkUiModule = module {
    factoryOf(::DeepLinkDetailsScreenModel)
    factoryOf(::FolderDetailsScreenModel)
}
