package dev.koga.deeplinklauncher.deeplink.impl.di

import dev.koga.deeplinklauncher.deeplink.api.repository.DeepLinkRepository
import dev.koga.deeplinklauncher.deeplink.api.repository.FolderRepository
import dev.koga.deeplinklauncher.deeplink.api.usecase.DuplicateDeepLink
import dev.koga.deeplinklauncher.deeplink.api.usecase.GetAutoSuggestionLinks
import dev.koga.deeplinklauncher.deeplink.api.usecase.GetDeepLinksAndFolderStream
import dev.koga.deeplinklauncher.deeplink.impl.repository.DeepLinkRepositoryImpl
import dev.koga.deeplinklauncher.deeplink.impl.repository.FolderRepositoryImpl
import dev.koga.deeplinklauncher.deeplink.impl.ui.addfolder.AddFolderViewModel
import dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.DeepLinkDetailsViewModel
import dev.koga.deeplinklauncher.deeplink.impl.ui.folderdetails.FolderDetailsViewModel
import dev.koga.deeplinklauncher.deeplink.impl.ui.navigation.DeepLinkNavigationGraph
import dev.koga.deeplinklauncher.deeplink.impl.usecase.DuplicateDeepLinkImpl
import dev.koga.deeplinklauncher.deeplink.impl.usecase.GetAutoSuggestionLinksImpl
import dev.koga.deeplinklauncher.deeplink.impl.usecase.GetDeepLinksAndFolderStreamImpl
import dev.koga.deeplinklauncher.navigation.NavigationGraph
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

public val deepLinkModule: Module = module {
    singleOf(::DeepLinkRepositoryImpl) bind DeepLinkRepository::class
    singleOf(::FolderRepositoryImpl) bind FolderRepository::class
    singleOf(::DuplicateDeepLinkImpl) bind DuplicateDeepLink::class
    singleOf(::GetAutoSuggestionLinksImpl) bind GetAutoSuggestionLinks::class
    singleOf(::GetDeepLinksAndFolderStreamImpl) bind GetDeepLinksAndFolderStream::class

    viewModelOf(::DeepLinkDetailsViewModel)
    viewModelOf(::FolderDetailsViewModel)
    viewModelOf(::AddFolderViewModel)
    singleOf(::DeepLinkNavigationGraph) bind NavigationGraph::class

    includes(platformModule)
}

internal expect val platformModule: Module
