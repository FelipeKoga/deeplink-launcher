package dev.koga.deeplinklauncher.di

import dev.koga.deeplinklauncher.usecase.deeplink.DeleteDeepLink
import dev.koga.deeplinklauncher.usecase.deeplink.GetCurrentDeepLinks
import dev.koga.deeplinklauncher.usecase.deeplink.GetDeepLinkById
import dev.koga.deeplinklauncher.usecase.deeplink.GetDeepLinkByLink
import dev.koga.deeplinklauncher.usecase.deeplink.GetDeepLinksJsonPreview
import dev.koga.deeplinklauncher.usecase.deeplink.GetDeepLinksPlainTextPreview
import dev.koga.deeplinklauncher.usecase.deeplink.GetDeepLinksStream
import dev.koga.deeplinklauncher.usecase.deeplink.UpsertDeepLink
import dev.koga.deeplinklauncher.usecase.folder.DeleteFolder
import dev.koga.deeplinklauncher.usecase.folder.GetFolderById
import dev.koga.deeplinklauncher.usecase.folder.GetFolderDeepLinksStream
import dev.koga.deeplinklauncher.usecase.folder.GetFoldersStream
import dev.koga.deeplinklauncher.usecase.folder.UpsertFolder
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


val domainModule = module {
    singleOf(::DeleteFolder)
    singleOf(::GetFolderById)
    singleOf(::GetFoldersStream)
    singleOf(::GetFolderDeepLinksStream)
    singleOf(::UpsertFolder)

    singleOf(::GetDeepLinksStream)
    singleOf(::GetCurrentDeepLinks)
    singleOf(::UpsertDeepLink)
    singleOf(::DeleteDeepLink)
    singleOf(::GetDeepLinkByLink)
    singleOf(::GetDeepLinkById)

    singleOf(::GetDeepLinksJsonPreview)
    singleOf(::GetDeepLinksPlainTextPreview)

    includes(platformDomainModule)
}

internal expect val platformDomainModule: Module