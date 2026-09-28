package dev.koga.deeplinklauncher.deeplink.impl.di

import dev.koga.deeplinklauncher.deeplink.impl.application.EnrichDeepLinkForDetails
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

/** Deeplink screens. The domain they use is bound by deepLinkDomainModule. */
public val deepLinkModule: Module = module {
    singleOf(::EnrichDeepLinkForDetails)

    viewModelOf(::DeepLinkDetailsViewModel)
    viewModelOf(::FolderDetailsViewModel)
    viewModelOf(::LinkDeepLinkForFolderViewModel)
    viewModelOf(::AddFolderViewModel)
    singleOf(::DeepLinkNavigationGraph) bind NavigationGraph::class

    includes(platformModule)
}

internal expect val platformModule: Module
