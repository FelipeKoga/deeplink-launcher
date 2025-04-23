package dev.koga.deeplinklauncher.deeplink.common.di

import dev.koga.deeplinklauncher.deeplink.common.repository.DeepLinkRepository
import dev.koga.deeplinklauncher.deeplink.common.repository.FolderRepository
import dev.koga.deeplinklauncher.deeplink.common.usecase.DuplicateDeepLink
import dev.koga.deeplinklauncher.deeplink.common.usecase.GetAutoSuggestionLinks
import dev.koga.deeplinklauncher.deeplink.common.usecase.GetDeepLinksAndFolderStream
import dev.koga.deeplinklauncher.deeplink.common.repository.impl.DeepLinkRepositoryImpl
import dev.koga.deeplinklauncher.deeplink.common.repository.impl.FolderRepositoryImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

public val deeplinkModule: Module = module {
    singleOf(::DeepLinkRepositoryImpl) bind DeepLinkRepository::class
    singleOf(::FolderRepositoryImpl) bind FolderRepository::class
    singleOf(::DuplicateDeepLink)
    singleOf(::GetAutoSuggestionLinks)
    singleOf(::GetDeepLinksAndFolderStream)
    includes(platformModule)
}

public expect val platformModule: Module
