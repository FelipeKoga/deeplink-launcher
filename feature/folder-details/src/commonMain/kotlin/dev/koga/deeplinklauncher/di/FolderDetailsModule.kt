package dev.koga.deeplinklauncher.di

import dev.koga.deeplinklauncher.FolderDetailsScreenModel
import org.koin.dsl.module

val folderDetailsModule = module {
    factory {
        FolderDetailsScreenModel(
            folderId = get(),
            deleteFolder = get(),
            getFolderById = get(),
            getFolderDeepLinksStream = get(),
            upsertFolder = get(),
        )
    }
}
