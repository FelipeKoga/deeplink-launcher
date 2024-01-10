package dev.koga.deeplinklauncher.di

import dev.koga.deeplinklauncher.repository.DeepLinkRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<DeepLinkRepository> { DeepLinkRepository(get()) }
}