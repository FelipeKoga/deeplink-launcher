package dev.koga.deeplinklauncher.di

import dev.koga.deeplinklauncher.FolderDetailsScreenModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val folderDetailsUiModule = module {
    factoryOf(::FolderDetailsScreenModel)
}
