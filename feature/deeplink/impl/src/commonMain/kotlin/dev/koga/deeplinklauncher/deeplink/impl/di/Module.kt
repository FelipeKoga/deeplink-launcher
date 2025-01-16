package dev.koga.deeplinklauncher.deeplink.impl.di

import dev.koga.deeplinklauncher.deeplink.api.repository.DeepLinkRepository
import dev.koga.deeplinklauncher.deeplink.api.repository.FolderRepository
import dev.koga.deeplinklauncher.deeplink.impl.repository.DeepLinkRepositoryImpl
import dev.koga.deeplinklauncher.deeplink.impl.repository.FolderRepositoryImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val deeplinkImplModule = module {
    singleOf(::DeepLinkRepositoryImpl) bind DeepLinkRepository::class
    singleOf(::FolderRepositoryImpl) bind FolderRepository::class
    includes(platformModule)
}

expect val platformModule: Module
