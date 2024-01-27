package dev.koga.deeplinklauncher.di

import dev.koga.deeplinklauncher.usecase.deeplink.DeleteDeepLink
import dev.koga.deeplinklauncher.usecase.deeplink.GetCurrentDeepLinks
import dev.koga.deeplinklauncher.usecase.deeplink.GetDeepLinkById
import dev.koga.deeplinklauncher.usecase.deeplink.GetDeepLinkByLink
import dev.koga.deeplinklauncher.usecase.deeplink.GetDeepLinksStream
import dev.koga.deeplinklauncher.usecase.deeplink.UpsertDeepLink
import dev.koga.deeplinklauncher.usecase.folder.DeleteFolder
import dev.koga.deeplinklauncher.usecase.folder.GetFolderById
import dev.koga.deeplinklauncher.usecase.folder.GetFolderDeepLinksStream
import dev.koga.deeplinklauncher.usecase.folder.GetFoldersStream
import dev.koga.deeplinklauncher.usecase.folder.UpsertFolder
import org.koin.dsl.module


val domainModule = module {
    single { DeleteFolder(get()) }
    single { GetFolderById(get()) }
    single { GetFoldersStream(get()) }
    single { GetFolderDeepLinksStream(get()) }
    single { UpsertFolder(get()) }

    single { GetDeepLinksStream(get()) }
    single { GetCurrentDeepLinks(get()) }
    single { UpsertDeepLink(get()) }
    single { DeleteDeepLink(get()) }
    single { GetDeepLinkByLink(get()) }
    single { GetDeepLinkById(get()) }
}