package dev.koga.deeplinklauncher.di

import dev.koga.deeplinklauncher.FolderDetailsScreenModel
import org.koin.dsl.module

val folderDetailsUiModule = module {
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
