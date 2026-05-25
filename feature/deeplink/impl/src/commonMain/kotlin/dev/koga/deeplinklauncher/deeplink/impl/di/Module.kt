package dev.koga.deeplinklauncher.deeplink.impl.di

import dev.koga.deeplinklauncher.deeplink.api.application.EnrichDeepLinkForDetails
import dev.koga.deeplinklauncher.deeplink.api.application.EnrichDeepLinksForList
import dev.koga.deeplinklauncher.deeplink.api.domain.repository.DeepLinkRepository
import dev.koga.deeplinklauncher.deeplink.api.domain.repository.FolderRepository
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.DuplicateDeepLink
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.GetAutoSuggestionLinks
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.GetDeepLinksAndFolderStream
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.LinkDeepLinkToFolder
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.SaveDeepLinkAssertion
import dev.koga.deeplinklauncher.deeplink.impl.application.EnrichDeepLinkForDetailsImpl
import dev.koga.deeplinklauncher.deeplink.impl.application.EnrichDeepLinksForListImpl
import dev.koga.deeplinklauncher.deeplink.impl.data.repository.DeepLinkRepositoryImpl
import dev.koga.deeplinklauncher.deeplink.impl.data.repository.FolderRepositoryImpl
import dev.koga.deeplinklauncher.deeplink.impl.domain.usecase.DuplicateDeepLinkImpl
import dev.koga.deeplinklauncher.deeplink.impl.domain.usecase.GetAutoSuggestionLinksImpl
import dev.koga.deeplinklauncher.deeplink.impl.domain.usecase.GetDeepLinksAndFolderStreamImpl
import dev.koga.deeplinklauncher.deeplink.impl.domain.usecase.LinkDeepLinkToFolderImpl
import dev.koga.deeplinklauncher.deeplink.impl.domain.usecase.SaveDeepLinkAssertionImpl
import dev.koga.deeplinklauncher.deeplink.impl.ui.addfolder.AddFolderViewModel
import dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.DeepLinkDetailsViewModel
import dev.koga.deeplinklauncher.deeplink.impl.ui.folderdetails.FolderDetailsViewModel
import dev.koga.deeplinklauncher.deeplink.impl.ui.linkdeeplinkforfolder.LinkDeepLinkForFolderViewModel
import dev.koga.deeplinklauncher.deeplink.impl.ui.navigation.DeepLinkNavigationGraph
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
    singleOf(::LinkDeepLinkToFolderImpl) bind LinkDeepLinkToFolder::class
    singleOf(::SaveDeepLinkAssertionImpl) bind SaveDeepLinkAssertion::class
    singleOf(::EnrichDeepLinksForListImpl) bind EnrichDeepLinksForList::class
    singleOf(::EnrichDeepLinkForDetailsImpl) bind EnrichDeepLinkForDetails::class

    viewModelOf(::DeepLinkDetailsViewModel)
    viewModelOf(::FolderDetailsViewModel)
    viewModelOf(::LinkDeepLinkForFolderViewModel)
    viewModelOf(::AddFolderViewModel)
    singleOf(::DeepLinkNavigationGraph) bind NavigationGraph::class

    includes(platformModule)
}

internal expect val platformModule: Module
