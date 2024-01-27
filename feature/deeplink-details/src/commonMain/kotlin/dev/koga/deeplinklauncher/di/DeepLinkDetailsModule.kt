package dev.koga.deeplinklauncher.di

import dev.koga.deeplinklauncher.DeepLinkDetailScreenModel
import org.koin.dsl.module

val deepLinkDetailsModule = module {
    factory {
        DeepLinkDetailScreenModel(
            deepLinkId = get(),
            getDeepLinkById = get(),
            launchDeepLink = get(),
            shareDeepLink = get(),
            deleteDeepLink = get(),
            upsertDeepLink = get(),
            getFoldersStream = get(),
            upsertFolder = get()
        )
    }
}