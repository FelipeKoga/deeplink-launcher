package dev.koga.deeplinklauncher.di

import dev.koga.deeplinklauncher.repository.DeepLinkRepository
import dev.koga.deeplinklauncher.repository.FolderRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { DeepLinkRepository(get()) }
    single { FolderRepository(get()) }
}