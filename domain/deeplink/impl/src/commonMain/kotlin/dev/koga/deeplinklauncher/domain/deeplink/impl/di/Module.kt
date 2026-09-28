package dev.koga.deeplinklauncher.domain.deeplink.impl.di

import dev.koga.deeplinklauncher.domain.deeplink.api.repository.DeepLinkRepository
import dev.koga.deeplinklauncher.domain.deeplink.api.repository.FolderRepository
import dev.koga.deeplinklauncher.domain.deeplink.api.usecase.DeleteAllDeepLinks
import dev.koga.deeplinklauncher.domain.deeplink.api.usecase.DeleteDeepLink
import dev.koga.deeplinklauncher.domain.deeplink.api.usecase.DuplicateDeepLink
import dev.koga.deeplinklauncher.domain.deeplink.api.usecase.GetAutoSuggestionLinks
import dev.koga.deeplinklauncher.domain.deeplink.api.usecase.LinkDeepLinkToFolder
import dev.koga.deeplinklauncher.domain.deeplink.impl.data.database.DatabaseProvider
import dev.koga.deeplinklauncher.domain.deeplink.impl.data.repository.DeepLinkRepositoryImpl
import dev.koga.deeplinklauncher.domain.deeplink.impl.data.repository.FolderRepositoryImpl
import dev.koga.deeplinklauncher.domain.deeplink.impl.usecase.DeleteAllDeepLinksImpl
import dev.koga.deeplinklauncher.domain.deeplink.impl.usecase.DeleteDeepLinkImpl
import dev.koga.deeplinklauncher.domain.deeplink.impl.usecase.DuplicateDeepLinkImpl
import dev.koga.deeplinklauncher.domain.deeplink.impl.usecase.GetAutoSuggestionLinksImpl
import dev.koga.deeplinklauncher.domain.deeplink.impl.usecase.GetDeepLinkFromClipboard
import dev.koga.deeplinklauncher.domain.deeplink.impl.usecase.LinkDeepLinkToFolderImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

public val deepLinkDomainModule: Module = module {
    single { DatabaseProvider(get()).create() }

    singleOf(::DeepLinkRepositoryImpl) bind DeepLinkRepository::class
    singleOf(::FolderRepositoryImpl) bind FolderRepository::class
    singleOf(::DuplicateDeepLinkImpl) bind DuplicateDeepLink::class
    singleOf(::DeleteDeepLinkImpl) bind DeleteDeepLink::class
    singleOf(::DeleteAllDeepLinksImpl) bind DeleteAllDeepLinks::class
    singleOf(::GetAutoSuggestionLinksImpl) bind GetAutoSuggestionLinks::class
    singleOf(::GetDeepLinkFromClipboard)
    singleOf(::LinkDeepLinkToFolderImpl) bind LinkDeepLinkToFolder::class

    includes(platformModule)
}

internal expect val platformModule: Module
